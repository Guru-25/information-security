import matplotlib.pyplot as plt

# Data from Java output
file_sizes = [125000, 250000, 375000, 500000, 625000, 750000, 875000, 1000000, 1125000, 1250000]
encryption_times = [0.056372, 0.018884, 0.049314, 0.031392, 0.040121, 0.041282, 0.039419, 0.048721, 0.055215, 0.054324]

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

# Add data labels
for i, txt in enumerate(encryption_times):
    plt.annotate(f'{txt:.6f}', (file_sizes[i], encryption_times[i]), textcoords="offset points", xytext=(0,10), ha='center')

plt.tight_layout()
plt.show()

# Print the results
for size, time in zip(file_sizes, encryption_times):
    print(f"File size: {size} bytes, Encryption time: {time:.6f} seconds")