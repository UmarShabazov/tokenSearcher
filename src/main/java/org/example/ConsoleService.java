package org.example;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleService {

    private static final Logger logger = Logger.getLogger(ConsoleService.class.getName());
    private final Scanner scanner;
    private final IndexService indexService;

    public ConsoleService(IndexService indexService) {
        this.scanner = new Scanner(System.in);
        this.indexService = indexService;
    }

    public void start() {
        System.out.println("Добро пожаловать в систему индексирования файлов Умара Шабазова");

        while (true) {
            System.out.println("\nВыберите команду:");
            System.out.println("1. Добавить файл или директорию для индексации");
            System.out.println("2. Поиск файлов по слову");
            System.out.println("3. Выйти");
            System.out.print("Ваш выбор: ");


            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: необходимо ввести одну цифру. Пожалуйста, попробуйте снова.");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> addPath();
                    case 2 -> search();
                    case 3 -> {
                        System.out.println("Выход из программы.");
                        return;
                    }
                    default -> System.out.println("Неверная команда. Пожалуйста, выберите снова.");
                }
            } catch (IOException e) {
                System.out.println("Ошибка при обработке файлов: " + e.getMessage());
                logger.log(Level.SEVERE, "Ошибка при обработке файлов", e);
            }
        }
    }

    private void addPath() throws IOException {
        System.out.print("Введите путь к файлу или директории: ");
        String path = scanner.nextLine();

        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Указанный путь не существует. Пожалуйста, проверьте и попробуйте снова.");
            return;
        }

        indexService.addPath(path);
        System.out.println("Индексирование завершено.");
        logger.log(Level.INFO, "Путь {0} успешно добавлен в индекс", path);
        indexService.printIndex();
    }

    private void search() {
        System.out.print("Введите слово для поиска: ");
        String query = scanner.nextLine();
        Set<File> results = indexService.search(query);

        if (results.isEmpty()) {
            System.out.println("Файлы не найдены.");
            logger.log(Level.INFO, "По запросу \"{0}\" файлы не найдены", query);
        } else {
            System.out.println("Найдены файлы:");
            results.forEach(file -> System.out.println(file.getAbsolutePath()));
            logger.log(Level.INFO, "По запросу \"{0}\" найдено {1} файлов", new Object[]{query, results.size()});
        }
    }


}