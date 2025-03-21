# Function to calculate the modular inverse using the Extended Euclidean Algorithm
def modinv(a, q):
    t, new_t = 0, 1
    r, new_r = q, a

    while new_r != 0:
        quotient = r // new_r
        t, new_t = new_t, t - quotient * new_t
        r, new_r = new_r, r - quotient * new_r

    if r > 1:
        raise ValueError(f"{a} has no inverse mod {q}")
    if t < 0:
        t = t + q

    return t

# Method to compute the public key y = g^x mod p
def compute_public_key(x, p, g):
    y = pow(g, x, p)
    print("Public key (Y) calculated:", y)
    return y

# Method to compute r = (g^k mod p) mod q
def compute_signature_r(k, p, q, g):
    r = pow(g, k, p) % q
    print("Calculated value of R:", r)
    return r

# Method to compute s = (k^-1 * (H(m) + x * r)) mod q
def compute_signature_s(k, hm, x, r, q):
    s_inv = modinv(k, q)  # k^-1 mod q using our own modinv function
    s = (s_inv * (hm + x * r)) % q
    print("Generated signature component (S):", s)
    return s

# Method to compute w = s^-1 mod q
def compute_inverse_s(s, q):
    w = modinv(s, q)  # s^-1 mod q using our own modinv function
    print("Computed inverse of S (W):", w)
    return w

# Method to compute u1 = (H(m) * w) mod q
def compute_u1(hm, w, q):
    u1 = (hm * w) % q
    print("Calculated U1 (intermediate value):", u1)
    return u1

# Method to compute u2 = (r * w) mod q
def compute_u2(r, w, q):
    u2 = (r * w) % q
    print("Calculated U2 (intermediate value):", u2)
    return u2

# Method to compute v = ((g^u1 * y^u2) mod p) mod q
def compute_verification_value(p, q, g, y, u1, u2):
    ans = pow(g, u1, p)
    print("Computed value of G^U1:", ans)
    ans1 = pow(y, u2, p)  
    print("Computed value of Y^U2:", ans1)
    v = ((ans * ans1) % p) % q
    print("Computed verification value (V):", v)
    return v

# Signature verification method
def verify_signature(r, v):
    if r == v:
        print("Signature successfully verified.")
    else:
        print("Signature verification failed.")

def hash_file(file_path):
    hash_func = hashlib.sha256()  # You can change the hashing algorithm if needed
    with open(file_path, "rb") as file:
        while chunk := file.read(4096):  # Read the file in chunks
            hash_func.update(chunk)
    hashed_value = int(hash_func.hexdigest(), 16)  # Convert hash to an integer
    print(f"Hash of the file (H(M)): {hashed_value}")
    return hashed_value

# Function to sign a file
def sign_file(file_path, p, q, g, x, k):
    # Hash the file content
    hm = hash_file(file_path)

    # Compute signature components
    r = compute_signature_r(k, p, q, g)
    s = compute_signature_s(k, hm, x, r, q)

    return r, s

# Input parameters from user
p = int(input("P value: "))
q = int(input("Q value: "))
hm = int(input("Hash of the message (H(M)): "))
g = int(input("G value: "))
x = int(input("Private key (X): "))
k = int(input("Random value (K): "))

# Compute public key
y = compute_public_key(x, p, g)

# Compute signature components
r = compute_signature_r(k, p, q, g)
s = compute_signature_s(k, hm, x, r, q)

# Compute intermediate values
w = compute_inverse_s(s, q)
u1 = compute_u1(hm, w, q)
u2 = compute_u2(r, w, q)

# Compute verification value and verify signature
v = compute_verification_value(p, q, g, y, u1, u2)
verify_signature(r, v)
