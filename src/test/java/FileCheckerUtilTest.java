import org.example.util.FileCheckerUtil;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class FileCheckerUtilTest {

    @Test
    public void testIsTextFileForTxtExtension() throws Exception {
        Path tempFile = Files.createTempFile("testFile", ".txt");
        Files.writeString(tempFile, "This is a sample text file.");

        assertTrue(FileCheckerUtil.isTextFile(tempFile.toFile()), "Text file with .txt extension should be detected as text.");

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testIsTextFileWithBinaryContent() throws Exception {
        Path tempFile = Files.createTempFile("binaryFile", ".bin");
        byte[] binaryContent = {0x00, 0x01, 0x02, 0x03, 0x04};
        Files.write(tempFile, binaryContent);

        assertFalse(FileCheckerUtil.isTextFile(tempFile.toFile()), "Binary file should not be detected as text.");

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testExcludedExtensionDetection() {
        File exeFile = new File("testProgram.exe");
        File dllFile = new File("library.dll");

        assertTrue(FileCheckerUtil.isExcludedExtension(exeFile), "Files with .exe extension should be excluded.");
        assertTrue(FileCheckerUtil.isExcludedExtension(dllFile), "Files with .dll extension should be excluded.");
    }
}
