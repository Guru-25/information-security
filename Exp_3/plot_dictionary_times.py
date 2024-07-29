import matplotlib.pyplot as plt

lengths = []
times = []

with open('dictionary_generation_times.txt', 'r') as file:
    for line in file:
        length, time = map(int, line.strip().split(','))
        lengths.append(length)
        times.append(time)

plt.figure(figsize=(10, 6))
plt.plot(lengths, times, marker='o')
plt.title('Password Generation Time vs Length')
plt.xlabel('Password Length')
plt.ylabel('Generation Time (ms)')
plt.grid(True)
plt.savefig('password_generation_time_plot.png')
plt.show()