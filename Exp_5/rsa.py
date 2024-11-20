from math import gcd
import sys

def is_prime(number):
    """Check if a number is prime using a probabilistic primality test."""
    if number < 2:
        return False
    # Simple primality test for demonstration
    for i in range(2, int(number ** 0.5) + 1):
        if number % i == 0:
            return False
    return True

def mod_inverse(e, phi):
    """Calculate the modular multiplicative inverse of e (mod phi)."""
    def extended_gcd(a, b):
        if a == 0:
            return b, 0, 1
        gcd, x1, y1 = extended_gcd(b % a, a)
        x = y1 - (b // a) * x1
        y = x1
        return gcd, x, y

    gcd, x, _ = extended_gcd(e, phi)
    if gcd != 1:
        raise ValueError("Modular inverse does not exist")
    return x % phi

def main():
    n = None
    e = None
    d = None

    while True:
        print("\nMenu:")
        print("1. Key Generation")
        print("2. Encryption")
        print("3. Decryption")
        print("4. Exit")
        
        try:
            choice = int(input("Choose an option: "))
        except ValueError:
            print("Please enter a valid number")
            continue

        if choice == 1:  # Key Generation
            try:
                p = int(input("Enter prime p (up to 100 digits): "))
                q = int(input("Enter prime q (up to 100 digits): "))

                if not is_prime(p) or not is_prime(q):
                    print("Both p and q must be prime numbers.")
                    continue

                n = p * q
                phi = (p - 1) * (q - 1)

                print(f"Modulus n: {n}")
                e = int(input("Enter public key e (should be coprime with phi): "))

                # Validate e
                if gcd(e, phi) == 1:
                    d = mod_inverse(e, phi)
                    print(f"Public key (e, n): ({e}, {n})")
                    print(f"Private key (d, n): ({d}, {n})")
                else:
                    print("Invalid public key e. It must be coprime with phi.")
            except ValueError as ve:
                print(f"Invalid input: {ve}")

        elif choice == 2:  # Encryption
            if None in (n, e):
                print("Please generate keys first.")
                continue

            message = input("Enter the plaintext message as a string: ")
            cipher_text = []
            
            for char in message:
                # Convert character to integer and encrypt
                message_int = ord(char)
                encrypted_char = pow(message_int, e, n)  # Using built-in modular exponentiation
                cipher_text.append(str(encrypted_char))
            
            print("Cipher text:", " ".join(cipher_text))

        elif choice == 3:  # Decryption
            if None in (n, d):
                print("Please generate keys first.")
                continue

            try:
                encrypted_message = input("Enter the encrypted message as integers separated by spaces: ")
                encrypted_parts = encrypted_message.split()
                
                decrypted_message = ""
                for part in encrypted_parts:
                    encrypted_char = int(part)
                    decrypted_char_int = pow(encrypted_char, d, n)  # Using built-in modular exponentiation
                    decrypted_message += chr(decrypted_char_int)
                
                print("Decrypted message:", decrypted_message)
            except ValueError:
                print("Invalid input. Please enter space-separated integers.")

        elif choice == 4:  # Exit
            print("Exiting...")
            sys.exit(0)

        else:
            print("Invalid option. Please try again.")

if __name__ == "__main__":
    main()