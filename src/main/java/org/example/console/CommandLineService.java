package org.example.console;

import org.example.indexation.Indexable;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class CommandLineService implements ApplicationMode {
    private final Indexable indexable;

    public CommandLineService(Indexable indexable) {
        this.indexable = indexable;
    }

    @Override
    public void run(String[] args) throws IOException {
        String path = null;
        String searchQuery = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--files":
                    if (i + 1 < args.length) {
                        path = args[++i];
                    } else {
                        System.out.println("Invalid path specified for --files");
                    }
                    break;
                case "--search":
                    if (i + 1 < args.length) {
                        searchQuery = args[++i];
                    } else {
                        System.out.println("Invalid search query specified for --search");
                    }
                    break;
            }
        }

        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                indexable.addPath(path);
                System.out.println("Indexing completed.");
                indexable.printIndex();
            } else {
                System.out.println("The specified path does not exist.");
            }
        }

        if (searchQuery != null) {
            Set<File> results = indexable.search(searchQuery);
            if (results.isEmpty()) {
                System.out.println("Files not found for query: " + searchQuery);
            } else {
                System.out.println("Files found:");
                results.forEach(f -> System.out.println(f.getAbsolutePath()));
            }
        }
    }
}
