package task1;

import java.util.*;
import java.io.*;

public class MainSimulation extends Global {
	private static double[] resultsThroughput;
	private static double[] resultsPacketLoss;
	private static double[] resultsUpperBound;

	private static final ArrayList<Double> meanValuesPacketLoss = new ArrayList<>();
	private static final ArrayList<Double> upperBounArrayList = new ArrayList<>();
	private static final ArrayList<Double> meanValuesThroughput = new ArrayList<>();
	int radius;
	double sleepTime;
	int sendTime;
	List<int[]> coordinates;

	public static void main(String[] args) throws IOException {
		System.out.println("staring main sim");

		// Initialize simulation components
		MainSimulation m = new MainSimulation();
		// Run the simulation 1000 times
		for (int i = 0; i < 100; i++) {
			runSimulation(m);
			if (i % 10 == 0)
				System.out.println("simulation n complete: " + i);
		}

		// Calculate and print averages
		calculateAndPrintAverages();
	}

	public MainSimulation() throws IOException {
		ConfigFileReader cfr = new ConfigFileReader("assignment3/task1/inputs/10000config");

		this.radius = cfr.getR();
		this.sleepTime = 1.0 / cfr.getTs();
		this.sendTime = cfr.getTp();
		this.coordinates = cfr.getCoordinates();
	}

	private static void runSimulation(MainSimulation m) throws IOException {
		// Load configuration

		SignalList signalList = new SignalList();
		CollisionSensor collisionSensor = new CollisionSensor(signalList);

		initializeSensors(collisionSensor, signalList, m.coordinates, m.radius, m.sleepTime, m.sendTime);

		// Run the simulation and process results
		simulate(collisionSensor, signalList);
		processResults(collisionSensor);
	}

	private static void initializeSensors(CollisionSensor collisionSensor, SignalList signalList,
			List<int[]> coordinates, int radius, double sleepTime, int sendTime) {
		for (int[] coord : coordinates) {
			int x = coord[1];
			int y = coord[2];
			Sensor sensor = new Sensor(x, y, collisionSensor, signalList, radius, sleepTime, sendTime);
			collisionSensor.addSensor(sensor);
		}

		for (Sensor s : collisionSensor.getSensors()) {
			s.initialStart();
		}
	}

	private static void simulate(CollisionSensor collisionSensor, SignalList signalList) {
		time = 0;
		double lowerBound = Double.MAX_VALUE;
		double previousUpperBound = 0.9519;

		// Start the measurement process
		signalList.SendSignal(MEASURE, collisionSensor, collisionSensor, time + getRandomDouble() * 6000);
		for (Sensor s : collisionSensor.getSensors()) {
			s.initialStart();
		}

		// Main simulation loop
		while (time < 20000 || previousUpperBound > lowerBound) {
			Signal actSignal = signalList.FetchSignal();
			time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
			lowerBound = collisionSensor.getConfInterval()[0];
		}

		// Clear the signal list after the simulation
		signalList.ClearList();
	}

	private static void processResults(CollisionSensor collisionSensor) {
		// Calculate statistics
		double throughput = 1.0 * collisionSensor.getnbrSuccess() / time;
		double[] confInterval = collisionSensor.getConfInterval();

		// Store results
		meanValuesPacketLoss.add(confInterval[2]);
		meanValuesThroughput.add(throughput);
		upperBounArrayList.add(confInterval[1]);

	}

	private static void calculateAndPrintAverages() {
		// Compute averages
		double avgPacketLoss = meanValuesPacketLoss.stream().mapToDouble(Double::doubleValue).average().orElse(0);
		double avgThroughput = meanValuesThroughput.stream().mapToDouble(Double::doubleValue).average().orElse(0);
		double avgUpperBound = upperBounArrayList.stream().mapToDouble(Double::doubleValue).average().orElse(0);

		// Print results
		System.out.println("Average PacketLoss: " + avgPacketLoss);
		System.out.println("Average Throughput: " + avgThroughput);
		System.out.println("Average Upper Bound: " + avgUpperBound);
		System.out.println("Packet Loss confidence Interval: "
				+ confidenceInterval.calculate95ConfidenceInterval(meanValuesPacketLoss)[3]);
		System.out.println("Throughput confidence Interval: "
				+ confidenceInterval.calculate95ConfidenceInterval(meanValuesThroughput)[3]);
	}

}