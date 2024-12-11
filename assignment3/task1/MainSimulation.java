package task1;

import java.util.*;
import java.io.*;

public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {
		// Simulationi parameters
		ConfigFileReader cfr = new ConfigFileReader(
				"assignment3/task1/inputs/2000config");
		Signal actSignal;
		int radius = cfr.getR();
		double sleepTime = 1.0 * 1.0 / cfr.getTs();
		int sendTime = cfr.getTp();
		List<int[]> coordinates = cfr.getCoordinates();
		Random rand = new Random();
		SignalList signalList = new SignalList();
		CollisionSensor collisionSensor = new CollisionSensor(signalList);
		double ConfLenght = Double.MAX_VALUE;

		// Initialize sensors
		for (int[] coord : coordinates) {
			int x = coord[1];
			int y = coord[2];
			Sensor sensor = new Sensor(x, y, collisionSensor, signalList,
					radius, sleepTime, sendTime);
			collisionSensor.addSensor(sensor);
		}
		// Start sensors
		for (Sensor s : collisionSensor.getSensors()) {
			s.initialStart();

		}
		signalList.SendSignal(MEASURE, collisionSensor, collisionSensor, time + getRandomDouble() * 6000);

		// Main loop
		while (time < 20000 || ConfLenght > 0.0001) {
			actSignal = signalList.FetchSignal();
			time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
			ConfLenght = collisionSensor.getConfInterval()[3];

		}
		System.out.println("Packet loss probability: " + collisionSensor.getConfInterval()[2]);
		System.out.println("Throughput:" + 1.0 * collisionSensor.getnbrSuccess() / time);

		System.out.println("Number of collisions: " + collisionSensor.getNbrCollision());
		System.out.println("Total sends: " + collisionSensor.getTotal());
		System.out.println("Successfull sends: " + collisionSensor.getnbrSuccess());

	}
}