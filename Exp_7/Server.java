import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class Server {
    private static final int BIT_LENGTH = 512; // Bit length for the private key

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is now listening on port 5000...");
            
            // Wait for a connection from the client
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            Scanner scanner = new Scanner(System.in);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            // Get user input for the prime number (p) and base (g)
            System.out.print("Please enter a large prime number (p): ");
            BigInteger p = new BigInteger(scanner.nextLine());
            
            System.out.print("Please enter a base (g): ");
            BigInteger g = new BigInteger(scanner.nextLine());

            // Generate the server's private key
            SecureRandom random = new SecureRandom();
            BigInteger serverPrivateKey = new BigInteger(BIT_LENGTH, random);
            System.out.println("Server's private key has been generated: " + serverPrivateKey);

            // Compute the server's public key
            BigInteger serverPublicKey = g.modPow(serverPrivateKey, p);
            System.out.println("Server's public key (to be shared with the client): " + serverPublicKey);

            // Send the public parameters (p, g) and the server's public key to the client
            output.writeUTF(p.toString());
            output.writeUTF(g.toString());
            output.writeUTF(serverPublicKey.toString());
            System.out.println("Public values (p, g) and server public key have been sent to the client.");

            // Receive the client's public key
            BigInteger clientPublicKey = new BigInteger(input.readUTF());
            System.out.println("Received client's public key: " + clientPublicKey);

            // Compute the shared secret
            BigInteger sharedSecret = clientPublicKey.modPow(serverPrivateKey, p);
            System.out.println("Shared secret successfully calculated: " + sharedSecret);

            // Close the connection
            socket.close();
            scanner.close();
            System.out.println("Connection closed.");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
