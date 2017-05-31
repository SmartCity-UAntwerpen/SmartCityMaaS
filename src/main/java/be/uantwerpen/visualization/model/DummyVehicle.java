package be.uantwerpen.visualization.model;

/**
 * Created by Frédéric Melaerts on 30/05/2017.
 */
public class DummyVehicle implements Runnable {
        private volatile int distance;

        @Override
        public void run() {
            distance = 0;
            while(distance < 100)
            {
                try {
                    Thread.sleep(250);
                    distance++;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    public int getValue() {
        return distance;
    }
}
