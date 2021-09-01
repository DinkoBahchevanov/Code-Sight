package com.codesight.codesight_api.infrastructure.custom_annotations;

import org.passay.CharacterData;

public class SpecialCharacterRule implements CharacterData{

    @Override
    public String getErrorCode() {
        return "Password must contain 1 or more characters from the group: ! @ # $ % ^ & * ?";
    }

    @Override
    public String getCharacters() {
        return "!@#$%^&*?";
    }
}
