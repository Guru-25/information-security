import java.util.Scanner;

public class AESKeyGeneration {
    // S-box for SubBytes operation
    public static final int[][] sbox = {
        {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
        {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
        {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
        {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
        {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
        {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
        {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
        {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
        {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
        {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
        {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
        {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
        {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
        {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
        {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
        {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
    };
    
    // Round constants for key expansion
    private static final int[] rcon = {
        0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36,
        0x6C, 0xD8, 0xAB, 0x4D  // Extended for AES-256
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[][] initialKey = null;
        int rounds = 0;
        
        // Main menu loop
        while (true) {
            System.out.println("\nAES Key Generation Menu:");
            System.out.println("1. Enter initial key");
            System.out.println("2. Generate keys for 10 rounds (AES-128)");
            System.out.println("3. Generate keys for 12 rounds (AES-192)");
            System.out.println("4. Generate keys for 14 rounds (AES-256)");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    initialKey = getInitialKeyFromUser(scanner);
                    break;
                case 2:
                    rounds = 10;
                    generateAndPrintKeys(initialKey, rounds);
                    break;
                case 3:
                    rounds = 12;
                    generateAndPrintKeys(initialKey, rounds);
                    break;
                case 4:
                    rounds = 14;
                    generateAndPrintKeys(initialKey, rounds);
                    break;
                case 5:
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to get the initial key from the user
    private static int[][] getInitialKeyFromUser(Scanner scanner) {
        int[][] key = new int[4][4];
        System.out.println("Enter the initial key (16 hexadecimal values):");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.printf("Enter value for position [%d][%d]: ", i, j);
                String hexValue = scanner.next();
                key[i][j] = Integer.parseInt(hexValue, 16);
            }
        }
        System.out.println("Initial key entered successfully.");
        return key;
    }

    // Method to generate and print keys for the specified number of rounds
    private static void generateAndPrintKeys(int[][] initialKey, int rounds) {
        if (initialKey == null) {
            System.out.println("Please enter the initial key first.");
            return;
        }
        int[][][] expandedKeys = generateKey(initialKey, rounds);
        for (int round = 0; round <= rounds; round++) {
            System.out.println("Round " + round + " Key:");
            printKey(expandedKeys[round]);
            System.out.println();
        }
    }

    // Main key expansion algorithm
    public static int[][][] generateKey(int[][] initialKey, int rounds) {
        int[][][] generatedKeys = new int[rounds + 1][4][4];
        generatedKeys[0] = initialKey;

        for (int round = 1; round <= rounds; round++) {
            int[][] prevKey = generatedKeys[round - 1];
            int[][] newKey = new int[4][4];

            // Generate first column of the new key
            int[] lastCol = getColumn(prevKey, 3);
            int[] rotated = rotWord(lastCol);
            int[] substituted = subBytes(rotated);
            int[] xorWithPrevFirst = xor(substituted, getColumn(prevKey, 0));
            int[] rconColumn = {rcon[round - 1], 0, 0, 0};
            int[] firstNewColumn = xor(xorWithPrevFirst, rconColumn);
            setColumn(newKey, 0, firstNewColumn);

            // Generate other columns
            for (int col = 1; col < 4; col++) {
                int[] newColumn = xor(getColumn(newKey, col - 1), getColumn(prevKey, col));
                setColumn(newKey, col, newColumn);
            }

            generatedKeys[round] = newKey;
        }

        return generatedKeys;
    }

    // Helper method to get a column from a matrix
    private static int[] getColumn(int[][] matrix, int col) {
        return new int[]{matrix[0][col], matrix[1][col], matrix[2][col], matrix[3][col]};
    }

    // Helper method to set a column in a matrix
    private static void setColumn(int[][] matrix, int col, int[] values) {
        for (int i = 0; i < 4; i++) {
            matrix[i][col] = values[i];
        }
    }

    // Rotate word operation for key expansion
    private static int[] rotWord(int[] word) {
        return new int[]{word[1], word[2], word[3], word[0]};
    }

    // SubBytes operation for key expansion
    private static int[] subBytes(int[] word) {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = sbox[(word[i] & 0xF0) >>> 4][word[i] & 0x0F];
        }
        return result;
    }

    // XOR operation for two arrays
    private static int[] xor(int[] a, int[] b) {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = a[i] ^ b[i];
        }
        return result;
    }

    // Method to print a key
    private static void printKey(int[][] key) {
        for (int[] row : key) {
            for (int value : row) {
                System.out.printf("%02X ", value);
            }
            System.out.println();
        }
    }
}