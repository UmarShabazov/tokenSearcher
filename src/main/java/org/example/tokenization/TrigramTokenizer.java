package org.example.tokenization;

public class TrigramTokenizer implements Tokenizer {

    @Override
    public String[] tokenize(String text) {
        text = text.toLowerCase().replaceAll("\\P{IsAlphabetic}+", "");
        int length = text.length();

        if (length < 3) {
            return new String[0];
        }

        String[] trigrams = new String[length - 2];
        for (int i = 0; i < length - 2; i++) {
            trigrams[i] = text.substring(i, i + 3);
        }

        return trigrams;
    }

}