package com.example.kr2.dto;


import lombok.Data;

@Data
public class SortDTO {
    private String inputCriminalFirstName;
    private String inputCriminalLastName;
    private String inputCriminalNickName;
    private String arrestedCriminals;
    private String criminalRecord;
    private String textInDossier;
    private String criminalGroupType;
    private String eyeColor;
    private String hairColor;
    private String sortBy;

    private boolean order;
}
