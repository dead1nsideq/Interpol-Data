package com.example.kr2.mapper;

import com.example.kr2.dto.CriminalDTO;
import com.example.kr2.entety.Criminal;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

public interface Mapper {
    Criminal criminaDtoToCriminal(CriminalDTO dto);

    CriminalDTO criminaToCriminalDto(Criminal criminal);
}
