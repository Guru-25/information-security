import java.nio.ByteBuffer;
import java.util.Scanner;

class SHA1Step {
    // Rotate left (circular left shift)
    public static int rotateLeft(int value, int bits) {
        return (value << bits) | (value >>> (32 - bits));
    }

    // SHA-1 logical function based on the round number
    public static int f(int t, int B, int C, int D) {
        if (t >= 0 && t <= 19) {
            return (B & C) | ((~B) & D);
        } else if (t >= 20 && t <= 39) {
            return B ^ C ^ D;
        } else if (t >= 40 && t <= 59) {
            return (B & C) | (B & D) | (C & D);
        } else {
            return B ^ C ^ D;
        }
    }

    // SHA-1 constant K_t based on round number
    public static int K(int t) {
        if (t >= 0 && t <= 19) {
            return 0x5A827999;
        } else if (t >= 20 && t <= 39) {
            return 0x6ED9EBA1;
        } else if (t >= 40 && t <= 59) {
            return 0x8F1BBCDC;
        } else {
            return 0xCA62C1D6;
        }
    }

    // Message padding according to the SHA-1 specification
    public static byte[] padMessage(byte[] message) {
        int messageLength = message.length;
        long messageBitsLength = (long) messageLength * 8;
        int paddingLength = (messageLength % 64 < 56) ? 56 - (messageLength % 64) : 120 - (messageLength % 64);
        byte[] padding = new byte[paddingLength + 8];

        // Padding starts with a single 1 bit followed by 0 bits
        padding[0] = (byte) 0x80;

        // Append original length as a 64-bit big-endian integer at the end
        for (int i = 0; i < 8; i++) {
            padding[paddingLength + i] = (byte) (messageBitsLength >>> (56 - 8 * i));
        }

        byte[] paddedMessage = new byte[messageLength + padding.length];
        System.arraycopy(message, 0, paddedMessage, 0, messageLength);
        System.arraycopy(padding, 0, paddedMessage, messageLength, padding.length);

        return paddedMessage;
    }

    // SHA-1 one step function
    public static void sha1Step(int[] h, int W_t, int K_t, int t) {
        // Extract the current values of A, B, C, D, E
        int A = h[0];
        int B = h[1];
        int C = h[2];
        int D = h[3];
        int E = h[4];

        // Perform one step of SHA-1
        int temp = rotateLeft(A, 5) + f(t, B, C, D) + E + W_t + K_t;
        E = D;
        D = C;
        C = rotateLeft(B, 30);
        B = A;
        A = temp;

        // Store the updated values back
        h[0] = A;
        h[1] = B;
        h[2] = C;
        h[3] = D;
        h[4] = E;
    }

    // Process a 512-bit block of the message
    public static void processBlock(byte[] block, int[] h) {
        int[] W = new int[80];

        // Prepare the message schedule W_t
        ByteBuffer buffer = ByteBuffer.wrap(block);
        for (int i = 0; i < 16; i++) {
            W[i] = buffer.getInt(i * 4);
        }

        for (int t = 16; t < 80; t++) {
            W[t] = rotateLeft(W[t - 3] ^ W[t - 8] ^ W[t - 14] ^ W[t - 16], 1);
        }

        // Copy the current hash value
        int[] tempHash = h.clone();

        // Perform 80 rounds
        for (int t = 0; t < 80; t++) {
            sha1Step(tempHash, W[t], K(t), t);
        }

        // Add the current hash value to the result
        for (int i = 0; i < 5; i++) {
            h[i] += tempHash[i];
        }
    }

    public static String toHexString(int[] h) {
        StringBuilder sb = new StringBuilder();
        for (int value : h) {
            sb.append(String.format("%08x", value));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the text to hash using SHA-1:");
        String inputText = scanner.nextLine();

        // Convert input text to bytes and pad the message
        byte[] message = null;
        try {
            message = inputText.getBytes("UTF-8");
        } catch (Exception e) {
            System.out.println("Error with encoding: " + e.getMessage());
        }
        
        if (message != null) {
            byte[] paddedMessage = padMessage(message);

            // Initial SHA-1 hash values (H0 - H4)
            int[] h = { 0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0 };

            // Process each 512-bit block
            for (int i = 0; i < paddedMessage.length / 64; i++) {
                byte[] block = new byte[64];
                System.arraycopy(paddedMessage, i * 64, block, 0, 64);
                processBlock(block, h);
            }

            // Output the final hash
            System.out.println("SHA-1 Hash of the input: " + toHexString(h));
        }
    }
}
