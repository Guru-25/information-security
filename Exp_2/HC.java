import java.util.Scanner;

public class HC {
    private final String alphaList;
    private final int m;

    public HC(int m, String alphaList) {
        this.alphaList = alphaList;
        this.m = m;
    }

    private int findGCD(int a, int b) {
        if (a < b) {
            int temp = a;
            a = b;
            b = temp;
        }
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private int extendedEuclids(int a, int b, int m) {
        int t1 = 0, t2 = 1;
        if (a < b) {
            int temp = a;
            a = b;
            b = temp;
        }
        while (b != 0) {
            int q = a / b;
            int temp = b;
            b = a % b;
            a = temp;
            int t = t1 - q * t2;
            t1 = t2;
            t2 = t;
        }
        if (t1 < 0) {
            return t1 + m;
        } else {
            return t1;
        }
    }

    private int[][] convertStrMat(String word, int syllable) {
        while (word.length() % syllable != 0) {
            word += 'X';
        }
        int rows = syllable;
        int cols = word.length() / syllable;
        int[][] result = new int[rows][cols];
        int k = 0;
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                result[j][i] = alphaList.indexOf(word.charAt(k++));
            }
        }
        return result;
    }

    private String convertMatStr(int[][] mat) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < mat[0].length; i++) {
            for (int j = 0; j < mat.length; j++) {
                text.append(alphaList.charAt(mat[j][i] % m));
            }
        }
        return text.toString();
    }

    private int[][] findMatMul(int[][] matA, int[][] matB) {
        int rowsA = matA.length;
        int colsA = matA[0].length;
        int colsB = matB[0].length;
        int[][] result = new int[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += matA[i][k] * matB[k][j];
                }
                result[i][j] %= m;
            }
        }
        return result;
    }

    private int[][] removeCopyMatrix(int[][] mat, int i, int j) {
        int size = mat.length - 1;
        int[][] result = new int[size][size];
        for (int row = 0, newRow = 0; row < mat.length; row++) {
            if (row == i) continue;
            for (int col = 0, newCol = 0; col < mat[0].length; col++) {
                if (col == j) continue;
                result[newRow][newCol++] = mat[row][col];
            }
            newRow++;
        }
        return result;
    }

    private boolean checkSquareMatrix(int[][] mat) {
        for (int[] row : mat) {
            if (row.length != mat.length) return false;
        }
        return true;
    }

    private int findDeterminant(int[][] mat) {
        if (!checkSquareMatrix(mat)) return -1;
        if (mat.length == 2) {
            return (mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0]) % m;
        }
        int det = 0;
        for (int c = 0; c < mat.length; c++) {
            det += ((c % 2 == 0 ? 1 : -1) * mat[0][c] * findDeterminant(removeCopyMatrix(mat, 0, c))) % m;
        }
        return det < 0 ? det + m : det;
    }

    private int[][] findAdjoint(int[][] mat) {
        int size = mat.length;
        int[][] adjoint = new int[size][size];
        if (size == 2) {
            adjoint[0][0] = mat[1][1];
            adjoint[1][1] = mat[0][0];
            adjoint[0][1] = -mat[0][1];
            adjoint[1][0] = -mat[1][0];
            return adjoint;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int[][] minor = removeCopyMatrix(mat, i, j);
                adjoint[j][i] = ((i + j) % 2 == 0 ? 1 : -1) * findDeterminant(minor) % m;
                if (adjoint[j][i] < 0) adjoint[j][i] += m;
            }
        }
        return adjoint;
    }

    private int[][] scalarMultiply(int scalar, int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                mat[i][j] = (scalar * mat[i][j]) % m;
                if (mat[i][j] < 0) mat[i][j] += m;
            }
        }
        return mat;
    }

    public String encrypt(String plaintext, int[][] keyMatrix) {
        int[][] plaintextMatrix = convertStrMat(plaintext, keyMatrix.length);
        System.out.println("Plaintext Matrix:");
        printMatrix(plaintextMatrix);
        int[][] cipherMatrix = findMatMul(keyMatrix, plaintextMatrix);
        System.out.println("Ciphertext Matrix:");
        printMatrix(cipherMatrix);
        return convertMatStr(cipherMatrix);
    }

    public String decrypt(String ciphertext, int[][] keyMatrix) {
        int det = findDeterminant(keyMatrix);
        if (det == 0 || findGCD(det, m) != 1) {
            throw new IllegalArgumentException("Matrix is not invertible");
        }
        int detInv = extendedEuclids(det, m, m);
        int[][] adjointMatrix = findAdjoint(keyMatrix);
        int[][] inverseMatrix = scalarMultiply(detInv, adjointMatrix);
        System.out.println("Inverse Key Matrix:");
        printMatrix(inverseMatrix);
        int[][] ciphertextMatrix = convertStrMat(ciphertext, inverseMatrix.length);
        int[][] plaintextMatrix = findMatMul(inverseMatrix, ciphertextMatrix);
        return convertMatStr(plaintextMatrix).replaceAll("X*$", "");
    }

    public int[][] knownPlaintextAttack(String pt, String ct, int size) {
        int[][] ptMatrix = convertStrMat(pt, size);
        System.out.println("Plaintext Matrix:");
        printMatrix(ptMatrix);

        int[][] ctMatrix = convertStrMat(ct, size);
        System.out.println("Ciphertext Matrix:");
        printMatrix(ctMatrix);

        int detCt = findDeterminant(ctMatrix);
        System.out.println("Determinant of Ciphertext Matrix: " + detCt);

        int detInv = extendedEuclids(detCt, m, m);
        System.out.println("Inverse of Determinant: " + detInv);

        int[][] adjCt = findAdjoint(ctMatrix);
        System.out.println("Adjoint of Ciphertext Matrix:");
        printMatrix(adjCt);

        int[][] ctInv = scalarMultiply(detInv, adjCt);
        System.out.println("Inverse of Ciphertext Matrix:");
        printMatrix(ctInv);

        int[][] keyInv = findMatMul(ptMatrix, ctInv);
        System.out.println("Inverse of Key Matrix:");
        printMatrix(keyInv);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ciphertext to decrypt using the found key: ");
        String newCiphertext = scanner.nextLine().toUpperCase();
        int[][] newCiphertextMatrix = convertStrMat(newCiphertext, keyInv.length);
        int[][] newPlaintextMatrix = findMatMul(keyInv, newCiphertextMatrix);
        System.out.println("Decrypted plaintext Matrix using the found key:");
        printMatrix(newPlaintextMatrix);
        String PlainText = convertMatStr(newPlaintextMatrix);
        System.out.println("PlainText:" + PlainText);

        return keyInv;
    }

    private void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int elem : row) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String alphaList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        HC hillCipher = new HC(alphaList.length(), alphaList);

        while (true) {
            System.out.println("\nHill Cipher Menu");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Known Plaintext Attack");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter the plaintext: ");
                    String plaintext = scanner.nextLine().toUpperCase();
                    System.out.print("Enter the size of the key matrix: ");
                    int size = scanner.nextInt();
                    int[][] keyMatrix = new int[size][size];
                    System.out.println("Enter the key matrix:");
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            keyMatrix[i][j] = scanner.nextInt();
                        }
                    }
                    String ciphertext = hillCipher.encrypt(plaintext, keyMatrix);
                    System.out.println("Ciphertext: " + ciphertext);
                }
                case 2 -> {
                    System.out.print("Enter the ciphertext: ");
                    String ciphertext = scanner.nextLine().toUpperCase();
                    System.out.print("Enter the size of the key matrix: ");
                    int size = scanner.nextInt();
                    int[][] keyMatrix = new int[size][size];
                    System.out.println("Enter the key matrix:");
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            keyMatrix[i][j] = scanner.nextInt();
                        }
                    }
                    String plaintext = hillCipher.decrypt(ciphertext, keyMatrix);
                    System.out.println("Decrypted Plaintext: " + plaintext);
                }
                case 3 -> {
                    System.out.print("Enter the known plaintext: ");
                    String pt = scanner.nextLine().toUpperCase();
                    System.out.print("Enter the corresponding ciphertext: ");
                    String ct = scanner.nextLine().toUpperCase();
                    System.out.print("Enter the size of the key matrix: ");
                    int size = scanner.nextInt();
                    hillCipher.knownPlaintextAttack(pt, ct, size);
                }
                case 4 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice! Please select again.");
            }
        }
    }
}
