import matplotlib.pyplot as plt
from collections import Counter

filename = "data/data1.txt"

with open(filename, "r") as f:
    lines = f.read().splitlines()

values = [float(line.strip()) for line in lines if line.strip()]

counts = Counter(values)

sorted_values = sorted(counts.keys())
frequencies = [counts[val] for val in sorted_values]

plt.figure(figsize=(10, 6))

plt.bar(range(len(sorted_values)), frequencies, tick_label=sorted_values)

plt.xlabel("Value")
plt.ylabel("Frequency")
plt.title("U(1,7) m/s vecocity")

plt.xticks(rotation=45)

plt.tight_layout()
plt.show()
