package be.uantwerpen.localization.astar;

/**
 * Created by Revil on 10/05/2017.
 * EDGES
 */
public class Links {
    private String name;
    private String startPos;
    private String endPos;
    private long weight;

    public Links(String name, String startPos, String endPos, long weight) {
        this.name = name;
        this.startPos = startPos;
        this.endPos = endPos;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartPos() {
        return startPos;
    }

    public void setStartPos(String startPos) {
        this.startPos = startPos;
    }

    public String getEndPos() {
        return endPos;
    }

    public void setEndPos(String endPos) {
        this.endPos = endPos;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }
}
