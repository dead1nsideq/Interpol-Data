package com.example.kr2.mapper;


import com.example.kr2.dto.CriminalDTO;
import com.example.kr2.entety.*;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CriminalMapper implements Mapper {
    public Criminal criminaDtoToCriminal(CriminalDTO dto) {
        Criminal criminal = new Criminal();
        criminal.setId(dto.getId());
        criminal.setFirstName(dto.getFirstName());
        criminal.setLastName(dto.getLastName());
        criminal.setNickName(dto.getNickName());
        criminal.setHeight(dto.getHeight());
        criminal.setHairColor(dto.getHairColor());
        criminal.setEyeColor(dto.getEyeColor());
        criminal.setDateOfArrest(dto.getDateOfArrest());
        criminal.setDateOfBirth(dto.getDateOfBirth());
        criminal.setCountry(dto.getCountry());

        CriminalGroup criminalGroup = new CriminalGroup();
        criminalGroup.setCriminalGroupType(dto.getCriminalGroupType());
        criminalGroup.setCriminalGroupName(dto.getCriminalGroupName());
        criminal.setCriminalGroup(criminalGroup);

        Set<Language> languages = new LinkedHashSet<>();
        String[] languageNames = dto.getSelectedLanguages().split(",");
        for (String languageName : languageNames) {
            Language language = new Language();
            language.setCriminalLanguage(languageName.trim());
            languages.add(language);
        }
        criminal.setLanguages(languages);

        Set<CriminalRecord> criminalRecords = new LinkedHashSet<>();
        String[] recordNames = dto.getSelectedCriminalRecords().split(",");
        for (String recordName : recordNames) {
            CriminalRecord record = new CriminalRecord();
            record.setCriminalRecord(recordName.trim());
            criminalRecords.add(record);
        }
        criminal.setCriminalRecords(criminalRecords);

        Dossier dossier = new Dossier();
        dossier.setTextOfDossier(dto.getTextOfDossier());
        criminal.setDossier(dossier);

        return criminal;
    }

    public CriminalDTO criminaToCriminalDto(Criminal criminal) {
        String languageNames = criminal
                .getLanguages()
                .stream()
                .map(Language::getCriminalLanguage)
                .collect(Collectors.joining(","));
        String recordNames = criminal
                .getCriminalRecords()
                .stream().map(CriminalRecord::getCriminalRecord)
                .collect(Collectors.joining(","));
        return CriminalDTO.builder()
                .id(criminal.getId())
                .firstName(criminal.getFirstName())
                .lastName(criminal.getLastName())
                .nickName(criminal.getNickName())
                .height(criminal.getHeight())
                .hairColor(criminal.getHairColor())
                .eyeColor(criminal.getEyeColor())
                .dateOfArrest(criminal.getDateOfArrest())
                .dateOfBirth(criminal.getDateOfBirth())
                .country(criminal.getCountry())
                .criminalGroupType(criminal.getCriminalGroup().getCriminalGroupType())
                .criminalGroupName(criminal.getCriminalGroup().getCriminalGroupName())
                .textOfDossier(criminal.getDossier().getTextOfDossier())
                .selectedLanguages(languageNames)
                .selectedCriminalRecords(recordNames)
                .imageId(criminal.getImage().getId())
                .build();
    }
}
