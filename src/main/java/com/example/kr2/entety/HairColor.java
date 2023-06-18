package com.example.kr2.entety;

public enum HairColor {
    BLONDE,
    BROWN,
    BLACK,
    RED,
    BALD,
    UNKNOWN;

    public static boolean isValidValue(String value) {
        for (HairColor hairColor : HairColor.values()) {
            if (hairColor.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
