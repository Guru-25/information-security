import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DictionaryAttack {

    // Method to hash a password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to create a dictionary of hashed passwords
    public static Map<String, String> createDictionary(char[] charSet, int maxSize) {
        Map<String, String> wordDict = new HashMap<>();
        createDictionaryRecursive(charSet, "", maxSize, wordDict);
        return wordDict;
    }

    // Recursive helper method to create a dictionary
    private static void createDictionaryRecursive(char[] charSet, String prefix, int maxSize, Map<String, String> wordDict) {
        if (prefix.length() == maxSize) {
            wordDict.put(prefix, hashPassword(prefix));
            return;
        }
        for (char c : charSet) {
            createDictionaryRecursive(charSet, prefix + c, maxSize, wordDict);
        }
    }

    // Method to perform dictionary attack
    public static String launchDictAttack(Map<String, String> wordDict, Map<String, String> passwordTable) {
        String username = "user";
        String hashedPassword = passwordTable.get(username);
        for (Map.Entry<String, String> entry : wordDict.entrySet()) {
            if (entry.getValue().equals(hashedPassword)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // Method to generate a random PIN
    private static String generateRandomPin(char[] curSet, int pinSize) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pinSize; i++) {
            int randomIndex = (int) (Math.random() * curSet.length);
            sb.append(curSet[randomIndex]);
        }
        return sb.toString();
    }

    // Method to create a password table
    public static void createPasswordTable(Map<String, String> passwordTable) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter username (or type 'done' to finish): ");
            String username = scanner.nextLine();
            if (username.equals("done")) {
                break;
            }
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            passwordTable.put(username, hashPassword(password));
        }
    }

    // Method to display the password table
    public static void displayPasswordTable(Map<String, String> passwordTable) {
        for (Map.Entry<String, String> entry : passwordTable.entrySet()) {
            System.out.println("Username: " + entry.getKey() + ", Hashed Password: " + entry.getValue());
        }
    }

    // Method to perform a dictionary attack on a specific username
    public static void dictionaryAttack(String username, Map<String, String> passwordTable, Map<String, String> wordDict) {
        if (!passwordTable.containsKey(username)) {
            System.out.println("Username not found.");
            return;
        }

        String hashedPassword = passwordTable.get(username);
        for (Map.Entry<String, String> entry : wordDict.entrySet()) {
            if (entry.getValue().equals(hashedPassword)) {
                System.out.println("Password found: " + entry.getKey());
                return;
            }
        }
        System.out.println("Password not found.");
    }

    // Method to export timing results to a CSV file
    private static void exportToCSV(String filename, long[] times, int[] lengths) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("Password Length,Average Time (ms)\n");
            for (int i = 0; i < times.length; i++) {
                writer.append(lengths[i] + "," + times[i] + "\n");
            }
            System.out.println("Timing results exported to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to test password length vs time and export to CSV
    public static void testPasswordLengthVsTime(String charset) {
        int maxLength = 5; // You can adjust this max length as needed
        int[] lengths = new int[maxLength];
        long[] timeTaken = new long[maxLength];

        char[] curSet = charset.toCharArray();

        for (int length = 1; length <= maxLength; length++) {
            Map<String, String> wordDict = createDictionary(curSet, length);
            String randomPin = generateRandomPin(curSet, length);
            Map<String, String> passwordTable = new HashMap<>();
            passwordTable.put("user", hashPassword(randomPin));

            long startTime = System.nanoTime();
            launchDictAttack(wordDict, passwordTable);
            long endTime = System.nanoTime();

            lengths[length - 1] = length;
            timeTaken[length - 1] = (endTime - startTime) / 1000000;  // Convert to milliseconds
        }

        // Export timing results to a CSV file
        exportToCSV("password_length_vs_time.csv", timeTaken, lengths);
    }

    // Main method to handle user input and menu options
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String charSet;
        int length;
        Map<String, String> wordDict = new HashMap<>();
        Map<String, String> passwordTable = new HashMap<>();

        System.out.print("Enter character set (e.g., abcdefghijklmnopqrstuvwxyz): ");
        charSet = scanner.nextLine();
        System.out.print("Enter password length (e.g., 5): ");
        length = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Create dictionary of all possible combinations and their hashes");
            System.out.println("2. Create password table");
            System.out.println("3. Display password table");
            System.out.println("4. Launch dictionary attack");
            System.out.println("5. Test Password Length vs Time and Export to CSV");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    wordDict = createDictionary(charSet.toCharArray(), length);
                    System.out.println("Dictionary created.");
                    break;
                case "2":
                    createPasswordTable(passwordTable);
                    System.out.println("Password table created.");
                    break;
                case "3":
                    if (!passwordTable.isEmpty()) {
                        displayPasswordTable(passwordTable);
                    } else {
                        System.out.println("Password table not created yet.");
                    }
                    break;
                case "4":
                    System.out.print("Enter username to attack: ");
                    String username = scanner.nextLine();
                    if (!passwordTable.isEmpty() && !wordDict.isEmpty()) {
                        dictionaryAttack(username, passwordTable, wordDict);
                    } else {
                        System.out.println("Dictionary or password table not created yet.");
                    }
                    break;
                case "5":
                    testPasswordLengthVsTime(charSet);
                    break;
                case "6":
                    System.out.println("Exiting.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}