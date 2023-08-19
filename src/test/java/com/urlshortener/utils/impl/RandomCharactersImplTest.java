package com.urlshortener.utils.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;


class RandomCharactersImplTest {
    private final RandomCharactersImpl randomCharacters;

    public RandomCharactersImplTest() {
        this.randomCharacters = new RandomCharactersImpl();
    }

    @Test
    void generate_ShouldReturnAStringWithSize5() {
        int size = 5;
        String value = randomCharacters.generate(size);
        assertNotNull(value);
        assertEquals(value.getClass(), String.class);
        assertEquals(value.length(), size);
    }
}
