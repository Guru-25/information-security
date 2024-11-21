ALPHABET = "abcdefghijklmnopqrstuvwxyz"
FREQUENT_ALPHABET = "aeioutnshrdlcmwfgypbvkjxqz"

def encrypt(plain_text, shift_key):
    """Encrypt a plain text using Caesar cipher with the given shift key."""
    plain_text = plain_text.lower()
    cipher_text = ""
    
    for char in plain_text:
        if char in ALPHABET:
            char_position = ALPHABET.index(char)
            key_val = (shift_key + char_position) % 26
            replace_val = ALPHABET[key_val]
            cipher_text += replace_val
        else:
            cipher_text += char
            
    return cipher_text

def decrypt(cipher_text, shift_key):
    """Decrypt a cipher text using Caesar cipher with the given shift key."""
    cipher_text = cipher_text.lower()
    plain_text = ""
    
    for char in cipher_text:
        if char in ALPHABET:
            char_position = ALPHABET.index(char)
            key_val = (char_position - shift_key + 26) % 26
            replace_val = ALPHABET[key_val]
            plain_text += replace_val
        else:
            plain_text += char
            
    return plain_text

def brute_force_attack(cipher_text):
    """Perform a brute force attack by trying all possible shift keys."""
    print("Brute Force Attack Results:")
    for i in range(26):
        plain_text = decrypt(cipher_text, i)
        print(f"Key {i}: {plain_text}")

def frequency_analysis_attack(cipher_text):
    """Perform a frequency analysis attack based on letter frequency."""
    print("Frequency Analysis Attack Results:")
    
    # Create frequency map
    freq_map = {}
    for char in cipher_text.lower():
        if char in ALPHABET:
            freq_map[char] = freq_map.get(char, 0) + 1
    
    # Sort by frequency
    sorted_freq = sorted(freq_map.items(), key=lambda x: x[1], reverse=True)
    
    if not sorted_freq:
        print("No letters found in cipher text")
        return
        
    most_frequent_char = sorted_freq[0][0]
    print(f"Most frequent character in cipher text: {most_frequent_char}")
    
    print("Possible decryptions (assuming most frequent letter in plain text):")
    for assumed_most_frequent in FREQUENT_ALPHABET:
        estimated_shift = (ALPHABET.index(most_frequent_char) - 
                         ALPHABET.index(assumed_most_frequent) + 26) % 26
        decrypted_text = decrypt(cipher_text, estimated_shift)
        print(f"{assumed_most_frequent} - Key {estimated_shift}: {decrypted_text}")

def main():
    while True:
        print("\n1 for Encryption")
        print("2 for Decryption")
        print("3 for Brute Force Attack")
        print("4 for Frequency Analysis Attack")
        print("5 for Exit")
        
        # try:
        choice = input("Enter an option: ")
        
        if choice == "1":
            plain_text = input("Enter a plain text: ")
            shift_key = int(input("Enter the shift key: "))
            cipher_text = encrypt(plain_text, shift_key)
            print(f"Encrypted text: {cipher_text}")
            
        elif choice == "2":
            cipher_text = input("Enter a cipher text: ")
            shift_key = int(input("Enter the shift key: "))
            plain_text = decrypt(cipher_text, shift_key)
            print(f"Decrypted text: {plain_text}")
            
        elif choice == "3":
            cipher_text = input("Enter a cipher text: ")
            brute_force_attack(cipher_text)
            
        elif choice == "4":
            cipher_text = input("Enter a cipher text: ")
            frequency_analysis_attack(cipher_text)
            
        elif choice == "5":
            print("Exiting...")
            break
            
        else:
            print("Invalid option")
                
        # except ValueError as e:
        #     print(f"Invalid input: {e}")
        # except Exception as e:
        #     print(f"An error occurred: {e}")

# if __name__ == "__main__":
main()