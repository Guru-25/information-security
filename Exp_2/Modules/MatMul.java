import java.util.Scanner;

public class MatMul {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input matrix A
        System.out.print("Enter number of rows for matrix A: ");
        int rA = scanner.nextInt();
        System.out.print("Enter number of columns for matrix A: ");
        int cA = scanner.nextInt();
        int[][] matrixA = new int[rA][cA];
        System.out.println("Enter elements of matrix A:");
        inputMatrix(scanner, matrixA);

        // Input matrix B
        System.out.print("Enter number of rows for matrix B: ");
        int rB = scanner.nextInt();
        System.out.print("Enter number of columns for matrix B: ");
        int cB = scanner.nextInt();
        int[][] matrixB = new int[rB][cB];
        System.out.println("Enter elements of matrix B:");
        inputMatrix(scanner, matrixB);

        // Validate dimensions for multiplication
        if (cA != rB) {
            System.out.println("Invalid matrix dimensions for multiplication.");
            return;
        }

        // Multiply matrices
        int[][] result = matmul(matrixA, matrixB, rA, cA, rB, cB);

        // Print result matrix
        System.out.println("Resultant matrix:");
        printMatrix(result);

        scanner.close();
    }

    // Matrix multiplication function
    public static int[][] matmul(int[][] a, int[][] b, int p, int q, int r, int s) {
        int[][] result = new int[p][s];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < s; j++) {
                for (int k = 0; k < q; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
                // Optional: Ensure positive result in modulo operation
                result[i][j] = (result[i][j] % 26 + 26) % 26;
            }
        }
        return result;
    }

    // Utility function to input matrix elements
    public static void inputMatrix(Scanner scanner, int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }
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