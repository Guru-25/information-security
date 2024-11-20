# Global variable for alphabet list
ALPHA_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
M = len(ALPHA_LIST)

def find_gcd(a, b):
    """Find the Greatest Common Divisor of two numbers."""
    if a < b:
        a, b = b, a
    while b != 0:
        a, b = b, a % b
    return a

def extended_euclids(a, b, m):
    """Implementation of Extended Euclidean Algorithm."""
    t1, t2 = 0, 1
    if a < b:
        a, b = b, a
    while b != 0:
        q = a // b
        a, b = b, a % b
        t1, t2 = t2, t1 - q * t2
    return t1 + m if t1 < 0 else t1

def convert_str_mat(word, syllable):
    """Convert string to matrix."""
    # Pad with 'X' if needed
    while len(word) % syllable != 0:
        word += 'X'
    
    cols = len(word) // syllable
    result = [[0] * cols for _ in range(syllable)]
    k = 0
    
    for i in range(cols):
        for j in range(syllable):
            result[j][i] = ALPHA_LIST.index(word[k])
            k += 1
    
    return result

def convert_mat_str(mat):
    """Convert matrix to string."""
    text = ""
    for i in range(len(mat[0])):
        for j in range(len(mat)):
            text += ALPHA_LIST[mat[j][i] % M]
    return text

def find_mat_mul(mat_a, mat_b):
    """Multiply two matrices."""
    rows_a = len(mat_a)
    cols_a = len(mat_a[0])
    cols_b = len(mat_b[0])
    result = [[0] * cols_b for _ in range(rows_a)]
    
    for i in range(rows_a):
        for j in range(cols_b):
            for k in range(cols_a):
                result[i][j] += mat_a[i][k] * mat_b[k][j]
            result[i][j] %= M
    
    return result

def remove_copy_matrix(mat, i, j):
    """Remove row i and column j from matrix."""
    return [[mat[row][col] for col in range(len(mat)) if col != j]
            for row in range(len(mat)) if row != i]

def check_square_matrix(mat):
    """Check if matrix is square."""
    return all(len(row) == len(mat) for row in mat)

def find_determinant(mat):
    """Calculate determinant of matrix."""
    if not check_square_matrix(mat):
        return -1
    
    if len(mat) == 2:
        return (mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0]) % M
    
    det = 0
    for c in range(len(mat)):
        sign = 1 if c % 2 == 0 else -1
        det += sign * mat[0][c] * find_determinant(remove_copy_matrix(mat, 0, c))
        det %= M
    
    return det if det >= 0 else det + M

def find_adjoint(mat):
    """Find adjoint of matrix."""
    size = len(mat)
    adjoint = [[0] * size for _ in range(size)]
    
    if size == 2:
        adjoint[0][0] = mat[1][1]
        adjoint[1][1] = mat[0][0]
        adjoint[0][1] = -mat[0][1]
        adjoint[1][0] = -mat[1][0]
        return adjoint
    
    for i in range(size):
        for j in range(size):
            minor = remove_copy_matrix(mat, i, j)
            sign = 1 if (i + j) % 2 == 0 else -1
            adjoint[j][i] = (sign * find_determinant(minor)) % M
            if adjoint[j][i] < 0:
                adjoint[j][i] += M
    
    return adjoint

def scalar_multiply(scalar, mat):
    """Multiply matrix by scalar."""
    result = [[0] * len(mat[0]) for _ in range(len(mat))]
    for i in range(len(mat)):
        for j in range(len(mat[0])):
            result[i][j] = (scalar * mat[i][j]) % M
            if result[i][j] < 0:
                result[i][j] += M
    return result

def print_matrix(matrix):
    """Print matrix in readable format."""
    for row in matrix:
        print(" ".join(str(elem) for elem in row))

def encrypt(plaintext, key_matrix):
    """Encrypt plaintext using Hill cipher."""
    plaintext_matrix = convert_str_mat(plaintext, len(key_matrix))
    print("Plaintext Matrix:")
    print_matrix(plaintext_matrix)
    
    cipher_matrix = find_mat_mul(key_matrix, plaintext_matrix)
    print("Ciphertext Matrix:")
    print_matrix(cipher_matrix)
    
    return convert_mat_str(cipher_matrix)

