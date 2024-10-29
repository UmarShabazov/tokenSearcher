package org.example.indexation;

import org.example.tokenization.Tokenizer;
import org.example.util.FileCheckerUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class IndexService implements Indexable {

    private static final Logger logger = Logger.getLogger(IndexService.class.getName());
    private final Tokenizer tokenizer;
    private final Indexer indexer;

    public IndexService(Tokenizer tokenizer, Indexer indexer) {
        this.tokenizer = tokenizer;
        this.indexer = indexer;
    }

    public void addPath(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                addFile(f);
            }
        } else {
            addFile(file);
        }
    }

    private void addFile(File file) throws IOException {
        if (file.isFile() && FileCheckerUtil.isTextFile(file)) {
            String content = Files.readString(file.toPath());
            String[] tokens = tokenizer.tokenize(content);

            if (tokens.length == 0) {
                logger.log(Level.WARNING, "File is empty or contains no tokens: {0}", file.getName());
                return;
            }

            logger.log(Level.INFO, "Indexing file: {0}", file.getName());

            for (String token : tokens) {
                indexer.addToken(token, file);
                logger.log(Level.FINE, "Token added: {0} for file: {1}", new Object[]{token, file.getName()});
            }
        }
    }

    public Set<File> search(String query) {
        Set<File> results = new HashSet<>();
        String[] queryTokens = tokenizer.tokenize(query);

        for (String token : queryTokens) {
            Set<File> filesContainingToken = indexer.getFilesForToken(token);

            if (filesContainingToken.isEmpty()) {
                logger.log(Level.INFO, "Token \"{0}\" not found in index. Returning empty set.", token);
                return Collections.emptySet();
            }

            if (results.isEmpty()) {
                results.addAll(filesContainingToken);
            } else {
                results.retainAll(filesContainingToken);
            }
        }
        return results;
    }

    public void printIndex() {
        indexer.printIndex();
    }
}
