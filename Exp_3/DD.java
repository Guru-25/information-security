import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DD {
    private static final String CHAR_SET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static Map<String, String> dictionary = new HashMap<>();
    private static Map<String, String> passwordTable = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    // Menu-driven loop
    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\nDictionary Attack Lab");
            System.out.println("1. Create Dictionary");
            System.out.println("2. Generate Password Table");
            System.out.println("3. Display Password Table");
            System.out.println("4. Launch Dictionary Attack");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                createDictionary();
                break;
                case 2:
                generatePasswordTable();
                break;
                case 3:
                displayPasswordTable();
                break;
                case 4:
                launchDictionaryAttack();
                break;
                case 0:
                System.out.println("Exiting...");
                break;
                default:
                System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    // Generates words of lengths 2, 3, and 4.
    private static void createDictionary() {
        dictionary.clear();
        try (FileWriter writer = new FileWriter("dictionary_generation_times.txt")) {
            for (int length = 2; length <= 4; length++) {
                Instant start = Instant.now();
                generateWords("", length);
                Instant end = Instant.now();
                long timeElapsed = Duration.between(start, end).toMillis();
                writer.write(length + "," + timeElapsed + "\n");
                System.out.println("Length " + length + " words generated in " + timeElapsed + " ms");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
        System.out.println("Dictionary created with " + dictionary.size() + " words.");
    }

    // Recursive method to generate words
    private static void generateWords(String prefix, int length) {
        if (length == 0) {
            String hash = sha256(prefix);
            dictionary.put(hash, prefix);
            return;
        }
        for (int i = 0; i < CHAR_SET.length(); i++) {
            generateWords(prefix + CHAR_SET.charAt(i), length - 1);
        }
    }
    
    // Collects usernames and passwords, stores hashes
    private static void generatePasswordTable() {
        passwordTable.clear();
        for (int i = 1; i <= 10; i++) {
            System.out.print("Enter username " + i + ": ");
            String username = scanner.nextLine();
            System.out.print("Enter password for " + username + ": ");
            String password = scanner.nextLine();
            String hash = sha256(password);
            passwordTable.put(username, hash);
        }
        System.out.println("Password table generated for 10 users.");
    }

    // Displays the password table
    private static void displayPasswordTable() {
        System.out.println("\nPassword Table:");
        for (Map.Entry<String, String> entry : passwordTable.entrySet()) {
            System.out.println("Username: " + entry.getKey() + ", Password Hash: " + entry.getValue());
        }
    }

    // Performs the dictionary attack
    private static void launchDictionaryAttack() {
        System.out.print("Enter username to attack: ");
        String username = scanner.nextLine();
        String hash = passwordTable.get(username);
        if (hash == null) {
            System.out.println("Username not found in the password table.");
            return;
        }
        String password = dictionary.get(hash);
        if (password != null) {
            System.out.println("Password found for " + username + ": " + password);
        } else {
            System.out.println("Password not found in the dictionary.");
        }
    }

    // Computes SHA-256 hash of the input string
    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}