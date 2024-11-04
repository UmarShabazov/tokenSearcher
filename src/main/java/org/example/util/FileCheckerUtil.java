package org.example.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileCheckerUtil {

    public static boolean isTextFile(File file) throws IOException {

        if (isExcludedExtension(file)) {
            return false;
        }

        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".txt")) {
            return true;
        }

        try (var inputStream = Files.newInputStream(file.toPath())) {
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

            for (int i = 0; i < bytesRead; i++) {
                byte b = buffer[i];

                if (b == 0) {
                    return false;
                }

                if ((b & 0x80) == 0) {
                    continue;
                }

                if (!isValidUtf8Sequence(buffer, i, bytesRead)) {
                    return false;
                }


                int sequenceLength = (b & 0xE0) == 0xC0 ? 2 :
                        (b & 0xF0) == 0xE0 ? 3 : 4;
                i += sequenceLength - 1;
            }
        }
        return true;
    }

    private static boolean isValidUtf8Sequence(byte[] buffer, int startIndex, int bytesRead) {

        int expectedLength;
        byte firstByte = buffer[startIndex];

        if ((firstByte & 0xE0) == 0xC0) expectedLength = 2;
        else if ((firstByte & 0xF0) == 0xE0) expectedLength = 3;
        else if ((firstByte & 0xF8) == 0xF0) expectedLength = 4;
        else return false;


        if (startIndex + expectedLength > bytesRead) return false;

        for (int i = 1; i < expectedLength; i++) {
            byte nextByte = buffer[startIndex + i];
            if ((nextByte & 0xC0) != 0x80) return false;
        }
        return true;
    }

    public static boolean isExcludedExtension(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".url") || fileName.endsWith(".lnk") || fileName.endsWith(".sys") ||
                fileName.endsWith(".exe") || fileName.endsWith(".dll") || fileName.endsWith(".bin");
    }
}
