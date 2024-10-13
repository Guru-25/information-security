import struct

# Rotate Left Function with Sample Input/Output
def rotate_left(value, bits):
    result = ((value << bits) | (value >> (32 - bits))) & 0xFFFFFFFF
    print(f"rotate_left({hex(value)}, {bits}) -> {hex(result)}")
    return result

# f(t, B, C, D) Function with Sample Input/Output
def f(t, B, C, D):
    if 0 <= t <= 19:
        result = (B & C) | ((~B) & D)
    elif 20 <= t <= 39:
        result = B ^ C ^ D
    elif 40 <= t <= 59:
        result = (B & C) | (B & D) | (C & D)
    else:
        result = B ^ C ^ D
    print(f"f({t}, {hex(B)}, {hex(C)}, {hex(D)}) -> {hex(result)}")
    return result

# K(t) Function with Sample Input/Output
def K(t):
    if 0 <= t <= 19:
        result = 0x5A827999
    elif 20 <= t <= 39:
        result = 0x6ED9EBA1
    elif 40 <= t <= 59:
        result = 0x8F1BBCDC
    else:
        result = 0xCA62C1D6
    print(f"K({t}) -> {hex(result)}")
    return result

# Pad Message Function with Sample Input/Output
def pad_message(message):
    message_length = len(message)
    message_bits_length = message_length * 8
    padding_length = (56 - (message_length % 64)) if (message_length % 64) < 56 else (120 - (message_length % 64))

    # Padding starts with a single 1 bit followed by 0 bits
    padding = b'\x80' + b'\x00' * (padding_length - 1)

    # Append original length as a 64-bit big-endian integer at the end
    length_padding = struct.pack('>Q', message_bits_length)

    padded_message = message + padding + length_padding
    print(f"pad_message({message}) -> {padded_message}")
    return padded_message

# SHA-1 Step Function with Sample Input/Output
def sha1_step(h, W_t, K_t, t):
    A, B, C, D, E = h

    temp = (rotate_left(A, 5) + f(t, B, C, D) + E + W_t + K_t) & 0xFFFFFFFF
    E = D
    D = C
    C = rotate_left(B, 30)
    B = A
    A = temp

    h[0], h[1], h[2], h[3], h[4] = A, B, C, D, E
    print(f"sha1_step -> h = {[hex(x) for x in h]}")

# Process Block Function with Sample Input/Output
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
    
    print(f"process_block -> h = {[hex(x) for x in h]}")

# Convert Hash State to Hex String with Sample Input/Output
def to_hex_string(h):
    result = ''.join(f'{value:08x}' for value in h)
    print(f"to_hex_string({[hex(x) for x in h]}) -> {result}")
    return result

# Sample Input/Output for each function
if __name__ == "__main__":
    # Sample input for rotate_left
    # rotate_left(0x12345678, 4)
    
    # Sample input for f
    # f(10, 0x5A, 0x6B, 0x7C)
    
    # Sample input for K
    # K(10)
    
    # Sample input for pad_message
    message = b"abc"
    padded_message = pad_message(message)
    
    # Sample input for sha1_step
    h = [0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0]
    W_t = 0x12345678
    K_t = 0x5A827999
    # sha1_step(h, W_t, K_t, 10)
    print("1")
    print(padded_message[:64])
    print("1")
    # Sample input for process_block
    # process_block(padded_message[:64], h)
    
    # Sample input for to_hex_string
    to_hex_string(h)
    
    # Full SHA-1 computation as previously shown
    # input_text = "abc"
    # message = input_text.encode('utf-8')
    # padded_message = pad_message(message)
    # h = [0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0]

    # for i in range(0, len(padded_message), 64):
    #     block = padded_message[i:i+64]
    #     process_block(block, h)

    # print("SHA-1 Hash of the input:", to_hex_string(h))
