import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EllipticCurveCryptography {

    // Generate elliptic curve points
    public static List<BigInteger[]> keygen(BigInteger a, BigInteger b, BigInteger p) {
        List<BigInteger[]> points = new ArrayList<>();
        for (BigInteger i = BigInteger.ZERO; i.compareTo(p) < 0; i = i.add(BigInteger.ONE)) {
            BigInteger R = i.pow(3).add(a.multiply(i)).add(b).mod(p);
            for (BigInteger j = BigInteger.ZERO; j.compareTo(p) < 0; j = j.add(BigInteger.ONE)) {
                BigInteger L = j.pow(2).mod(p);
                if (L.equals(R)) {
                    points.add(new BigInteger[]{i, j});
                }
            }
        }
        return points;
    }

    // Scalar multiplication on elliptic curve
    public static BigInteger[] scalarMultiply(BigInteger[] P, BigInteger n, BigInteger a, BigInteger p) {
        BigInteger[] result = null; // Point at infinity
        BigInteger[] current = P;

        while (n.compareTo(BigInteger.ZERO) > 0) {
            if (n.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
                result = pointAdd(result, current, a, p);
            }
            current = pointAdd(current, current, a, p);
            n = n.divide(BigInteger.TWO);
        }

        return result;
    }

    // Point addition on elliptic curve
    public static BigInteger[] pointAdd(BigInteger[] P, BigInteger[] Q, BigInteger a, BigInteger p) {
        if (P == null) return Q;
        if (Q == null) return P;

        BigInteger x1 = P[0], y1 = P[1], x2 = Q[0], y2 = Q[1];
        BigInteger m;

        if (P[0].equals(Q[0]) && P[1].equals(Q[1])) {
            m = (BigInteger.valueOf(3).multiply(x1.pow(2)).add(a))
                    .multiply(mulInv(BigInteger.valueOf(2).multiply(y1), p)).mod(p);
        } else {
            m = (y2.subtract(y1)).multiply(mulInv(x2.subtract(x1), p)).mod(p);
        }

        BigInteger x3 = m.pow(2).subtract(x1).subtract(x2).mod(p);
        BigInteger y3 = m.multiply(x1.subtract(x3)).subtract(y1).mod(p);

        return new BigInteger[]{x3, y3};
    }

    // Modular multiplicative inverse
    public static BigInteger mulInv(BigInteger a, BigInteger p) {
        BigInteger m0 = p, t, q;
        BigInteger x0 = BigInteger.ZERO, x1 = BigInteger.ONE;

        if (p.equals(BigInteger.ONE)) return BigInteger.ZERO;

        while (a.compareTo(BigInteger.ONE) > 0) {
            q = a.divide(p);
            t = p;
            p = a.mod(p);
            a = t;
            t = x0;
            x0 = x1.subtract(q.multiply(x0));
            x1 = t;
        }

        if (x1.compareTo(BigInteger.ZERO) < 0) x1 = x1.add(m0);

        return x1;
    }

    // Main function to perform elliptic curve key exchange
    public static void ellipticCurveKeyExchange() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter curve parameter 'a': ");
        BigInteger a = scanner.nextBigInteger();
        System.out.println("Enter curve parameter 'b': ");
        BigInteger b = scanner.nextBigInteger();
        System.out.println("Enter prime number 'p': ");
        BigInteger p = scanner.nextBigInteger();

        List<BigInteger[]> points = keygen(a, b, p);
        System.out.println("Generated points on the elliptic curve:");
        for (int i = 0; i < points.size(); i++) {
            BigInteger[] point = points.get(i);
            System.out.println(i + ": (x = " + point[0] + ", y = " + point[1] + ")");
        }

        System.out.println("Select a base point index (G): ");
        int gIndex = scanner.nextInt();
        BigInteger[] G = points.get(gIndex);

        System.out.println("User A, enter your private key: ");
        BigInteger nA = scanner.nextBigInteger();
        BigInteger[] PA = scalarMultiply(G, nA, a, p);
        System.out.println("User A's public key (PA): (x = " + PA[0] + ", y = " + PA[1] + ")");

        System.out.println("User B, enter your private key: ");
        BigInteger nB = scanner.nextBigInteger();
        BigInteger[] PB = scalarMultiply(G, nB, a, p);
        System.out.println("User B's public key (PB): (x = " + PB[0] + ", y = " + PB[1] + ")");

        BigInteger[] sharedKeyA = scalarMultiply(PB, nA, a, p);
        BigInteger[] sharedKeyB = scalarMultiply(PA, nB, a, p);

        System.out.println("User A's computed shared key: (x = " + sharedKeyA[0] + ", y = " + sharedKeyA[1] + ")");
        System.out.println("User B's computed shared key: (x = " + sharedKeyB[0] + ", y = " + sharedKeyB[1] + ")");

        if (sharedKeyA[0].equals(sharedKeyB[0]) && sharedKeyA[1].equals(sharedKeyB[1])) {
            System.out.println("Key exchange successful! Both users share the same key.");
        } else {
            System.out.println("Key exchange failed! The keys do not match.");
        }
    }

    public static void main(String[] args) {
        ellipticCurveKeyExchange();
    }
}
