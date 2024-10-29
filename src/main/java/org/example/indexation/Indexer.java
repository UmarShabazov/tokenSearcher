package org.example.indexation;

import java.io.File;
import java.util.Set;

public interface Indexer {
    void addToken(String token, File file);
    Set<File> getFilesForToken(String token);
    void printIndex();
}
