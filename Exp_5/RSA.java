import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RSA {

    // Function to calculate GCD using BigInteger
    public static BigInteger getGCD(BigInteger a, BigInteger b) {
        return b.equals(BigInteger.ZERO) ? a : getGCD(b, a.mod(b));
    }

    // Function to check if a number is prime
    public static boolean isPrime(BigInteger number) {
        return number.isProbablePrime(100); // Use a probabilistic primality test
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BigInteger n = null, e = null, d = null; // Store n, e, d

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Key Generation");
            System.out.println("2. Encryption");
            System.out.println("3. Decryption");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: // Key Generation
                    System.out.print("Enter prime p (up to 100 digits): ");
                    BigInteger p = new BigInteger(scanner.next());

                    System.out.print("Enter prime q (up to 100 digits): ");
                    BigInteger q = new BigInteger(scanner.next());

                    if (!isPrime(p) || !isPrime(q)) {
                        System.out.println("Both p and q must be prime numbers.");
                        break;
                    }

                    n = p.multiply(q);
                    BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

                    System.out.println("Modulus n: " + n);
                    System.out.print("Enter public key e (should be coprime with phi): ");
                    e = new BigInteger(scanner.next());

                    // Validate e
                    if (getGCD(e, phi).equals(BigInteger.ONE)) {
                        // Compute d
                        d = e.modInverse(phi);
                        System.out.println("Public key (e, n): (" + e + ", " + n + ")");
                        System.out.println("Private key (d, n): (" + d + ", " + n + ")");
                    } else {
                        System.out.println("Invalid public key e. It must be coprime with phi.");
                    }
                    break;

                case 2: // Encryption
                    if (n == null || e == null) {
                        System.out.println("Please generate keys first.");
                        break;
                    }
                    System.out.print("Enter the plaintext message as a string: ");
                    scanner.nextLine(); // Consume newline
                    String message = scanner.nextLine();

                    StringBuilder cipherText = new StringBuilder();
                    for (char ch : message.toCharArray()) {
                        BigInteger messageBigInt = BigInteger.valueOf((int) ch);
                        BigInteger encryptedChar = messageBigInt.modPow(e, n);
                        cipherText.append(encryptedChar).append(" ");
                    }
                    System.out.println("Cipher text: " + cipherText.toString().trim());
                    break;

                case 3: // Decryption
                    if (n == null || d == null) {
                        System.out.println("Please generate keys first.");
                        break;
                    }
                    System.out.print("Enter the encrypted message as integers separated by spaces: ");
                    scanner.nextLine(); // Consume newline
                    String[] encryptedMessageParts = scanner.nextLine().split(" ");

                    StringBuilder decryptedMessage = new StringBuilder();
                    for (String part : encryptedMessageParts) {
                        BigInteger encryptedChar = new BigInteger(part);
                        BigInteger decryptedCharBigInt = encryptedChar.modPow(d, n);
                        char decryptedChar = (char) (decryptedCharBigInt.intValue());
                        decryptedMessage.append(decryptedChar);
                    }
                    System.out.println("Decrypted message: " + decryptedMessage.toString());
                    break;

                case 4: // Exit
                    scanner.close();
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
