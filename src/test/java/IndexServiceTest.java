import org.example.indexation.InMemoryIndexer;
import org.example.indexation.IndexService;
import org.example.indexation.Indexer;
import org.example.tokenization.Tokenizer;
import org.example.tokenization.WordByWordTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class IndexServiceTest {
    private IndexService indexService;

    @BeforeEach
    public void setup() {
        Tokenizer tokenizer = new WordByWordTokenizer();
        Indexer indexer = new InMemoryIndexer();
        indexService = new IndexService(tokenizer, indexer);
    }
    @Test
    public void testIndexSingleFile() throws Exception {

        Path tempFile = Files.createTempFile("testFile", ".txt");
        Files.writeString(tempFile, "sample text for testing");

        indexService.addPath(tempFile.toString());

        Set<File> results = indexService.search("sample");
        assertTrue(results.contains(tempFile.toFile()), "The file should be found for the query 'sample'");

        Files.deleteIfExists(tempFile);
    }
    @Test
    public void testIndexMultipleFiles() throws Exception {
        Path tempFile1 = Files.createTempFile("testFile1", ".txt");
        Files.writeString(tempFile1, "sample text in first file");

        Path tempFile2 = Files.createTempFile("testFile2", ".txt");
        Files.writeString(tempFile2, "another text in second file");

        indexService.addPath(tempFile1.toString());
        indexService.addPath(tempFile2.toString());

        Set<File> results1 = indexService.search("sample");
        Set<File> results2 = indexService.search("another");

        assertTrue(results1.contains(tempFile1.toFile()), "File1 should be found for the query 'sample'");
        assertTrue(results2.contains(tempFile2.toFile()), "File2 should be found for the query 'another'");

        Files.deleteIfExists(tempFile1);
        Files.deleteIfExists(tempFile2);
    }

    @Test
    public void testSearchWithMultipleTokens() throws Exception {

        Path tempFile = Files.createTempFile("testFile", ".txt");
        Files.writeString(tempFile, "sample text with multiple words");

        indexService.addPath(tempFile.toString());

        Set<File> results = indexService.search("sample text");
        assertTrue(results.contains(tempFile.toFile()), "The file should be found for the query 'sample text'");

        Set<File> partialResults = indexService.search("sample nonexistent");
        assertFalse(partialResults.contains(tempFile.toFile()), "The file should not be found for the query 'sample nonexistent'");

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testNoMatches() throws Exception {

        Path tempFile = Files.createTempFile("testFile", ".txt");
        Files.writeString(tempFile, "unrelated content");

        indexService.addPath(tempFile.toString());

        Set<File> results = indexService.search("nonexistent");
        assertTrue(results.isEmpty(), "Search results should be empty for the query 'nonexistent'");

        Files.deleteIfExists(tempFile);
    }


    @Test
    public void testEmptyDirectory() throws Exception {
        Path tempDir = Files.createTempDirectory("emptyTestDir");

        indexService.addPath(tempDir.toString());

        Set<File> results = indexService.search("anyWord");
        assertTrue(results.isEmpty(), "Index should be empty for an empty directory");

        Files.deleteIfExists(tempDir);
    }

    @Test
    public void testFileWithoutTokens() throws Exception {
        Path tempFile = Files.createTempFile("noTokensFile", ".txt");
        Files.writeString(tempFile, "#@@@");

        indexService.addPath(tempFile.toString());

        Set<File> results = indexService.search("anyWord");
        assertTrue(results.isEmpty(), "A file without tokens should not be added to the index");

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testBinaryFileIgnored() throws Exception {

        Path binaryFile = Files.createTempFile("binaryFile", ".bin");
        Files.write(binaryFile, new byte[]{0x00, 0x01, 0x02, 0x03});

        indexService.addPath(binaryFile.toString());

        Set<File> results = indexService.search("sample");
        assertTrue(results.isEmpty(), "Binary files should not be added to the index");

        Files.deleteIfExists(binaryFile);
    }
}
