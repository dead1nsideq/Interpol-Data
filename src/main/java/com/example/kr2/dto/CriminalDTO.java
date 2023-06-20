package com.example.kr2.dto;

import com.example.kr2.entety.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CriminalDTO {

    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 25)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max = 25)
    private String lastName;

    @NotNull
    @NotBlank
    @Size(max = 25)
    private String nickName;

    @NotNull
    @Min(value = 130)
    @Max(value = 220)
    private Integer height;

    @NotNull
    private HairColor hairColor;

    @NotNull
    private EyeColor eyeColor;

    @NotNull
    @NotBlank
    private String criminalGroupName;
    @NotNull
    private CriminalGroupType criminalGroupType;


    private LocalDate dateOfArrest;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    @NotBlank
    @Size(max = 35)
    private String country;

    @NotNull
    @NotBlank
    private String selectedLanguages;
    @NotNull
    @NotBlank
    private String selectedCriminalRecords;
    @NotNull
    @NotBlank
    private String textOfDossier;

    private Long imageId;
}
