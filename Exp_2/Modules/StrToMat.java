import java.util.Scanner;

public class StrToMat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input plaintext string
        System.out.print("Enter plaintext string (only A-Z characters): ");
        String pt = scanner.nextLine();

        // Input value of k (matrix column size)
        System.out.print("Enter value of k (matrix column size): ");
        int k = scanner.nextInt();

        // Convert string to matrix
        int[][] matrix = strtomat(pt, k);

        // Print resulting matrix
        System.out.println("Resultant matrix:");
        printMatrix(matrix);

        scanner.close();
    }

    // Function to convert string to matrix
    public static int[][] strtomat(String pt, int k) {
        pt = pt.toUpperCase().replaceAll("[^A-Z]", ""); // Remove non A-Z characters and convert to uppercase
        int len = pt.length();
        int padding = (k - (len % k)) % k; // Calculate needed padding
        if (padding > 0) {
            pt += "X".repeat(padding); // Add 'X' for padding
        }

        int rows = pt.length() / k;
        int[][] matrix = new int[rows][k];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < k; j++) {
                matrix[i][j] = pt.charAt(i * k + j) - 'A'; // Convert character to numeric value ('A' = 0, 'B' = 1, ..., 'Z' = 25)
            }
        }
        return matrix;
    }

    // Utility function to print matrix
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}