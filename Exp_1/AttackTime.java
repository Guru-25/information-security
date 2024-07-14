import java.util.*;
import java.time.Instant;
import java.time.Duration;

class CaesarCipher {
    private final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private final String FREQUENT_ALPHABET = "оеаинтсрвлкмдпуяыьгзбчйхжшюцщэфъё";
    
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

    public long bruteForceAttack(String cipherText) {
        Instant start = Instant.now();
        for (int i = 0; i < ALPHABET.length(); i++) {
            decrypt(cipherText, i);
        }
        Instant end = Instant.now();
        return Duration.between(start, end).toMillis();
    }
    
    public long frequencyAnalysisAttack(String cipherText) {
        Instant start = Instant.now();
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : cipherText.toLowerCase().toCharArray()) {
            if (ALPHABET.indexOf(c) != -1) {
                freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
            }
        }
        
        List<Map.Entry<Character, Integer>> sortedFreq = new ArrayList<>(freqMap.entrySet());
        sortedFreq.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        char mostFrequentChar = sortedFreq.get(0).getKey();
        
        for (char assumedMostFrequent : FREQUENT_ALPHABET.toCharArray()) {
            int estimatedShift = (ALPHABET.indexOf(mostFrequentChar) - ALPHABET.indexOf(assumedMostFrequent) + ALPHABET.length()) % ALPHABET.length();
            decrypt(cipherText, estimatedShift);
        }
        Instant end = Instant.now();
        return Duration.between(start, end).toMillis();
    }

    public String generateRandomRussianText(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}

class AttackTime {
    public static void main(String[] args) {
        CaesarCipher cc = new CaesarCipher();
        int[] sampleSizes = {125000, 250000, 375000, 500000, 625000, 750000, 875000, 1000000, 1125000, 1250000};
        int samples = 10;

        System.out.println("Sample Size,Brute Force (ms),Frequency Analysis (ms)");

        for (int size : sampleSizes) {
            long totalBruteForce = 0;
            long totalFrequencyAnalysis = 0;

            for (int i = 0; i < samples; i++) {
                String plainText = cc.generateRandomRussianText(size);
                String cipherText = cc.encrypt(plainText, 3); // Using a fixed shift of 3 for simplicity

                totalBruteForce += cc.bruteForceAttack(cipherText);
                totalFrequencyAnalysis += cc.frequencyAnalysisAttack(cipherText);
            }

            double avgBruteForce = totalBruteForce / (double) samples;
            double avgFrequencyAnalysis = totalFrequencyAnalysis / (double) samples;

            System.out.printf("%d,%.2f,%.2f%n", size, avgBruteForce, avgFrequencyAnalysis);
        }
    }
}