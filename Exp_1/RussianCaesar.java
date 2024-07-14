import java.util.Scanner;

class RussianCaesarCipher {
    private final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    public String encrypt(String plainText, int shiftKey) {
        plainText = plainText.toLowerCase();
        StringBuilder cipherText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            if (ALPHABET.indexOf(c) != -1) {
                int charPosition = ALPHABET.indexOf(c);
                int keyVal = (shiftKey + charPosition) % 33;
                char replaceVal = ALPHABET.charAt(keyVal);
                cipherText.append(replaceVal);
            } else {
                cipherText.append(c);
            }
        }
        return cipherText.toString();
    }

    public String decrypt(String cipherText, int shiftKey) {
        cipherText = cipherText.toLowerCase();
        StringBuilder plainText = new StringBuilder();
        for (char c : cipherText.toCharArray()) {
            if (ALPHABET.indexOf(c) != -1) {
                int charPosition = ALPHABET.indexOf(c);
                int keyVal = (charPosition - shiftKey + 33) % 33;
                char replaceVal = ALPHABET.charAt(keyVal);
                plainText.append(replaceVal);
            } else {
                plainText.append(c);
            }
        }
        return plainText.toString();
    }
}

class RussianCaesar {
    public static void main(String args[]) {
        boolean flag = true;
        Scanner s = new Scanner(System.in);
        RussianCaesarCipher rcc = new RussianCaesarCipher();

        while (flag) {
            System.out.print("1 for Encryption\n2 for Decryption\n3 for Exit\nEnter an option: ");
            int op = s.nextInt();
            s.nextLine(); // Consume newline
            
            switch (op) {
                case 1:
                    System.out.print("Enter a plain text: ");
                    String plainTextE = s.nextLine();
                    System.out.print("Enter the shift key: ");
                    int shiftKey = s.nextInt();
                    String cipherText = rcc.encrypt(plainTextE, shiftKey);
                    System.out.println("Encrypted text: " + cipherText + "\n");
                    break;
                case 2:
                    System.out.print("Enter a cipher text: ");
                    String cipherTextD = s.nextLine();
                    System.out.print("Enter the shift key: ");
                    int shiftKeyD = s.nextInt();
                    String plainTextD = rcc.decrypt(cipherTextD, shiftKeyD);
                    System.out.println("Decrypted text: " + plainTextD + "\n");
                    break;
                case 3:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid\n");
                    break;
            }
        }
        s.close();
    }
}