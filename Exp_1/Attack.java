import java.util.*;

class CaesarCipher {
    private final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private final String FREQUENT_ALPHABET = "aeioutnshrdlcmwfgypbvkjxqz";
    
    public String encrypt(String plainText, int shiftKey) {
        plainText = plainText.toLowerCase();
        StringBuilder cipherText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            if (ALPHABET.indexOf(c) != -1) {
                int charPosition = ALPHABET.indexOf(c);
                int keyVal = (shiftKey + charPosition) % 26;
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
                int keyVal = (charPosition - shiftKey + 26) % 26;
                char replaceVal = ALPHABET.charAt(keyVal);
                plainText.append(replaceVal);
            } else {
                plainText.append(c);
            }
        }
        return plainText.toString();
    }
    
    public void bruteForceAttack(String cipherText) {
        System.out.println("Brute Force Attack Results:");
        for (int i = 0; i < 26; i++) {
            String plainText = decrypt(cipherText, i);
            System.out.println("Key " + i + ": " + plainText);
        }
    }
    
    public void frequencyAnalysisAttack(String cipherText) {
        System.out.println("Frequency Analysis Attack Results:");
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : cipherText.toLowerCase().toCharArray()) {
            if (ALPHABET.indexOf(c) != -1) {
                freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
            }
        }
        
        List<Map.Entry<Character, Integer>> sortedFreq = new ArrayList<>(freqMap.entrySet());
        sortedFreq.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        char mostFrequentChar = sortedFreq.get(0).getKey();
        System.out.println("Most frequent character in cipher text: " + mostFrequentChar);
        
        System.out.println("Possible decryptions (assuming most frequent letter in plain text):");
        for (char assumedMostFrequent : FREQUENT_ALPHABET.toCharArray()) {
            int estimatedShift = (ALPHABET.indexOf(mostFrequentChar) - ALPHABET.indexOf(assumedMostFrequent) + 26) % 26;
            String decryptedText = decrypt(cipherText, estimatedShift);
            System.out.println(assumedMostFrequent + " - Key " + estimatedShift + ": " + decryptedText);
        }
    }
}

class Attack {
    public static void main(String[] args) {
        boolean flag = true;
        Scanner s = new Scanner(System.in);
        CaesarCipher cc = new CaesarCipher();
        
        while (flag) {
            System.out.print("1 for Encryption\n2 for Decryption\n3 for Brute Force Attack\n4 for Frequency Analysis Attack\n5 for Exit\nEnter an option: ");
            int op = s.nextInt();
            s.nextLine(); // Consume newline
            
            switch (op) {
                case 1:
                    System.out.print("Enter a plain text: ");
                    String plainTextE = s.nextLine();
                    System.out.print("Enter the shift key: ");
                    int shiftKey = s.nextInt();
                    String cipherText = cc.encrypt(plainTextE, shiftKey);
                    System.out.println("Encrypted text: " + cipherText + "\n");
                    break;
                case 2:
                    System.out.print("Enter a cipher text: ");
                    String cipherTextD = s.nextLine();
                    System.out.print("Enter the shift key: ");
                    int shiftKeyD = s.nextInt();
                    String plainTextD = cc.decrypt(cipherTextD, shiftKeyD);
                    System.out.println("Decrypted text: " + plainTextD + "\n");
                    break;
                case 3:
                    System.out.print("Enter a cipher text: ");
                    String bruteForceText = s.nextLine();
                    cc.bruteForceAttack(bruteForceText);
                    System.out.println();
                    break;
                case 4:
                    System.out.print("Enter a cipher text: ");
                    String freqAnalysisText = s.nextLine();
                    cc.frequencyAnalysisAttack(freqAnalysisText);
                    System.out.println();
                    break;
                case 5:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid option\n");
                    break;
            }
        }
        s.close();
    }
}