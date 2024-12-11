package task1;

import java.util.ArrayList;
import java.util.HashMap;

public class CollisionSensor extends Proc {
    private ArrayList<Sensor> sensors = new ArrayList<Sensor>();
    private int nbrColisions, nbrSuccess, total, nbrMeasurments = 0, runningTransmissios = 0, runningTotal = 0;
    private ArrayList<Sensor> currentlySending = new ArrayList<Sensor>();
    private ArrayList<Double> packetLossList = new ArrayList<Double>();
    private SignalList signalList;
    private double[] confInterval = new double[] { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE,
            Double.MAX_VALUE };
    private boolean fail = false;

    public CollisionSensor(SignalList signalList) {
        this.signalList = signalList;
    }

    public void addSensor(Sensor s) {
        sensors.add(s);
    }

    @Override
    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case START: {
                total++;
                Sensor sensor = (Sensor) x.origin;
                if (currentlySending.size() > 0) {
                    fail = true;
                    nbrColisions++;
                    sensor.getSignalList().SendSignal(COLISSON, sensor, this, time);
                } else {
                    currentlySending.add(sensor);
                    sensor.getSignalList().SendSignal(OK, sensor, this, time);
                }
            }
                break;
            case FINISHEDSENDING: {
                if (!fail) {
                    nbrSuccess++;
                }
                currentlySending.remove(x.origin);
                if (currentlySending.size() == 0) {
                    fail = false;
                }
            }
                break;
            case MEASURE: {
                nbrMeasurments++;
                double packetLoss = 1 - 1.0 * nbrSuccess / total;
                packetLossList.add(packetLoss);
                signalList.SendSignal(MEASURE, this, this, time + getRandomDouble() * 6000);
                confInterval = confidenceInterval.calculate95ConfidenceInterval(packetLossList);

            }
                break;

        }
    }

    private boolean collision(Sensor origin) {
        for (Sensor s : currentlySending) {
            if (overlappingAreas(origin, s)) {

                return true;
            }
        }
        return false;
    }

    private boolean overlappingAreas(Sensor origin, Sensor target) {
        double dx = target.getX() - origin.getX();
        double dy = target.getY() - origin.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= origin.getRadius();
    }

    public ArrayList<Sensor> getSensors() {
        return sensors;
    }

    public int getNbrCollision() {
        return nbrColisions;
    }

    public int getTotal() {
        return total;
    }

    public int getnbrSuccess() {
        return nbrSuccess;
    }

    public double[] getConfInterval() {
        return confInterval;
    }

}
