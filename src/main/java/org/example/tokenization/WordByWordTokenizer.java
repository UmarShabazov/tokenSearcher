package org.example.tokenization;

public class WordByWordTokenizer implements Tokenizer {

    private final String delimiter;

    public WordByWordTokenizer(String delimiter) {
        this.delimiter = delimiter != null ? delimiter : "\\P{IsAlphabetic}+";
    }

    public WordByWordTokenizer() {
        this("\\P{IsAlphabetic}+");
    }

    @Override
    public String[] tokenize(String text) {

        if (text == null || text.isEmpty()) {
            return new String[0];
        }
        return text.toLowerCase().split(delimiter);
    }
}
