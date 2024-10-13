import struct

def rotate_left(value, bits):
    return ((value << bits) | (value >> (32 - bits))) & 0xFFFFFFFF

def f(t, B, C, D):
    if 0 <= t <= 19:
        return (B & C) | ((~B) & D)
    elif 20 <= t <= 39:
        return B ^ C ^ D
    elif 40 <= t <= 59:
        return (B & C) | (B & D) | (C & D)
    else:
        return B ^ C ^ D

def K(t):
    if 0 <= t <= 19:
        return 0x5A827999
    elif 20 <= t <= 39:
        return 0x6ED9EBA1
    elif 40 <= t <= 59:
        return 0x8F1BBCDC
    else:
        return 0xCA62C1D6

def pad_message(message):
    message_length = len(message)
    message_bits_length = message_length * 8
    padding_length = (56 - (message_length % 64)) if (message_length % 64) < 56 else (120 - (message_length % 64))

    # Padding starts with a single 1 bit followed by 0 bits
    padding = b'\x80' + b'\x00' * (padding_length - 1)

    # Append original length as a 64-bit big-endian integer at the end
    length_padding = struct.pack('>Q', message_bits_length)

    return message + padding + length_padding


def sha1_step(h, W_t, K_t, t):
    A, B, C, D, E = h

    temp = (rotate_left(A, 5) + f(t, B, C, D) + E + W_t + K_t) & 0xFFFFFFFF
    E = D
    D = C
    C = rotate_left(B, 30)
    B = A
    A = temp

    h[0], h[1], h[2], h[3], h[4] = A, B, C, D, E

def process_block(block, h):
    W = [0] * 80

    # Prepare the message schedule W_t
    for i in range(16):
        W[i] = struct.unpack('>I', block[i * 4:(i + 1) * 4])[0]

    for t in range(16, 80):
        W[t] = rotate_left(W[t - 3] ^ W[t - 8] ^ W[t - 14] ^ W[t - 16], 1)

    temp_hash = h[:]

    # Perform 80 rounds
    for t in range(80):
        sha1_step(temp_hash, W[t], K(t), t)

    for i in range(5):
            h[i] = (h[i] + temp_hash[i]) & 0xFFFFFFFF

def to_hex_string(h):
    return ''.join(f'{value:08x}' for value in h)

input_text = input("Enter the text to hash using SHA-1: ")
message = input_text.encode('utf-8')

padded_message = pad_message(message)

h = [0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0]

for i in range(0, len(padded_message), 64):
    block = padded_message[i:i+64]
    process_block(block, h)

print("SHA-1 Hash of the input:", to_hex_string(h))