def decrypt(ciphertext, key_matrix):
    """Decrypt ciphertext using Hill cipher."""
    det = find_determinant(key_matrix)
    if det == 0 or find_gcd(det, M) != 1:
        raise ValueError("Matrix is not invertible")
    
    det_inv = extended_euclids(det, M, M)
    adjoint_matrix = find_adjoint(key_matrix)
    inverse_matrix = scalar_multiply(det_inv, adjoint_matrix)
    
    print("Inverse Key Matrix:")
    print_matrix(inverse_matrix)
    
    ciphertext_matrix = convert_str_mat(ciphertext, len(inverse_matrix))
    plaintext_matrix = find_mat_mul(inverse_matrix, ciphertext_matrix)
    
    return convert_mat_str(plaintext_matrix).rstrip('X')

def known_plaintext_attack(pt, ct, size):
    """Perform known plaintext attack."""
    pt_matrix = convert_str_mat(pt, size)
    print("Plaintext Matrix:")
    print_matrix(pt_matrix)

    ct_matrix = convert_str_mat(ct, size)
    print("Ciphertext Matrix:")
    print_matrix(ct_matrix)

    det_ct = find_determinant(ct_matrix)
    print(f"Determinant of Ciphertext Matrix: {det_ct}")

    det_inv = extended_euclids(det_ct, M, M)
    print(f"Inverse of Determinant: {det_inv}")

    adj_ct = find_adjoint(ct_matrix)
    print("Adjoint of Ciphertext Matrix:")
    print_matrix(adj_ct)

    ct_inv = scalar_multiply(det_inv, adj_ct)
    print("Inverse of Ciphertext Matrix:")
    print_matrix(ct_inv)

    key_inv = find_mat_mul(pt_matrix, ct_inv)
    print("Inverse of Key Matrix:")
    print_matrix(key_inv)

    new_ciphertext = input("Enter the ciphertext to decrypt using the found key: ").upper()
    new_ciphertext_matrix = convert_str_mat(new_ciphertext, len(key_inv))
    new_plaintext_matrix = find_mat_mul(key_inv, new_ciphertext_matrix)
    print("Decrypted plaintext Matrix using the found key:")
    print_matrix(new_plaintext_matrix)
    plaintext = convert_mat_str(new_plaintext_matrix)
    print(f"PlainText: {plaintext}")

    return key_inv

def main():
    """Main function with menu interface."""
    while True:
        print("\nHill Cipher Menu")
        print("1. Encrypt")
        print("2. Decrypt")
        print("3. Known Plaintext Attack")
        print("4. Exit")
        
        try:
            choice = int(input("Enter your choice: "))
            
            if choice == 1:
                plaintext = input("Enter the plaintext: ").upper()
                size = int(input("Enter the size of the key matrix: "))
                print("Enter the key matrix:")
                key_matrix = [[int(input(f"Enter element [{i}][{j}]: ")) 
                             for j in range(size)] for i in range(size)]
                ciphertext = encrypt(plaintext, key_matrix)
                print(f"Ciphertext: {ciphertext}")
                
            elif choice == 2:
                ciphertext = input("Enter the ciphertext: ").upper()
                size = int(input("Enter the size of the key matrix: "))
                print("Enter the key matrix:")
                key_matrix = [[int(input(f"Enter element [{i}][{j}]: ")) 
                             for j in range(size)] for i in range(size)]
                plaintext = decrypt(ciphertext, key_matrix)
                print(f"Decrypted Plaintext: {plaintext}")
                
            elif choice == 3:
                pt = input("Enter the known plaintext: ").upper()
                ct = input("Enter the corresponding ciphertext: ").upper()
                size = int(input("Enter the size of the key matrix: "))
                known_plaintext_attack(pt, ct, size)
                
            elif choice == 4:
                print("Exiting...")
                break
                
            else:
                print("Invalid choice! Please select again.")
                
        except ValueError as e:
            print(f"Error: {e}")
            print("Please try again with valid input.")

if __name__ == "__main__":
    main()