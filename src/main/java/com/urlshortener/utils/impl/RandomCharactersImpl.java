package com.urlshortener.utils.impl;

import com.urlshortener.utils.RandomCharacters;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCharactersImpl implements RandomCharacters {

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private String randomString = "";

    @Override
    public String generate(int size) {
        if(size==0) return this.randomString;
        Random random = new Random();
        int randomNumber = random.nextInt(characters.length());
        String randomCharacter = String.valueOf(characters.charAt(randomNumber));
        this.randomString += randomCharacter;
        return generate(size-1);
    }
}
