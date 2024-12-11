package task1;

import java.util.ArrayList;
import java.util.Arrays;

public class confidenceInterval {
    public static double[] calculate95ConfidenceInterval(ArrayList<Double> data) {
        int n = data.size();
        if (n == 0) {
            throw new IllegalArgumentException("Data is empty");
        }
        double mean = data.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(-9999);

        double squaredDifference = 0.0;

        for (double d : data) {
            squaredDifference += Math.pow(d - mean, 2);
        }

        double standardDeviation = Math.sqrt(squaredDifference / (n - 1));

        double sem = standardDeviation / Math.sqrt(n);

        double z = 1.96;

        double marginOfError = z * sem;

        double lowerBound = mean - marginOfError;
        double upperBound = mean + marginOfError;

        return new double[] { lowerBound, upperBound, mean, marginOfError * 2 };
    }
}
