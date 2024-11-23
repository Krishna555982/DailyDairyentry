import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class DiaryEntry {
    private LocalDate date;
    private String content;

    public DiaryEntry(LocalDate date, String content) {
        this.date = date;
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE) + ": " + content;
    }
}

public class DailyDiary {
    private static final String DIARY_FILE = "diary_entries.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addEntry();
                    break;
                case 2:
                    viewEntries();
                    break;
                case 3:
                    searchEntries();
                    break;
                case 4:
                    System.out.println("Exiting the diary. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n--- Daily Diary ---");
        System.out.println("1. Add New Entry");
        System.out.println("2. View All Entries");
        System.out.println("3. Search Entries");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void addEntry() {
        System.out.print("Enter your diary entry: ");
        String content = scanner.nextLine();

        DiaryEntry entry = new DiaryEntry(LocalDate.now(), content);
        saveEntry(entry);
        System.out.println("Entry added successfully!");
    }

    private static void saveEntry(DiaryEntry entry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DIARY_FILE, true))) {
            writer.write(entry.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving entry: " + e.getMessage());
        }
    }

    private static void viewEntries() {
        List<DiaryEntry> entries = readEntries();
        if (entries.isEmpty()) {
            System.out.println("No entries found.");
            return;
        }

        for (DiaryEntry entry : entries) {
            System.out.println(entry);
        }
    }

    private static void searchEntries() {
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine().toLowerCase();

        List<DiaryEntry> entries = readEntries();
        boolean found = false;

        for (DiaryEntry entry : entries) {
            if (entry.getContent().toLowerCase().contains(keyword)) {
                System.out.println(entry);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No entries found matching the keyword.");
        }
    }

    private static List<DiaryEntry> readEntries() {
        List<DiaryEntry> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DIARY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ", 2);
                if (parts.length == 2) {
                    LocalDate date = LocalDate.parse(parts[0]);
                    entries.add(new DiaryEntry(date, parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading entries: " + e.getMessage());
        }
        return entries;
    }
}

