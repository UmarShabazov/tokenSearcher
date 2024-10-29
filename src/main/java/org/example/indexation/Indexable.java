package org.example.indexation;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface Indexable {

    void addPath(String path) throws IOException;
    Set<File> search(String query);
    void printIndex();
}
