import socket
import random

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

# Set up the client socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(('localhost', 65432))
print("Connected to server.")

# Receive public parameters and PB from the server
data = client_socket.recv(1024).decode()
a, b, p, xB, yB = map(int, data.split(','))
PB = (xB, yB)
print(a)
print(b)
print(p)

# Generate client's private key
nA = random.randint(1, p - 1)
points = keygen(a, b, p)
G = points[0]

# Compute PA
PA = scalar_multiply(G, nA, a, p)
print(PA)
client_socket.sendall(f"{PA[0]},{PA[1]}".encode())

# Compute shared key: S = nA * PB
shared_key = scalar_multiply(PB, nA, a, p)
print(f"Shared secret (Client): {shared_key}")

client_socket.close()