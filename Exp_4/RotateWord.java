public class RotateWord {

    public static void main(String[] args) {
        // Example 4x4 matrix with hexadecimal values
        int[][] matrix = {
            {0x2b, 0x28, 0xab, 0x09},
            {0x7e, 0xae, 0xa7, 0xcf},
            {0x15, 0xb2, 0x15, 0x4f},
            {0x16, 0xa6, 0x88, 0x3c}
        };

        // Perform key scheduling
        int[] rotateWord = getRotateWord(matrix);

        // Print the rotateWord
        System.out.println("RotateWord:");
        for (int value : rotateWord) {
            System.out.printf("%02X ", value);
        }
    }

    public static int[] getRotateWord(int[][] matrix) {
        // Check if the input is a 4x4 matrix
        if (matrix.length != 4 || matrix[0].length != 4) {
            throw new IllegalArgumentException("Input must be a 4x4 matrix.");
        }

        // Extract the last column
        int[] lastColumn = new int[4];
        for (int i = 0; i < 4; i++) {
            lastColumn[i] = matrix[i][3];
        }

        // Rotate the first row element to the last
        int temp = lastColumn[0];
        for (int i = 0; i < 3; i++) {
            lastColumn[i] = lastColumn[i + 1];
        }
        lastColumn[3] = temp;

        return lastColumn;
    }
}
