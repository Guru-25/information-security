import java.util.Scanner;

public class Adjoint {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Example usage: Matrix input
        System.out.print("Enter size of square matrix (n x n): ");
        int n = scanner.nextInt();

        int[][] matrix = new int[n][n];

        System.out.println("Enter elements of the matrix (integer values):");
        inputMatrix(scanner, matrix);

        // Example usage: Compute adjoint
        int[][] adjointMatrix = adjoint(matrix, n);
        System.out.println("adjoint of the matrix:");
        printMatrix(adjointMatrix);

        scanner.close();
    }

    // Function to compute adjoint of matrix
    private static int[][] adjoint(int[][] matrix, int n) {
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