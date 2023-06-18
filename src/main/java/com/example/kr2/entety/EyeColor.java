package com.example.kr2.entety;


public enum EyeColor {
    BLUE,
    GREEN,
    BROWN,
    HAZEL,
    UNKNOWN;

    public static boolean isValidValue(String value) {
        for (EyeColor eyeColor : EyeColor.values()) {
            if (eyeColor.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
