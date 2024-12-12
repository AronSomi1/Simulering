package task1;

import java.util.ArrayList;

public class CollisionSensor extends Proc {
    private ArrayList<Sensor> sensors = new ArrayList<Sensor>();
    private int nbrColisions, nbrSuccess, total, nbrMeasurments = 0, runningTransmissios = 0, runningTotal = 0;
    private ArrayList<Sensor> currentlySending;
    private ArrayList<Double> packetLossList;
    private SignalList signalList;
    private double[] confInterval = new double[] { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE,
            Double.MAX_VALUE };
    private boolean fail = false;

    public CollisionSensor(SignalList signalList) {
        this.signalList = signalList;
        this.packetLossList = new ArrayList<Double>();
        this.currentlySending = new ArrayList<Sensor>();
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
                currentlySending.add(sensor);
                if (currentlySending.size() > 1)
                    fail = true;
            }
                break;
            case FINISHEDSENDING: {
                if (!fail && inRange((Sensor) x.origin)) {
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
                double packetLoss = (total > 0) ? 1 - 1.0 * nbrSuccess / total : 1.0;
                packetLossList.add(packetLoss);
                signalList.SendSignal(MEASURE, this, this, time + getRandomDouble() * 6000);
                confInterval = confidenceInterval.calculate95ConfidenceInterval(packetLossList);

            }
                break;

        }
    }

    public boolean collision(Sensor origin) {
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

    private boolean inRange(Sensor origin) {
        double dx = 5000 - origin.getX();
        double dy = 5000 - origin.getY();
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
