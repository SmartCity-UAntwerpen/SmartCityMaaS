package be.uantwerpen.Models;

/**
 * Created by dries on 10/05/2017.
 */
public class Cost {
    private boolean status;
    private long weightToStart;
    private long weight;
    private long idVehicle;

    public Cost() {}

    public Cost(boolean status, long weightToStart, long weight, long idVehicle)
    {
        this.status = status;
        this.weightToStart = weightToStart;
        this.weight = weight;
        this.idVehicle = idVehicle;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getWeightToStart() {
        return weightToStart;
    }

    public void setWeightToStart(long weightToStart) {
        this.weightToStart = weightToStart;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public long getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(long idVehicle) {
        this.idVehicle = idVehicle;
    }
}
