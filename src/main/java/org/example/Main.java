package org.example;


import org.example.tokenization.Tokenizer;
import org.example.tokenization.TrigramTokenizer;

public class Main {
    public static void main(String[] args) {
        Tokenizer tokenizer = new TrigramTokenizer();
        IndexService indexService = new IndexService(tokenizer);
        ConsoleService consoleService = new ConsoleService(indexService);

        consoleService.start();
    }
}