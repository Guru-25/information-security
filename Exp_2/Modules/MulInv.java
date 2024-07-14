import java.util.Scanner;

public class MulInv {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter value for a: ");
        int a = scanner.nextInt();

        System.out.print("Enter value for m: ");
        int m = scanner.nextInt();

        int result = mulinv(a, m);
        System.out.println("Multiplicative inverse of " + a + " modulo " + m + " is: " + result);

        scanner.close();
    }

    public static int mulinv(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1; // If no multiplicative inverse exists
    }
}