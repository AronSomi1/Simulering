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
	double previousUpperBound;

	public static void main(String[] args) throws IOException {

		// if (args.length < 2) {
		// System.out.println("Usage: java MainSimulation <n> <previousUpperBound>");
		// return;
		// }

		// Parse input parameters
		// int n = Integer.parseInt(args[0]);
		// double previousUpperBound = Double.parseDouble(args[1]);

		System.out.println("staring main sim");

		// Initialize simulation components
		MainSimulation m = new MainSimulation(10000, 0.97);
		// Run the simulation 1000 times
		for (int i = 0; i < 100; i++) {
			runSimulation(m);
			// if (i % 10 == 0)
			System.out.println("simulation n complete: " + i);
		}

		// Calculate and print averages
		calculateAndPrintAverages();
	}

	public MainSimulation(int n, double previousUpperBound) throws IOException {
		String configPath = "assignment3/task1/inputs/" + n + "config";
		ConfigFileReader cfr = new ConfigFileReader(configPath);

		this.radius = cfr.getR();
		this.sleepTime = 1.0 / cfr.getTs();
		this.sendTime = cfr.getTp();
		this.coordinates = cfr.getCoordinates();
		this.previousUpperBound = previousUpperBound;
	}

	private static void runSimulation(MainSimulation m) throws IOException {
		// Load configuration

		SignalList signalList = new SignalList();
		CollisionSensor collisionSensor = new CollisionSensor(signalList);

		initializeSensors(collisionSensor, signalList, m.coordinates, m.radius, m.sleepTime, m.sendTime);

		// Run the simulation and process results
		simulate(collisionSensor, signalList, m.previousUpperBound);
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

	private static void simulate(CollisionSensor collisionSensor, SignalList signalList, double previousUpperBound) {
		time = 0;
		double lowerBound = Double.MAX_VALUE;

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
		System.out.println("Average Throughput: " + avgThroughput);
		System.out.println("Throughput confidence Interval: "
				+ confidenceInterval.calculate95ConfidenceInterval(meanValuesThroughput)[3]);

		System.out.println("Average PacketLoss: " + avgPacketLoss);
		System.out.println("Packet Loss confidence Interval: "
				+ confidenceInterval.calculate95ConfidenceInterval(meanValuesPacketLoss)[3]);
		System.out.println("Average Upper Bound: " + avgUpperBound);

	}

}