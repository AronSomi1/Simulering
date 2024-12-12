package task1;

import java.util.Random;

public class Sensor extends Proc {

    int x, y, radius, sendTime;
    double sleepTime;
    boolean sending = false;
    SignalList signalList;
    CollisionSensor collisionSensor;
    Random rand;

    public Sensor(int x, int y, CollisionSensor collisionSensor, SignalList signalList, int radius, double sleepTime,
            int sendTime) {
        this.x = x;
        this.y = y;
        this.sendTime = sendTime;
        this.sleepTime = sleepTime;
        this.radius = radius;
        this.signalList = signalList;
        this.collisionSensor = collisionSensor;
        this.rand = new Random();

    }

    @Override
    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case START: {
                if (collisionSensor.collision(this)) {
                    double randomBackoff = rand.nextDouble();

                    signalList.SendSignal(START, collisionSensor, this, time + randomBackoff);
                    signalList.SendSignal(FINISHEDSENDING, collisionSensor, this, time + sendTime + randomBackoff);
                    signalList.SendSignal(START, this, this, time + sendTime + getNext(sleepTime) + randomBackoff);

                } else {

                    signalList.SendSignal(START, collisionSensor, this, time);
                    signalList.SendSignal(FINISHEDSENDING, collisionSensor, this, time + sendTime);
                    signalList.SendSignal(START, this, this, time + sendTime + getNext(sleepTime));

                }
            }
                break;

            /*
             * case COLISSON: {
             * sending = false;
             * signalList.SendSignal(START, this, this, time + getNext(sleepTime));
             * }
             * break;
             * case OK: {
             * sending = true;
             * signalList.SendSignal(FINISHEDSENDING, collisionSensor, this, time +
             * sendTime);
             * signalList.SendSignal(START, this, this, time + sendTime +
             * getNext(sleepTime));
             * }
             * break;
             */
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
