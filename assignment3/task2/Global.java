package task2;

import java.util.Random;

public class Global {
	public static final int MOVE = 1, MEASURE = 2, START_SOCIALIZING = 3, STOP_SOCIALIZING = 4;
	public static double time = 0;
	public static double GLOBAL_STEP_SIZE = 0.2, INTERACTION_PAUSE_TIME = 60;
	public static Random rand = new Random();

	public double getNext(double lambda) {
		double res = Math.log(1 - rand.nextDouble()) / (-lambda);
		return res;
	}

	public static double getRandomDouble() {
		return rand.nextDouble();
	}
}
