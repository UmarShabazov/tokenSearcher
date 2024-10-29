package org.example.indexation;

import java.io.File;
import java.util.*;

public class InMemoryIndexer implements Indexer {
    private final Map<String, Set<File>> index = new HashMap<>();

    public void addToken(String token, File file) {
        index.computeIfAbsent(token, k -> new HashSet<>()).add(file);
    }

    public Set<File> getFilesForToken(String token) {
        return index.getOrDefault(token, Collections.emptySet());
    }

    public void printIndex() {
        index.forEach((token, files) -> {
            System.out.println("Token: " + token + " appears in files: ");
            files.forEach(file -> System.out.println("  - " + file.getAbsolutePath()));
        });
    }
}
