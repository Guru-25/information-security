import java.util.*;
import java.math.BigInteger;

class DSS {
    BigInteger g;
    BigInteger y;
    BigInteger r;
    BigInteger s;
    BigInteger w;
    BigInteger u1;
    BigInteger u2;
    BigInteger v;

    // Method to compute the public key y = g^x mod p
    void computePublicKey(BigInteger x, BigInteger p, BigInteger g) {
        y = g.modPow(x, p);
        System.out.println("Public key (Y) calculated: " + y);
    }

    // Method to compute r = (g^k mod p) mod q
    void computeSignatureR(BigInteger k, BigInteger p, BigInteger q, BigInteger g) {
        r = g.modPow(k, p).mod(q);
        System.out.println("Calculated value of R: " + r);
    }

    // Method to compute s = (k^-1 * (H(m) + x * r)) mod q
    void computeSignatureS(BigInteger k, BigInteger hm, BigInteger x, BigInteger q) {
        s = k.modInverse(q);  // k^-1 mod q
        BigInteger j = hm.add(x.multiply(r));
        s = s.multiply(j).mod(q);
        System.out.println("Generated signature component (S): " + s);
    }

    // Method to compute w = s^-1 mod q
    void computeInverseS(BigInteger q) {
        w = s.modInverse(q);  // s^-1 mod q
        System.out.println("Computed inverse of S (W): " + w);
    }

    // Method to compute u1 = (H(m) * w) mod q
    void computeU1(BigInteger hm, BigInteger q) {
        u1 = hm.multiply(w).mod(q);
        System.out.println("Calculated U1 (intermediate value): " + u1);
    }

    // Method to compute u2 = (r * w) mod q
    void computeU2(BigInteger q) {
        u2 = r.multiply(w).mod(q);
        System.out.println("Calculated U2 (intermediate value): " + u2);
    }

    // Method to compute v = ((g^u1 * y^u2) mod p) mod q
    void computeVerificationValue(BigInteger p, BigInteger q, BigInteger g) {
        BigInteger ans = modularExponentiation(g, u1, p);
        BigInteger ans1 = modularExponentiation(y, u2, p);
        v = ans.multiply(ans1).mod(p).mod(q);
        System.out.println("Computed verification value (V): " + v);
    }

    // Signature verification method
    void verifySignature() {
        if (r.equals(v)) {
            System.out.println("Signature successfully verified.");
        } else {
            System.out.println("Signature verification failed.");
        }
    }

    // Method to compute base^exponent % mod using square-and-multiply method
    BigInteger modularExponentiation(BigInteger base, BigInteger exponent, BigInteger mod) {
        BigInteger result = BigInteger.ONE;
        base = base.mod(mod);  // Ensure base < mod

        if (base.equals(BigInteger.ZERO)) return BigInteger.ZERO;  // If base is 0
        while (exponent.compareTo(BigInteger.ZERO) > 0) {
            // If exponent is odd, multiply base with result
            if (exponent.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
                result = result.multiply(base).mod(mod);
            }
            // Right shift the exponent and square the base
            exponent = exponent.divide(BigInteger.TWO);
            base = base.multiply(base).mod(mod);
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner ob = new Scanner(System.in);

        // Input parameters from user
        System.out.print("P value:");
        BigInteger p = new BigInteger(ob.next());

        System.out.print("Q value:");
        BigInteger q = new BigInteger(ob.next());

        System.out.print("Hash of the message (H(M)):");
        BigInteger hm = new BigInteger(ob.next());

        System.out.print("G value:");
        BigInteger g = new BigInteger(ob.next());

        System.out.print("Private key (X):");
        BigInteger x = new BigInteger(ob.next());

        System.out.print("Random value (K):");
        BigInteger k = new BigInteger(ob.next());

        // Create DSS object and calculate components
        DSS dss = new DSS();
        dss.computePublicKey(x, p, g);         // Public key calculation
        dss.computeSignatureR(k, p, q, g);     // Signature component r
        dss.computeSignatureS(k, hm, x, q);    // Signature component s
        dss.computeInverseS(q);                // Inverse of s
        dss.computeU1(hm, q);                  // Intermediate value u1
        dss.computeU2(q);                      // Intermediate value u2
        dss.computeVerificationValue(p, q, g); // Verification value v

        // Verify the signature
        dss.verifySignature();
    }
}
