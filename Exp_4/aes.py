# S-box for SubBytes operation
sbox = [
    [0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76],
    [0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0],
    [0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15],
    [0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75],
    [0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84],
    [0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf],
    [0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8],
    [0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2],
    [0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73],
    [0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb],
    [0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79],
    [0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08],
    [0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a],
    [0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e],
    [0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf],
    [0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16]
]

# Round constants for key expansion
rcon = [0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36, 0x6C, 0xD8, 0xAB, 0x4D]

def get_column(matrix, col):
    """Extract a column from a matrix."""
    return [matrix[i][col] for i in range(4)]

def set_column(matrix, col, values):
    """Set a column in a matrix."""
    for i in range(4):
        matrix[i][col] = values[i]

def rot_word(word):
    """Rotate word operation for key expansion."""
    return word[1:] + word[:1]

def sub_bytes(word):
    """SubBytes operation for key expansion."""
    return [sbox[(x & 0xF0) >> 4][x & 0x0F] for x in word]

def xor_arrays(a, b):
    """XOR operation for two arrays."""
    return [x ^ y for x, y in zip(a, b)]

def print_key(key):
    """Print a key matrix."""
    for row in key:
        print(' '.join(f'{value:02X}' for value in row))

def get_initial_key_from_user():
    """Get the initial key from user input."""
    key = [[0] * 4 for _ in range(4)]
    print("Enter the initial key (16 hexadecimal values):")
    for i in range(4):
        for j in range(4):
            while True:
                try:
                    hex_value = input(f"Enter value for position [{i}][{j}]: ")
                    key[i][j] = int(hex_value, 16)
                    break
                except ValueError:
                    print("Invalid hexadecimal value. Please try again.")
    print("Initial key entered successfully.")
    return key

def generate_key(initial_key, rounds):
    """Generate round keys."""
    generated_keys = [[[0] * 4 for _ in range(4)] for _ in range(rounds + 1)]
    generated_keys[0] = [row[:] for row in initial_key]

    for round in range(1, rounds + 1):
        prev_key = generated_keys[round - 1]
        new_key = [[0] * 4 for _ in range(4)]

        # Generate first column of the new key
        last_col = get_column(prev_key, 3)
        rotated = rot_word(last_col)
        substituted = sub_bytes(rotated)
        xor_with_prev_first = xor_arrays(substituted, get_column(prev_key, 0))
        rcon_column = [rcon[round - 1], 0, 0, 0]
        first_new_column = xor_arrays(xor_with_prev_first, rcon_column)
        set_column(new_key, 0, first_new_column)

        # Generate other columns
        for col in range(1, 4):
            new_column = xor_arrays(get_column(new_key, col - 1), get_column(prev_key, col))
            set_column(new_key, col, new_column)

        generated_keys[round] = new_key

    return generated_keys

def generate_and_print_keys(initial_key, rounds):
    """Generate and print keys for specified number of rounds."""
    if initial_key is None:
        print("Please enter the initial key first.")
        return
    expanded_keys = generate_key(initial_key, rounds)
    for round in range(rounds + 1):
        print(f"\nRound {round} Key:")
        print_key(expanded_keys[round])

def main():
    """Main function with menu interface."""
    initial_key = None
    
    while True:
        print("\nAES Key Generation Menu:")
        print("1. Enter initial key")
        print("2. Generate keys for 10 rounds (AES-128)")
        print("3. Generate keys for 12 rounds (AES-192)")
        print("4. Generate keys for 14 rounds (AES-256)")
        print("5. Exit")
        
        try:
            choice = int(input("Enter your choice: "))
            
            if choice == 1:
                initial_key = get_initial_key_from_user()
            elif choice == 2:
                generate_and_print_keys(initial_key, 10)
            elif choice == 3:
                generate_and_print_keys(initial_key, 12)
            elif choice == 4:
                generate_and_print_keys(initial_key, 14)
            elif choice == 5:
                print("Exiting program.")
                break
            else:
                print("Invalid choice. Please try again.")
        except ValueError:
            print("Invalid input. Please enter a number.")

if __name__ == "__main__":
    main()