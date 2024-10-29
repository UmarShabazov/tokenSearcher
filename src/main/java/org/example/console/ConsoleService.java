package org.example.console;

import org.example.indexation.Indexable;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleService implements ApplicationMode {

    private static final Logger logger = Logger.getLogger(ConsoleService.class.getName());
    private final Scanner scanner;
    private final Indexable indexable;

    public ConsoleService(Indexable indexable) {
        this.scanner = new Scanner(System.in);
        this.indexable = indexable;
    }

    @Override
    public void run(String[] args) {
        start();
    }

    public void start() {
        System.out.println("Welcome to Umar Shabazov's file indexing system");

        while (true) {
            System.out.println("\nChoose a command:");
            System.out.println("1. Add a file or directory for indexing");
            System.out.println("2. Search files by words");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a single digit. Try again.");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> addPath();
                    case 2 -> search();
                    case 3 -> {
                        System.out.println("Exiting the program.");
                        return;
                    }
                    default -> System.out.println("Invalid command. Please choose again.");
                }
            } catch (IOException e) {
                System.out.println("File processing error: " + e.getMessage());
                logger.log(Level.SEVERE, "File processing error", e);
            }
        }
    }

    private void addPath() throws IOException {
        System.out.print("Enter the path to the file or directory: ");
        String path = scanner.nextLine();

        File file = new File(path);
        if (!file.exists()) {
            System.out.println("The specified path does not exist. Please check and try again.");
            return;
        }

        indexable.addPath(path);
        System.out.println("Indexing completed.");
        logger.log(Level.INFO, "Path {0} successfully added to the index", path);
        indexable.printIndex();
    }

    private void search() {
        System.out.print("Enter a word to search: ");
        String query = scanner.nextLine();
        Set<File> results = indexable.search(query);

        if (results.isEmpty()) {
            System.out.println("No files found.");
            logger.log(Level.INFO, "No files found for query \"{0}\"", query);
        } else {
            System.out.println("Files found:");
            results.forEach(file -> System.out.println(file.getAbsolutePath()));
            logger.log(Level.INFO, "{1} files found for query \"{0}\"", new Object[]{query, results.size()});
        }
    }
}
