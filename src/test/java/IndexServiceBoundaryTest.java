import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.indexation.IndexService;
import org.example.indexation.Indexer;
import org.example.indexation.InMemoryIndexer;
import org.example.tokenization.WordByWordTokenizer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class IndexServiceBoundaryTest {

    private IndexService indexService;

    @BeforeEach
    public void setup() {
        Indexer indexer = new InMemoryIndexer();
        WordByWordTokenizer tokenizer = new WordByWordTokenizer();
        indexService = new IndexService(tokenizer, indexer);
    }

    @Test
    public void testVeryLongTextFile() throws Exception {
        Path tempFile = Files.createTempFile("longTextFile", ".txt");

        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longText.append("longword ");
        }
        Files.writeString(tempFile, longText.toString());

        indexService.addPath(tempFile.toString());

        Set<File> results = indexService.search("longword");
        assertTrue(results.contains(tempFile.toFile()), "Long text should be indexed correctly");

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testFileNamesWithSpacesAndSpecialCharacters() throws Exception {
        Path specialFile = Files.createTempFile("file with spaces @#$%", ".txt");
        Files.writeString(specialFile, "special content");

        indexService.addPath(specialFile.toString());

        Set<File> results = indexService.search("special");
        assertTrue(results.contains(specialFile.toFile()), "File with spaces and special characters in the name should be indexed correctly");

        Files.deleteIfExists(specialFile);
    }
}
