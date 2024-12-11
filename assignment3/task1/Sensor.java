package task1;

public class Sensor extends Proc {

    int x, y, radius, sendTime;
    double sleepTime;
    boolean sending = false;
    SignalList signalList;
    CollisionSensor collisionSensor;

    public Sensor(int x, int y, CollisionSensor collisionSensor, SignalList signalList, int radius, double sleepTime,
            int sendTime) {
        this.x = x;
        this.y = y;
        this.sendTime = sendTime;
        this.sleepTime = sleepTime;
        this.radius = radius;
        this.signalList = signalList;
        this.collisionSensor = collisionSensor;
    }

    @Override
    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case START: {
                signalList.SendSignal(START, collisionSensor, this, time);
            }
                break;
            case COLISSON: {
                sending = false;
                signalList.SendSignal(START, this, this, time + getNext(sleepTime));
            }
                break;
            case OK: {
                sending = true;
                signalList.SendSignal(FINISHEDSENDING, collisionSensor, this, time + sendTime);
                signalList.SendSignal(START, this, this, time + sendTime + getNext(sleepTime));
            }
                break;

        }
    }

    public void initialStart() {
        signalList.SendSignal(START, this, this, time + getNext(sleepTime));
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getRadius() {
        return radius;
    }

    public SignalList getSignalList() {
        return signalList;
    }
}
