package com.example.kr2.service;

import com.example.kr2.entety.Criminal;
import com.example.kr2.entety.CriminalGroup;
import com.example.kr2.entety.Dossier;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CriminalService {
    Optional<Criminal> getCriminalById(Long id);

    void updateCriminalById(Long id, Criminal criminal, Dossier textOfDossier, CriminalGroup criminalGroup, String selectedLanguages, String selectedCriminalRecords, MultipartFile file);

    void addCriminal(Criminal criminal, Dossier dossier, CriminalGroup criminalGroup, String criminalLanguage, String selectedCriminalRecords, MultipartFile file);

    List<Criminal> findCriminalsByInputParams(String inputCriminalFirstName,
                                              String inputCriminalLastName,
                                              String inputCriminalNickName,
                                              String arrestedCriminals,
                                              String criminalRecord,
                                              String textInDossier,
                                              String criminalGroupType,
                                              String eyeColor,
                                              String hairColor,
                                              String sortBy, boolean order);
    boolean deleteCriminal(Long id);

    void arrestCriminal(Long id);
}
