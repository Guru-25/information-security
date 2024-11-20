import hashlib
import time
import itertools

CHAR_SET = "0123456789abcdefghijklmnopqrstuvwxyz"
dictionary = {}
password_table = {}

# Menu-driven loop
def main():
    while True:
        print("\nDictionary Attack Lab")
        print("1. Create Dictionary")
        print("2. Generate Password Table")
        print("3. Display Password Table")
        print("4. Launch Dictionary Attack")
        print("0. Exit")
        choice = input("Enter your choice: ")
        if choice == "1":
            create_dictionary()
        elif choice == "2":
            generate_password_table()
        elif choice == "3":
            display_password_table()
        elif choice == "4":
            launch_dictionary_attack()
        elif choice == "0":
            print("Exiting...")
            break
        else:
            print("Invalid choice. Please try again.")

# Generates words of lengths 2, 3, and 4.
def create_dictionary():
    global dictionary
    dictionary.clear()
    with open("dictionary_generation_times.txt", "w") as writer:
        for length in range(2, 5):
            start_time = time.time()
            generate_words("", length)
            end_time = time.time()
            time_elapsed = (end_time - start_time) * 1000  # convert to milliseconds
            writer.write(f"{length},{time_elapsed}\n")
            print(f"Length {length} words generated in {time_elapsed:.2f} ms")
    print(f"Dictionary created with {len(dictionary)} words.")

# Recursive method to generate words
def generate_words(prefix, length):
    if length == 0:
        hash = sha256(prefix)
        dictionary[hash] = prefix
        return
    for i in range(len(CHAR_SET)):
        generate_words(prefix + CHAR_SET[i], length - 1)

# Collects usernames and passwords, stores hashes
def generate_password_table():
    global password_table
    password_table.clear()
    for i in range(1, 11):
        username = input(f"Enter username {i}: ")
        password = input(f"Enter password for {username}: ")
        hash = sha256(password)
        password_table[username] = hash
    print("Password table generated for 10 users.")

# Displays the password table
def display_password_table():
    print("\nPassword Table:")
    for username, hash in password_table.items():
        print(f"Username: {username}, Password Hash: {hash}")

# Performs the dictionary attack
def launch_dictionary_attack():
    username = input("Enter username to attack: ")
    hash = password_table.get(username)
    if hash is None:
        print("Username not found in the password table.")
        return
    password = dictionary.get(hash)
    if password:
        print(f"Password found for {username}: {password}")
    else:
        print("Password not found in the dictionary.")

# Computes SHA-256 hash of the input string
def sha256(input_str):
    return hashlib.sha256(input_str.encode()).hexdigest()

if __name__ == "__main__":
    main()
