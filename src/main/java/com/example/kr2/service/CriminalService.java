package com.example.kr2.service;

import com.example.kr2.dto.CriminalDTO;
import com.example.kr2.dto.SortDTO;
import com.example.kr2.entety.Criminal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CriminalService {
    Optional<CriminalDTO> getCriminalById(Long id);

    void updateCriminalById(Long id, CriminalDTO criminalDto,MultipartFile file);

    void addCriminal(CriminalDTO dto, MultipartFile file);

    List<Criminal> findCriminalsByInputParams(SortDTO sortDTO);
    boolean deleteCriminal(Long id);

    void arrestCriminal(Long id);
}
