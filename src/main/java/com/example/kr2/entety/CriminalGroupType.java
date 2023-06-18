package com.example.kr2.entety;

public enum CriminalGroupType {
    Mafia,
    Cartel,
    Gang,
    Syndicate,
    Triad,
    Yakuza,
    Familia,
    Organisation;

    public static boolean isValidValue(String value) {
        for (CriminalGroupType criminalGroupType: CriminalGroupType.values()) {
            if (criminalGroupType.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}

