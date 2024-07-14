import os
import time
import matplotlib.pyplot as plt

ALPHABET = 'abcdefghijklmnopqrstuvwxyz'

def encrypt(plain_text, shift_key):
    plain_text = plain_text.lower()
    cipher_text = ""
    for c in plain_text:
        if c in ALPHABET:
            char_position = ALPHABET.index(c)
            key_val = (shift_key + char_position) % 26
            replace_val = ALPHABET[key_val]
            cipher_text += replace_val
        else:
            cipher_text += c
    return cipher_text

def generate_file(size_bytes):
    filename = f"test_file_{size_bytes}.txt"
    with open(filename, 'w') as f:
        f.write('a' * size_bytes)
    return filename

def encrypt_file(filename, shift_key):
    with open(filename, 'r') as f:
        data = f.read()
    
    start_time = time.time()
    encrypted_data = encrypt(data, shift_key)
    end_time = time.time()
    
    encryption_time = end_time - start_time
    return encryption_time

# Use a fixed shift key for consistency
shift_key = 3

file_sizes = [125000, 250000, 375000, 500000, 625000, 750000, 875000, 1000000, 1125000, 1250000]  # File sizes in bytes
encryption_times = []

for size in file_sizes:
    filename = generate_file(size)
    encryption_time = encrypt_file(filename, shift_key)
    encryption_times.append(encryption_time)
    os.remove(filename)  # Clean up the test file
    print(f"Encrypted file of size {size} bytes in {encryption_time:.6f} seconds")

# Plot the results
plt.figure(figsize=(10, 6))
plt.plot(file_sizes, encryption_times, marker='o')
plt.xscale('log')
plt.yscale('log')
plt.xlabel('File Size (bytes)')
plt.ylabel('Encryption Time (seconds)')
plt.title('File Size vs Encryption Time (Caesar Cipher)')
plt.grid(True)
plt.show()

# Print the results
for size, time in zip(file_sizes, encryption_times):
    print(f"File size: {size} bytes, Encryption time: {time:.6f} seconds")