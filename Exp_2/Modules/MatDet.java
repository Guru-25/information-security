import java.util.Scanner;

public class MatDet {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Example usage: Matrix input
        System.out.print("Enter size of square matrix (n x n): ");
        int n = scanner.nextInt();

        int[][] matrix = new int[n][n];

        System.out.println("Enter elements of the matrix (integer values):");
        inputMatrix(scanner, matrix);

        // Example usage: Compute determinant
        int determinant = matdet(matrix, n, 26);
        System.out.println("Determinant of the matrix: " + determinant);

        scanner.close();
    }

    // Function to compute determinant of matrix
    public static int matdet(int[][] a, int n, int m) {
        if (n == 1) {
            return a[0][0] % m;
        }

        int det = 0;
        for (int i = 0; i < n; i++) {
            int[][] submatrix = new int[n-1][n-1];
            for (int j = 1; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (k < i) {
                        submatrix[j-1][k] = a[j][k];
                    } else if (k > i) {
                        submatrix[j-1][k-1] = a[j][k];
                    }
                }
            }
            det += Math.pow(-1, i) * a[0][i] * matdet(submatrix, n-1, m);
        }
        return ((det % m) + m) % m; // Ensure positive result within modulo m
    }

    // Utility function to input matrix elements
    public static void inputMatrix(Scanner scanner, int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }
    }
}