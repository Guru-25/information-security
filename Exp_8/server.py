import socket
import random
from hashlib import sha256

def keygen(a, b, p):
    points = []
    for i in range(p):
        R = (i**3 + a * i + b) % p
        for j in range(p):
            L = (j**2) % p
            if L == R:
                points.append((i, j))
    return points

def scalar_multiply(P, n, a, p):
    result = None  # Point at infinity
    current = P
    while n > 0:
        if n % 2 == 1:
            result = point_add(result, current, a, p)
        current = point_add(current, current, a, p)
        n = n // 2
    return result

def point_add(P, Q, a, p):
    if P is None:
        return Q
    if Q is None:
        return P
    x1, y1 = P
    x2, y2 = Q
    if P == Q:
        m = (3 * x1**2 + a) * mulinv(2 * y1, p) % p
    else:
        m = (y2 - y1) * mulinv(x2 - x1, p) % p
    x3 = (m**2 - x1 - x2) % p
    y3 = (m * (x1 - x3) - y1) % p
    return x3, y3

def mulinv(a, b):
    m1, m2 = b, a
    t1, t2 = 0, 1
    while m2 != 0:
        q = m1 // m2
        m1, m2 = m2, m1 % m2
        t1, t2 = t2, t1 - q * t2
    if t1 < 0:
        t1 += b
    return t1


a = 24
b = 5
p = 2  # Example prime
points = keygen(a, b, p)

# Pick a base point G
G = points[0]

# Generate server's private key
nB = random.randint(1, p - 1)
PB = scalar_multiply(G, nB, a, p)
print(PB)

# Set up the server socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(('localhost', 65432))
server_socket.listen(1)
print("Server is listening on port 65432...")

client_socket, addr = server_socket.accept()
print(f"Client connected from {addr}")

# Send public parameters and PB to the client
client_socket.sendall(f"{a},{b},{p},{PB[0]},{PB[1]}".encode())

# Receive client's public key (PA)
data = client_socket.recv(1024).decode()
xA, yA = map(int, data.split(','))
PA = (xA, yA)
print(PA)

# Compute shared key: S = nB * PA
shared_key = scalar_multiply(PA, nB, a, p)
print(f"Shared secret (Server): {shared_key}")

client_socket.close()
server_socket.close()
