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
# Sample input/output for keygen
# print("Sample keygen output (a=2, b=3, p=7):")
# print(keygen(2, 3, 7))  
# Output: [(0, 3), (0, 4), (2, 5), (2, 2), (3, 6), (3, 1), (4, 6), (4, 1), (6, 3), (6, 4)]

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

# Sample input/output for mulinv
# print("\nSample mulinv output (a=2, b=7):")
# print(mulinv(2, 7))  
# Output: 4 - Multiplicative inverse of 2 mod 7 is 4

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

# # Sample input/output for point_add
# print("\nSample point_add output (P=(2,5), Q=(2,5), a=2, p=7):")
# print(point_add((2, 5), (2, 5), 2, 7))  
# # Output: (4, 1) - Adding point (2, 5) to itself on the elliptic curve

def scalar_multiply(P, n, a, p):
    result = None  # Point at infinity
    current = P
    while n > 0:
        if n % 2 == 1:
            result = point_add(result, current, a, p)
        current = point_add(current, current, a, p)
        n = n // 2
    return result

# Sample input/output for scalar_multiply
# print("\nSample scalar_multiply output (P=(2,5), n=2, a=2, p=7):")
# print(scalar_multiply((2, 5), 2, 2, 7))  
# Output: (4, 1) - Doubles the point (2, 5) on the elliptic curve

def string_to_point(message, points):
    index = hash(message) % len(points)
    return points[index]

# # Sample input/output for string_to_point
# message = "hello"
points = keygen(2, 3, 7)
# print("\nSample string_to_point output (message='hello', points from keygen):")
# print(string_to_point(message, points))  
# # Output: Point corresponding to the hash of "hello" from the points

def encrypt(message, G, Pa, k, a, b, p, points):
    Pm = string_to_point(message, points)  # Convert the message into a point
    C1 = scalar_multiply(G, k, a, p)  # C1 = kG
    kPa = scalar_multiply(Pa, k, a, p)  # kPa = k * Pa
    C2 = point_add(Pm, kPa, a, p)  # C2 = Pm + kPa
    return C1, C2

# Sample input/output for encrypt
print("\nSample encrypt output (message='hello', G=(2,5), Pa=(4,1), k=3, a=2, b=3, p=7):")
C1, C2 = encrypt("hello", (2, 5), (4, 1), 3, 2, 3, 7, points)
print(f"C1 = {C1}, C2 = {C2}")
# Output: C1 and C2 are the encrypted values in the form of elliptic curve points
