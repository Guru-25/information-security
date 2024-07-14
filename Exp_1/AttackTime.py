import matplotlib.pyplot as plt

sample_sizes = [125000, 250000, 375000, 500000, 625000, 750000, 875000, 1000000, 1125000, 1250000]
brute_force_times = [282.20, 539.10, 826.40, 1091.90, 1397.10, 1666.30, 1919.10, 2203.90, 2523.80, 2869.60]
frequency_analysis_times = [286.10, 551.80, 851.20, 1113.90, 1442.90, 1724.90, 1989.80, 2236.40, 2529.80, 2925.70]

plt.figure(figsize=(12, 6))
plt.plot(sample_sizes, brute_force_times, label='Brute Force')
plt.plot(sample_sizes, frequency_analysis_times, label='Frequency Analysis')
plt.xscale('log')
plt.yscale('log')
plt.xlabel('Sample Size')
plt.ylabel('Time (ms)')
plt.title('Caesar Cipher Attack Performance (Russian)')
plt.legend()
plt.grid(True)
plt.show()