package be.uantwerpen.model;

import javax.persistence.*;
/**
 * NV 2018
 */
@Entity
//@Table(name = "link", schema = "", catalog = "smartcitydb")
public class Link
{
    private Long id;
    private Long length;
    private String startDirection;
    private String stopDirection;
    private Point startPoint;
    private Point stopPoint;
    private Long weight;
    private String vehicle;
    private Long IDVehicle;

    public Link() {}

    public Link(Long id, Point startPoint, Point stopPoint, String vehicle)
    {
        this.id = id;
        this.startPoint = startPoint;
        this.stopPoint = stopPoint;
        this.vehicle = vehicle;
    }

    public Link(Long id, Point startPoint, Point stopPoint, String vehicle, Long weight)
    {
        this.id = id;
        this.startPoint = startPoint;
        this.stopPoint = stopPoint;
        this.vehicle = vehicle;
        this.weight = weight;
    }

    @Id
    @Column(name = "lid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Basic
    @Column(name = "length")
    public Long getLength()
    {
        return length;
    }

    public void setLength(Long length)
    {
        this.length = length;
    }

    @Basic
    @Column(name = "start_direction")
    public String getStartDirection()
    {
        return startDirection;
    }

    public void setStartDirection(String startDirection)
    {
        this.startDirection = startDirection;
    }

    @Basic
    @Column(name = "stop_direction")
    public String getStopDirection()
    {
        return stopDirection;
    }

    public void setStopDirection(String stopDirection)
    {
        this.stopDirection = stopDirection;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Link that = (Link) o;

        if(id != that.id) return false;
        if(length != null ? !length.equals(that.length) : that.length != null) return false;
        if(startDirection != null ? !startDirection.equals(that.startDirection) : that.startDirection != null)
            return false;
        if(stopDirection != null ? !stopDirection.equals(that.stopDirection) : that.stopDirection != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (int)(id % Integer.MAX_VALUE);

        result = 31 * result + (length != null ? length.hashCode() : 0);
        result = 31 * result + (startDirection != null ? startDirection.hashCode() : 0);
        result = 31 * result + (stopDirection != null ? stopDirection.hashCode() : 0);

        return result;
    }

    @OneToOne
    @JoinColumn(name = "start_point", referencedColumnName = "pid")
    public Point getStartPoint()
    {
        return startPoint;
    }

    public void setStartPoint(Point startPoint)
    {
        this.startPoint = startPoint;
    }

    @OneToOne
    @JoinColumn(name = "stop_point", referencedColumnName = "pid")
    public Point getStopPoint()
    {
        return stopPoint;
    }

    public void setStopPoint(Point stopPoint)
    {
        this.stopPoint = stopPoint;
    }

    @Basic
    @Column(name = "weight")
    public Long getWeight()
    {
        return weight;
    }

    public void setWeight(Long weight)
    {
        this.weight = weight;
    }

    @Basic
    @Column(name = "vehicle")
    public String getVehicle()
    {
        return vehicle;
    }

    public void setVehicle(String vehicle)
    {
        this.vehicle = vehicle;
    }

//    @Basic
//    @Column(name = "vehicleID")
    public Long getVehicleID() { return IDVehicle;}

    public void setVehicleID(Long vehicleID) { this.IDVehicle = vehicleID;}

    @Override
    public String toString() {
        return "LinkEntity{" +
                "lid=" + id +
                ", length=" + length +
                ", startDirection='" + startDirection + '\'' +
                ", stopDirection='" + stopDirection + '\'' +
                ", startPoint=" + startPoint +
                ", stopPoint=" + stopPoint +
                ", weight=" + weight +
                ", vehicle=" + vehicle +
                '}';
    }
}
