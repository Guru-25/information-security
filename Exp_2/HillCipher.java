import java.util.Scanner;

public class HillCipher {
    // Method to find multiplicative inverse
    public static int mulinv(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1; // If no multiplicative inverse exists
    }

    // Matrix multiplication
    public static int[][] matmul(int[][] a, int[][] b, int p, int q, int r, int s) {
        if (q != r) {
            throw new IllegalArgumentException("Invalid matrix dimensions for multiplication");
        }
        
        int[][] result = new int[p][s];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < s; j++) {
                for (int k = 0; k < q; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
                result[i][j] = (result[i][j] % 26 + 26) % 26; // Ensure positive result
            }
        }
        return result;
    }

    // Scalar multiplication
    public static int[][] scalmul(int[][] a, int p, int q, int scalar, int m) {
        int[][] result = new int[p][q];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < q; j++) {
                result[i][j] = ((a[i][j] * scalar) % m + m) % m; // Ensure positive result
            }
        }
        return result;
    }

    // String to matrix conversion
    public static int[][] strtomat(String pt, int k) {
        pt = pt.toUpperCase().replaceAll("[^A-Z]", "");
        int len = pt.length();
        int padding = (k - (len % k)) % k;  // Calculate needed padding
        if (padding > 0) {
            pt += "X".repeat(padding);  // Add 'X' for padding
        }
        
        int rows = pt.length() / k;
        int[][] matrix = new int[rows][k];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < k; j++) {
                matrix[i][j] = pt.charAt(i * k + j) - 'A';
            }
        }
        return matrix;
    }

    // Matrix to string conversion
    public static String mattostr(int[][] mat, int p, int q) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < q; j++) {
                result.append((char) (mat[i][j] + 'A'));
            }
        }
        return result.toString();
    }

    // Determinant of matrix
    public static int matdet(int[][] a, int n, int m) {
        if (n == 1) return a[0][0];
        int det = 0;
        for (int i = 0; i < n; i++) {
            int[][] submatrix = new int[n-1][n-1];
            for (int j = 1; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (k < i) submatrix[j-1][k] = a[j][k];
                    else if (k > i) submatrix[j-1][k-1] = a[j][k];
                }
            }
            det += Math.pow(-1, i) * a[0][i] * matdet(submatrix, n-1, m);
        }
        return ((det % m) + m) % m;
    }

    // Adjugate of matrix
    private static int[][] adjugate(int[][] matrix, int n) {
        int[][] adj = new int[n][n];
        if (n == 1) {
            adj[0][0] = 1;
            return adj;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int[][] submatrix = new int[n-1][n-1];
                int sub_i = 0, sub_j = 0;
                for (int r = 0; r < n; r++) {
                    if (r == i) continue;
                    sub_j = 0;
                    for (int c = 0; c < n; c++) {
                        if (c == j) continue;
                        submatrix[sub_i][sub_j] = matrix[r][c];
                        sub_j++;
                    }
                    sub_i++;
                }
                adj[j][i] = (int) (Math.pow(-1, i+j) * matdet(submatrix, n-1, 26) + 26) % 26;
            }
        }
        return adj;
    }

    // Encrypt method
    public static String encrypt(String plaintext, int[][] key) {
        int k = key.length;
        int[][] ptMatrix = strtomat(plaintext, k);
        int[][] ctMatrix = matmul(ptMatrix, key, ptMatrix.length, k, k, k);
        return mattostr(ctMatrix, ctMatrix.length, k);
    }

    // Decrypt method
    public static String decrypt(String ciphertext, int[][] key) {
        int k = key.length;
        int m = 26; // Alphabet size
        
        int det = matdet(key, k, m);
        int detInv = mulinv(det, m);
        
        if (detInv == -1) {
            throw new IllegalArgumentException("Key is not invertible");
        }
        
        int[][] adjKey = adjugate(key, k);
        int[][] invKey = scalmul(adjKey, k, k, detInv, m);
        
        int[][] ctMatrix = strtomat(ciphertext, k);
        int[][] ptMatrix = matmul(ctMatrix, invKey, ctMatrix.length, k, k, k);
        return mattostr(ptMatrix, ptMatrix.length, k);
    }

    // Known Plaintext Attack
    public static int[][] knownPlaintextAttack(String plaintext, String ciphertext, int n) {
        int[][] P = strtomat(plaintext, n);
        int[][] C = strtomat(ciphertext, n);

        if (P.length != C.length || P[0].length != n || C[0].length != n) {
            throw new IllegalArgumentException("Invalid input sizes for attack");
        }

        int[][] P_inv = matrixInverse(P, 26);
        if (P_inv == null) {
            throw new IllegalArgumentException("Plaintext matrix is not invertible");
        }

        return matmul(P_inv, C, n, n, n, n); // P_inv * C
    }

    // Matrix inverse
    public static int[][] matrixInverse(int[][] matrix, int m) {
        int n = matrix.length;
        int det = matdet(matrix, n, m);
        int detInv = mulinv(det, m);

        if (detInv == -1) {
            return null; // Matrix is not invertible
        }

        int[][] adj = adjugate(matrix, n);
        return scalmul(adj, n, n, detInv, m);
    }

    // Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[][] key = null;
        int keySize;

        while (true) {
            System.out.println("\nHill Cipher Menu:");
            System.out.println("1. Set Encryption Key");
            System.out.println("2. Encrypt Message");
            System.out.println("3. Decrypt Message");
            System.out.println("4. Known Plaintext Attack");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter key size (n): ");
                    keySize = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    key = new int[keySize][keySize];
                    System.out.println("Enter key matrix elements:");
                    for (int i = 0; i < keySize; i++) {
                        for (int j = 0; j < keySize; j++) {
                            key[i][j] = scanner.nextInt();
                        }
                    }
                    System.out.println("Key set successfully!");
                    break;
                case 2:
                    if (key == null) {
                        System.out.println("Please set the key first!");
                        break;
                    }
                    System.out.print("Enter plaintext: ");
                    String plaintext = scanner.nextLine();
                    String ciphertext = encrypt(plaintext, key);
                    System.out.println("Encrypted text: " + ciphertext);
                    break;
                case 3:
                    if (key == null) {
                        System.out.println("Please set the key first!");
                        break;
                    }
                    System.out.print("Enter ciphertext: ");
                    String encryptedText = scanner.nextLine();
                    try {
                        String decryptedText = decrypt(encryptedText, key);
                        System.out.println("Decrypted text: " + decryptedText);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.print("Enter known plaintext: ");
                    String knownPlaintext = scanner.nextLine();
                    System.out.print("Enter corresponding ciphertext: ");
                    String knownCiphertext = scanner.nextLine();
                    System.out.print("Enter key size (n): ");
                    keySize = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    try {
                        int[][] recoveredKey = knownPlaintextAttack(knownPlaintext, knownCiphertext, keySize);
                        System.out.println("Recovered Key:");
                        for (int i = 0; i < keySize; i++) {
                            for (int j = 0; j < keySize; j++) {
                                System.out.print(recoveredKey[i][j] + " ");
                            }
                            System.out.println();
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Exiting program. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
