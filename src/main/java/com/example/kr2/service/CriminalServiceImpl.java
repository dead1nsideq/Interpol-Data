package com.example.kr2.service;

import com.example.kr2.controller.NotFoundException;
import com.example.kr2.dto.CriminalDTO;
import com.example.kr2.dto.SortDTO;
import com.example.kr2.entety.*;
import com.example.kr2.mapper.Mapper;
import com.example.kr2.repository.CriminalGroupRepository;
import com.example.kr2.repository.CriminalRecordRepository;
import com.example.kr2.repository.CriminalRepository;
import com.example.kr2.repository.LanguageRepository;
import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
@Log4j2
public class CriminalServiceImpl implements CriminalService {
    private final CriminalRepository criminalRepository;
    private final CriminalGroupRepository criminalGroupRepository;
    private final CriminalRecordRepository criminalRecordRepository;
    private final LanguageRepository languageRepository;
    private final Mapper mapper;

    @Override
    public Optional<CriminalDTO> getCriminalById(Long id) {
        return Optional.ofNullable(mapper.criminaToCriminalDto(criminalRepository.getReferenceById(id)));
    }
    @Override
    @Transactional
    public void updateCriminalById(Long id,
                                   CriminalDTO dto,
                                   MultipartFile updatedImageFile) {
        Criminal updatedCriminal = mapper.criminaDtoToCriminal(dto);
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
            existingCriminal.getDossier().setTextOfDossier(dto.getTextOfDossier());
            CriminalGroup criminalGroupFromDb = criminalGroupRepository
                    .getCriminalGroupByCriminalGroupNameAndCriminalGroupType(updatedCriminal.getCriminalGroup().getCriminalGroupName(), updatedCriminal.getCriminalGroup().getCriminalGroupType());
            existingCriminal.setCriminalGroup(Objects.requireNonNullElseGet(criminalGroupFromDb, () -> criminalGroupRepository.save(updatedCriminal.getCriminalGroup())));

            if (updatedImageFile != null && !updatedImageFile.isEmpty()) {
                try {
                    Image updatedImage = toImageEntity(updatedImageFile);
                    existingCriminal.setImage(updatedImage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            existingCriminal.setCriminalGroup(criminalGroupFromDb);
            existingCriminal.getCriminalRecords().clear();
            existingCriminal.getLanguages().clear();
            splitStringToLanguages(dto.getSelectedLanguages(),existingCriminal);
            splitStringToCriminalRecords(dto.getSelectedCriminalRecords(),existingCriminal);

            atomicReference.set(Optional.of(criminalRepository.save(existingCriminal)));
        }, () -> atomicReference.set(Optional.empty()));

        atomicReference.get();
    }

    @Override
    @Transactional
    public void addCriminal(CriminalDTO dto,MultipartFile file) {
        Image image;
        try {
            image = toImageEntity(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Criminal criminal = mapper.criminaDtoToCriminal(dto);
        criminal.addImageToCriminal(image);
        criminal.addDossierToCriminal(criminal.getDossier());
        CriminalGroup criminalGroupFromDb = criminalGroupRepository
                .getCriminalGroupByCriminalGroupNameAndCriminalGroupType
                        (criminal.getCriminalGroup().getCriminalGroupName(),criminal.getCriminalGroup().getCriminalGroupType());
        if (criminalGroupFromDb == null) {
            criminalGroupFromDb = criminal.getCriminalGroup();
            criminalGroupRepository.save(criminal.getCriminalGroup());
        }
        criminal.setCriminalGroup(criminalGroupFromDb);
        criminal.getCriminalRecords().clear();
        criminal.getLanguages().clear();
        splitStringToLanguages(dto.getSelectedLanguages(),criminal);
        splitStringToCriminalRecords(dto.getSelectedCriminalRecords(),criminal);
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

    @Override
    public List<Criminal> findCriminalsByInputParams(SortDTO sortDTO) {
        QCriminal criminal = QCriminal.criminal;
        BooleanBuilder predicate = new BooleanBuilder();
        if (sortDTO.getInputCriminalFirstName() != null && !sortDTO.getInputCriminalFirstName().isBlank()) {
            predicate.and(criminal.firstName.containsIgnoreCase(sortDTO.getInputCriminalFirstName()));
        }
        if (sortDTO.getArrestedCriminals() != null &&!sortDTO.getArrestedCriminals().isBlank()) {
            switch (sortDTO.getArrestedCriminals()) {
                case "notArrested" -> predicate.and(criminal.dateOfArrest.isNull());
                case "onlyArrested" -> predicate.and(criminal.dateOfArrest.isNotNull());
            }
        }
        if (sortDTO.getInputCriminalLastName() != null && !sortDTO.getInputCriminalLastName().isBlank()) {
            predicate.and(criminal.lastName.containsIgnoreCase(sortDTO.getInputCriminalLastName()));
        }
        if (sortDTO.getInputCriminalNickName() != null && !sortDTO.getInputCriminalNickName().isBlank()) {
            predicate.and(criminal.nickName.containsIgnoreCase(sortDTO.getInputCriminalNickName()));
        }
        if (sortDTO.getCriminalRecord() != null && !sortDTO.getCriminalRecord().isBlank()) {
            predicate.and(criminal.criminalRecords.any().criminalRecord.equalsIgnoreCase(sortDTO.getCriminalRecord()));
        }
        if (sortDTO.getTextInDossier() != null && !sortDTO.getTextInDossier().isBlank()) {
            predicate.and(criminal.dossier.textOfDossier.contains(sortDTO.getTextInDossier()));
        }
        if (CriminalGroupType.isValidValue(sortDTO.getCriminalGroupType())) {
            predicate.and(criminal.criminalGroup.criminalGroupType.eq(CriminalGroupType.valueOf(sortDTO.getCriminalGroupType())));
        }
        if (EyeColor.isValidValue(sortDTO.getEyeColor())) {
            predicate.and(criminal.eyeColor.eq(EyeColor.valueOf(sortDTO.getEyeColor())));
        }
        if (HairColor.isValidValue(sortDTO.getHairColor())) {
            predicate.and(criminal.hairColor.eq(HairColor.valueOf(sortDTO.getHairColor())));
        }
        Sort.Direction direction = sortDTO.isOrder() ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortBy = sortDTO.getSortBy();
        if (sortBy == null) {
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
        Criminal criminal = criminalRepository.findById(id).orElseThrow(NotFoundException::new);
        criminal.setDateOfArrest(LocalDate.now());
        criminalRepository.save(criminal);
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
