package org.example.tokenization;

public class WordByWordTokenizer implements Tokenizer {

    @Override
    public String[] tokenize(String text) {

        return text.toLowerCase().split("\\P{IsAlphabetic}+");

    }

}