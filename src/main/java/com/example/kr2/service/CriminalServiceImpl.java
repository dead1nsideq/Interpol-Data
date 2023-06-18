package com.example.kr2.service;

import com.example.kr2.controller.NotFoundException;
import com.example.kr2.controller.ValidationException;
import com.example.kr2.entety.*;
import com.example.kr2.repository.CriminalGroupRepository;
import com.example.kr2.repository.CriminalRecordRepository;
import com.example.kr2.repository.CriminalRepository;
import com.example.kr2.repository.LanguageRepository;
import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class CriminalServiceImpl implements CriminalService {
    private final CriminalRepository criminalRepository;
    private final CriminalGroupRepository criminalGroupRepository;
    private final CriminalRecordRepository criminalRecordRepository;
    private final LanguageRepository languageRepository;

    @Override
    public Optional<Criminal> getCriminalById(Long id) {
        return Optional.ofNullable(criminalRepository.getCriminalById(id));
    }
    @Override
    @Transactional
    public void updateCriminalById(Long id,
                                   Criminal updatedCriminal,
                                   Dossier updatedDossier,
                                   CriminalGroup updatedCriminalGroup,
                                   @Valid @NotBlank String updatedLanguages,
                                   @Valid @NotBlank String updatedCriminalRecords,
                                   MultipartFile updatedImageFile) {
        if (updatedLanguages.isBlank() || updatedCriminalRecords.isBlank()) {
            throw new ValidationException();
        }
        AtomicReference<Optional<Criminal>> atomicReference = new AtomicReference<>();
        criminalRepository.findById(id).ifPresentOrElse(existingCriminal -> {
            existingCriminal.setFirstName(updatedCriminal.getFirstName());
            existingCriminal.setLastName(updatedCriminal.getLastName());
            existingCriminal.setNickName(updatedCriminal.getNickName());
            existingCriminal.setHeight(updatedCriminal.getHeight());
            existingCriminal.setCountry(updatedCriminal.getCountry());
            existingCriminal.setDateOfArrest(updatedCriminal.getDateOfArrest());
            existingCriminal.setDateOfBirth(updatedCriminal.getDateOfBirth());
            existingCriminal.setEyeColor(updatedCriminal.getEyeColor());
            existingCriminal.setHairColor(updatedCriminal.getHairColor());

            CriminalGroup criminalGroupFromDb = criminalGroupRepository
                    .getCriminalGroupByCriminalGroupNameAndCriminalGroupType(updatedCriminalGroup.getCriminalGroupName(), updatedCriminalGroup.getCriminalGroupType());
            existingCriminal.setCriminalGroup(Objects.requireNonNullElseGet(criminalGroupFromDb, () -> criminalGroupRepository.save(updatedCriminalGroup)));

            if (updatedImageFile != null && !updatedImageFile.isEmpty()) {
                try {
                    Image updatedImage = toImageEntity(updatedImageFile);
                    existingCriminal.setImage(updatedImage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            existingCriminal.getCriminalRecords().clear();
            splitStringToCriminalRecords(updatedCriminalRecords, existingCriminal);

            existingCriminal.getLanguages().clear();
            splitStringToLanguages(updatedLanguages, existingCriminal);


            existingCriminal.setDossier(updatedDossier);

            atomicReference.set(Optional.of(criminalRepository.save(existingCriminal)));
        }, () -> atomicReference.set(Optional.empty()));

        atomicReference.get();
    }

    @Override
    @Transactional
    public void addCriminal(Criminal criminal,
                            Dossier dossier,
                            CriminalGroup criminalGroup,
                            @Valid @NotEmpty String criminalLanguage,@Valid @NotEmpty String selectedCriminalRecords, MultipartFile file) {
        Image image;
        try {
            image = toImageEntity(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        criminal.addImageToCriminal(image);
        criminal.addDossierToCriminal(dossier);
        CriminalGroup criminalGroupFromDb = criminalGroupRepository
                .getCriminalGroupByCriminalGroupNameAndCriminalGroupType
                        (criminalGroup.getCriminalGroupName(),criminalGroup.getCriminalGroupType());
        if (criminalGroupFromDb == null) {
            criminalGroupFromDb = criminalGroup;
            criminalGroupRepository.save(criminalGroup);
        }
        criminal.setCriminalGroup(criminalGroupFromDb);
        splitStringToLanguages(criminalLanguage,criminal);
        splitStringToCriminalRecords(selectedCriminalRecords,criminal);
        criminalRepository.save(criminal);
    }

    @Override
    public List<Criminal> findCriminalsByInputParams(String inputCriminalFirstName, String inputCriminalLastName,
                                                     String inputCriminalNickName, String arrestedCriminals,
                                                     String criminalRecord, String textInDossier, String criminalGroupType,
                                                     String eyeColor, String hairColor, String sortBy, boolean order) {
        QCriminal criminal = QCriminal.criminal;
        BooleanBuilder predicate = new BooleanBuilder();
        if (!inputCriminalFirstName.isBlank()) {
            predicate.and(criminal.firstName.containsIgnoreCase(inputCriminalFirstName));
        }
        if (!arrestedCriminals.isBlank()) {
            switch (arrestedCriminals) {
                case "notArrested" -> predicate.and(criminal.dateOfArrest.isNull());
                case "onlyArrested" -> predicate.and(criminal.dateOfArrest.isNotNull());
            }
        }
        if (!inputCriminalLastName.isBlank()) {
            predicate.and(criminal.lastName.containsIgnoreCase(inputCriminalLastName));
        }
        if (!inputCriminalNickName.isBlank()) {
            predicate.and(criminal.nickName.containsIgnoreCase(inputCriminalNickName));
        }
        if (!criminalRecord.isBlank()) {
            predicate.and(criminal.criminalRecords.any().criminalRecord.equalsIgnoreCase(criminalRecord));
        }
        if (!textInDossier.isBlank()) {
            predicate.and(criminal.dossier.textOfDossier.contains(textInDossier));
        }
        if (CriminalGroupType.isValidValue(criminalGroupType)) {
            predicate.and(criminal.criminalGroup.criminalGroupType.eq(CriminalGroupType.valueOf(criminalGroupType)));
        }
        if (EyeColor.isValidValue(eyeColor)) {
            predicate.and(criminal.eyeColor.eq(EyeColor.valueOf(eyeColor)));
        }
        if (HairColor.isValidValue(hairColor)) {
            predicate.and(criminal.hairColor.eq(HairColor.valueOf(hairColor)));
        }
        Sort.Direction direction = order ? Sort.Direction.DESC : Sort.Direction.ASC;
        if (sortBy.isBlank()) {
            sortBy = "id";
        }
        Sort sort = Sort.by(direction,sortBy);
        return criminalRepository.findAll(predicate,sort);
    }

    @Override
    public boolean deleteCriminal(Long id) {
        if (criminalRepository.existsById(id)) {
            criminalRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void arrestCriminal(Long id) {
        Criminal criminal = getCriminalById(id).orElseThrow(NotFoundException::new);
        criminal.setDateOfArrest(LocalDate.now());
        criminalRepository.save(criminal);
    }

    private void splitStringToLanguages(String str, Criminal criminal) {
        String[] languages = str.split(",");
        List<Language> languageList = new ArrayList<>();
        for (String l : languages) {
            Language language = languageRepository.getLanguageByCriminalLanguage(l);
            if (language == null) {
                language = new Language();
                language.setCriminalLanguage(l);
            }
            criminal.addLanguageToCriminal(language);
            languageList.add(language);
        }
        languageRepository.saveAll(languageList);
    }

    private void splitStringToCriminalRecords(String str, Criminal criminal) {
        String[] criminalRecords = str.split(",");
        List<CriminalRecord> criminalRecordList = new ArrayList<>();
        for (String cr : criminalRecords) {
            CriminalRecord criminalRecord = criminalRecordRepository.getCriminalRecordByCriminalRecord(cr);
            if (criminalRecord == null) {
                criminalRecord = new CriminalRecord();
                criminalRecord.setCriminalRecord(cr);
            }
            criminal.addCriminalRecordToCriminal(criminalRecord);
            criminalRecordList.add(criminalRecord);
        }
        criminalRecordRepository.saveAll(criminalRecordList);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }
}
