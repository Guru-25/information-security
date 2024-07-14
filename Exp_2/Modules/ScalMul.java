import java.util.Scanner;

public class ScalMul {
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

        // Input scalar value
        System.out.print("Enter scalar value: ");
        int scalar = scanner.nextInt();

        // Input modulo value
        System.out.print("Enter modulo value (m): ");
        int m = scanner.nextInt();

        // Perform scalar multiplication
        int[][] result = scalmul(matrixA, rA, cA, scalar, m);

        // Print result matrix
        System.out.println("Resultant matrix:");
        printMatrix(result);

        scanner.close();
    }

    // Scalar multiplication function
    public static int[][] scalmul(int[][] a, int p, int q, int scalar, int m) {
        int[][] result = new int[p][q];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < q; j++) {
                result[i][j] = ((a[i][j] * scalar) % m + m) % m; // Ensure positive result
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