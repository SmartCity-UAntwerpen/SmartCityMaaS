package be.uantwerpen.visualization.model;

/**
 * Created by Frédéric Melaerts on 30/05/2017.
 */
public class DummyVehicle implements Runnable {
        private volatile int distance;
        private int ID;
        @Override
        public void run() {
            distance = 0;
            // 1000 = 100,0%
            while(distance < 1000)
            {
                try {
                    // every 50 millisecons, increase: 0.1%
                    // or 1% every second
                    Thread.sleep(50);
                    distance++;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    public DummyVehicle(int ID) {
        this.ID = ID;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getValue() {
        return distance;
    }
}
