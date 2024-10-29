import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainIntegrationTest {


    @Test
    public void testCommandLineMode() throws Exception {

        Path tempDir = Files.createTempDirectory("testDir");
        Path tempFile = Files.createFile(tempDir.resolve("testFile.txt"));
        Files.writeString(tempFile, "sample text for indexing, RIP XXXTentacion");


        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar", "target/artifacts/token_search_jar/token-search.jar", "--files", tempDir.toString(), "--search", "rip"
        );
        processBuilder.directory(new File(System.getProperty("user.dir")));
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        boolean foundResult = false;
        while ((line = reader.readLine()) != null) {
            if (line.contains("Files found") || line.contains(tempFile.toString())) {
                foundResult = true;
            }
        }

        process.waitFor();

        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(tempDir);

        assertTrue(foundResult, "File was not found in the search result");
    }
}
