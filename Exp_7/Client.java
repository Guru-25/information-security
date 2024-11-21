import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Client {
    private static final int BIT_LENGTH = 512; // Bit length for the private key

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6000)) {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            // Receive the public parameters (p, g) and server's public key
            BigInteger p = new BigInteger(input.readUTF());
            BigInteger g = new BigInteger(input.readUTF());
            BigInteger serverPublicKey = new BigInteger(input.readUTF());
            System.out.println("Received values from server: Prime number (p) = " + p + ", Base (g) = " + g);
            System.out.println("Received server's public key: " + serverPublicKey);

            // Generate the client's private key
            SecureRandom random = new SecureRandom();
            BigInteger clientPrivateKey = new BigInteger(BIT_LENGTH, random);
            System.out.println("Client's private key has been generated: " + clientPrivateKey);

            // Compute the client's public key
            BigInteger clientPublicKey = g.modPow(clientPrivateKey, p);
            System.out.println("Client's public key (to be shared with the server): " + clientPublicKey);

            // Send the client's public key to the server
            output.writeUTF(clientPublicKey.toString());
            System.out.println("Client's public key has been sent to the server.");

            // Compute the shared secret
            BigInteger sharedSecret = serverPublicKey.modPow(clientPrivateKey, p);
            System.out.println("Shared secret successfully calculated: " + sharedSecret);

            // Close the connection
            socket.close();
            System.out.println("Connection closed.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
