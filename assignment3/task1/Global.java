package task1;

import java.util.Random;

public class Global {
	public static final int ARRIVAL = 1, READY = 2, MEASURE = 3, START = 4, COLISSON = 5, OK = 6, FINISHEDSENDING = 7;
	public static double time = 0;

	public static Random rand = new Random();

	public double getNext(double lambda) {
		double res = Math.log(1 - rand.nextDouble()) / (-lambda);
		return res;
	}

	public static double getRandomDouble() {
		return rand.nextDouble();
	}
}
