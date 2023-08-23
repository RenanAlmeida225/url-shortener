package com.urlshortener.utils.impl;

import com.urlshortener.utils.RandomCharacters;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCharactersImpl implements RandomCharacters {

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    @Override
    public String generate(int size) {
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < size; i++) {
            Random random = new Random();
            int randomNumber = random.nextInt(characters.length());
            String randomCharacter = String.valueOf(characters.charAt(randomNumber));
            randomString.append(randomCharacter);
        }
        return randomString.toString();
    }
}
