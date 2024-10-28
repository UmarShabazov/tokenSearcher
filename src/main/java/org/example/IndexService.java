package org.example;

import org.example.tokenization.Tokenizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IndexService {

    private static final Logger logger = Logger.getLogger(IndexService.class.getName());
    private final Tokenizer tokenizer;
    private final Map<String, Set<File>> index = new HashMap<>();

    public IndexService(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
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
        if (file.isFile() && file.getName().endsWith(".txt")) {
            String content = Files.readString(file.toPath());
            String[] tokens = tokenizer.tokenize(content);

            if (tokens.length == 0) {
                logger.log(Level.WARNING, "Файл пуст или не содержит токенов: {0}", file.getName());
                return;
            }

            logger.log(Level.INFO, "Индексируем файл: {0}", file.getName());

            for (String token : tokens) {
                index.computeIfAbsent(token, k -> new HashSet<>()).add(file);
                logger.log(Level.FINE, "Добавлен токен: {0} для файла: {1}", new Object[]{token, file.getName()});
            }
        }
    }

    public Set<File> search(String query) {
        Set<File> results = new HashSet<>();
        String[] queryTokens = tokenizer.tokenize(query);

        logger.log(Level.INFO, "Поиск по запросу: {0}", query);

        for (String token : queryTokens) {
            Set<File> filesContainingToken = index.getOrDefault(token, Collections.emptySet());

            if (filesContainingToken.isEmpty()) {
                logger.log(Level.INFO, "Токен \"{0}\" отсутствует в индексе. Возвращаем пустое множество.", token);
                return Collections.emptySet();
            }

            if (results.isEmpty()) {
                results.addAll(filesContainingToken);
            } else {
                results.retainAll(filesContainingToken);
            }
        }

        logger.log(Level.INFO, "Найдено {0} файлов по запросу \"{1}\"", new Object[]{results.size(), query});
        return results;
    }

    public void printIndex() {
        index.forEach((token, files) -> {
            System.out.println("Токен: " + token + " встречается в файлах: ");
            files.forEach(file -> System.out.println("  - " + file.getAbsolutePath()));
        });
    }

}