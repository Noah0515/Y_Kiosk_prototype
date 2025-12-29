package com.example.y_kiosk_prototype.generator;

import java.util.Random;
import java.util.stream.Collectors;

public class IdGenerator {
    public String generateStringId(int length) {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        String result = new Random().ints(60, 0, characters.length())
                .mapToObj(characters::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        return result;
    }

    public int generateIntId(int range) {
        return new Random().nextInt(range);
    }
}
