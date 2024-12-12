import matplotlib.pyplot as plt


def plot_radius_vs_metrics(radius, throughput, packet_loss):
    plt.figure(figsize=(12, 6))

    # Throughput vs. Radius
    plt.subplot(1, 2, 1)
    plt.plot(radius, throughput, marker="o", label="Throughput", color="blue")
    plt.title("Throughput vs. Radius")
    plt.xlabel("Radius")
    plt.ylabel("Throughput")
    plt.grid()
    plt.legend()

    # Packet Loss vs. Radius
    plt.subplot(1, 2, 2)
    plt.plot(radius, packet_loss, marker="o", label="Packet Loss", color="red")
    plt.title("Packet Loss vs. Radius")
    plt.xlabel("Radius")
    plt.ylabel("Packet Loss Probability")
    plt.grid()
    plt.legend()

    plt.tight_layout()
    plt.show()


if __name__ == "__main__":
    radius = [6000, 7000, 8000, 9000, 10000, 11000]
    throughput = [0.2198, 0.2375, 0.2464, 0.2528, 0.25634, 0.2571]
    packet_loss = [0.5626, 0.5255, 0.5112, 0.4961, 0.4894, 0.4886]

    plot_radius_vs_metrics(radius, throughput, packet_loss)
