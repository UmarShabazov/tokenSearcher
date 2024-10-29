package org.example;

import org.example.console.ApplicationMode;
import org.example.console.CommandLineService;
import org.example.console.ConsoleService;
import org.example.indexation.IndexService;
import org.example.indexation.InMemoryIndexer;
import org.example.indexation.Indexable;
import org.example.indexation.Indexer;
import org.example.tokenization.Tokenizer;
import org.example.tokenization.TrigramTokenizer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Tokenizer tokenizer = new TrigramTokenizer();
        Indexer indexer = new InMemoryIndexer();
        Indexable indexable = new IndexService(tokenizer, indexer);

        ApplicationMode mode;

        if (args.length > 0) {
            mode = new CommandLineService(indexable);
        } else {

            mode = new ConsoleService(indexable);
        }

        mode.run(args);
    }
}
