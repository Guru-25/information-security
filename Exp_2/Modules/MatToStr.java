import java.util.Scanner;

public class MatToStr {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Example usage: Matrix input
        System.out.print("Enter number of rows for the matrix: ");
        int rows = scanner.nextInt();
        System.out.print("Enter number of columns for the matrix: ");
        int cols = scanner.nextInt();

        int[][] matrix = new int[rows][cols];

        System.out.println("Enter elements of the matrix (integer values):");
        inputMatrix(scanner, matrix);

        // Convert matrix to string
        String resultString = mattostr(matrix, rows, cols);
        System.out.println("Converted matrix to string: " + resultString);

        scanner.close();
    }

    // Function to convert matrix to string
    public static String mattostr(int[][] mat, int p, int q) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < q; j++) {
                result.append((char) (mat[i][j] + 'A'));
            }
        }
        return result.toString();
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