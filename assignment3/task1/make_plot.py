import matplotlib.pyplot as plt


def plot_results(
    intensity,
    throughput_strategy_1,
    throughput_strategy_2,
    packet_loss_strategy_1,
    packet_loss_strategy_2,
):
    plt.figure(figsize=(12, 6))

    # Throughput Plot
    plt.subplot(1, 2, 1)
    plt.plot(
        intensity,
        throughput_strategy_1,
        label="Throughput Strategy 1",
        marker="o",
        color="blue",
    )
    plt.plot(
        intensity,
        throughput_strategy_2,
        label="Throughput Strategy 2",
        marker="s",
        color="green",
    )
    plt.title("Throughput vs. Traffic Intensity")
    plt.xlabel("Traffic Intensity")
    plt.ylabel("Throughput")
    plt.grid()
    plt.legend()

    # Packet Loss Plot
    plt.subplot(1, 2, 2)
    plt.plot(
        intensity,
        packet_loss_strategy_1,
        label="Packet Loss Strategy 1",
        marker="o",
        color="red",
    )
    plt.plot(
        intensity,
        packet_loss_strategy_2,
        label="Packet Loss Strategy 2",
        marker="s",
        color="purple",
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
    throughput_strategy_1 = [
        0.1518,
        0.1839,
        0.1673,
        0.1353,
        0.1025,
        0.0739,
        0.0521,
        0.0362,
        0.0247,
        0.0167,
    ]
    packet_loss_strategy_1 = [
        0.3912,
        0.6321,
        0.7764,
        0.8646,
        0.9179,
        0.9504,
        0.9702,
        0.9818,
        0.9888,
        0.9932,
    ]

    throughput_strategy_2 = [
        0.1965,
        0.2359,
        0.2297,
        0.1936,
        0.1496,
        0.1096,
        0.0769,
        0.0522,
        0.0353,
        0.0232,
    ]

    packet_loss_strategy_2 = [
        0.3152,
        0.5358,
        0.6949,
        0.8059,
        0.8803,
        0.9270,
        0.9560,
        0.9739,
        0.9843,
        0.9906,
    ]

    plot_results(
        intensity,
        throughput_strategy_1,
        throughput_strategy_2,
        packet_loss_strategy_1,
        packet_loss_strategy_2,
    )
