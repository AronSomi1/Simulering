import matplotlib.pyplot as plt


def plot_results(intensity, throughput, packet_loss):

    plt.figure(figsize=(12, 6))

    # Throughput Plot
    plt.subplot(1, 2, 1)
    plt.plot(intensity, throughput, label="Throughput", marker="o")
    plt.title("Throughput vs. Traffic Intensity")
    plt.xlabel("Traffic Intensity")
    plt.ylabel("Throughput")
    plt.grid()
    plt.legend()

    # Packet Loss Plot
    plt.subplot(1, 2, 2)
    plt.plot(
        intensity, packet_loss, label="Packet Loss Probability", marker="o", color="red"
    )
    plt.title("Packet Loss Probability vs. Traffic Intensity")
    plt.xlabel("Traffic Intensity")
    plt.ylabel("Packet Loss Probability")
    plt.grid()
    plt.legend()

    plt.tight_layout()
    plt.show()


if __name__ == "__main__":
    intensity = [1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000]
    throughput = [0.84, 0.74, 0.65, 0.59, 0.54, 0.49, 0.46, 0.43, 0.41, 0.38]
    packet_loss = [0.16, 0.26, 0.35, 0.41, 0.46, 0.51, 0.54, 0.57, 0.59, 0.62]

    x1 = [0.181, 0.279, 0.331, 0.357, 0.368, 0.372, 0.365, 0.359, 0.351, 0.336]
    x2 = [0.121, 0.218, 0.291, 0.368, 0.431, 0.474, 0.527, 0.565, 0.597, 0.635]
    y = [1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000]

    plot_results(intensity, throughput, packet_loss)
    plot_results(y, x1, x2)
