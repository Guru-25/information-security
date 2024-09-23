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

def elliptic_curve_key_exchange():
    a = int(input("Enter the curve parameter 'a': "))
    b = int(input("Enter the curve parameter 'b': "))
    p = int(input("Enter the prime number 'p': "))

    points = keygen(a, b, p)
    print("Generated points on the elliptic curve:")
    for i, point in enumerate(points):
        print(f"Point {i}: (x = {point[0]}, y = {point[1]})")

    g_index = int(input("Choose the index of the base point (G) from the list above: "))
    G = points[g_index]

    nA = int(input("User A: Enter your private key (nA): "))
    PA = scalar_multiply(G, nA, a, p)
    print(f"User A's public key (PA): (x = {PA[0]}, y = {PA[1]})")

    nB = int(input("User B: Enter your private key (nB): "))
    PB = scalar_multiply(G, nB, a, p)
    print(f"User B's public key (PB): (x = {PB[0]}, y = {PB[1]})")

    shared_key_A = scalar_multiply(PB, nA, a, p)
    shared_key_B = scalar_multiply(PA, nB, a, p)

    print(f"User A's calculated shared key: (x = {shared_key_A[0]}, y = {shared_key_A[1]})")
    print(f"User B's calculated shared key: (x = {shared_key_B[0]}, y = {shared_key_B[1]})")

    if shared_key_A == shared_key_B:
        print("Key exchange successful! Both users have the same shared key.")
    else:
        print("Key exchange failed! The keys do not match.")

elliptic_curve_key_exchange()

def string_to_point(message, points):
    index = hash(message) % len(points)
    return points[index]

def encrypt(message, G, Pa, k, a, b, p, points):
    Pm = string_to_point(message, points)  # Convert the message into a point
    C1 = scalar_multiply(G, k, a, p)  # C1 = kG
    kPa = scalar_multiply(Pa, k, a, p)  # kPa = k * Pa
    C2 = point_add(Pm, kPa, a, p)  # C2 = Pm + kPa
    return C1, C2

def elliptic_curve_encryption():
    a = int(input("Enter the curve parameter 'a': "))
    b = int(input("Enter the curve parameter 'b': "))
    p = int(input("Enter the prime number 'p': "))

    points = keygen(a, b, p)
    print("Generated points on the elliptic curve:")
    for i, point in enumerate(points):
        print(f"Point {i}: (x = {point[0]}, y = {point[1]})")

    g_index = int(input("Choose the index of the base point (G) from the list above: "))
    G = points[g_index]

    pa_index = int(input("Enter the recipient's public key index: "))
    Pa = points[pa_index]

    k = random.randint(1, p-1)
    print(f"Random number chosen for encryption (k): {k}")

    message = input("Enter the message to encrypt: ")
    C1, C2 = encrypt(message, G, Pa, k, a, b, p, points)
    print(f"Encrypted message: C1 = (x = {C1[0]}, y = {C1[1]}), C2 = (x = {C2[0]}, y = {C2[1]})")

elliptic_curve_encryption()
