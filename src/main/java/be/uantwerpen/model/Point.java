package be.uantwerpen.model;

import javax.persistence.*;

/**
 * Created by Niels on 24/03/2016.
 */
@Entity
//@Table(name = "point", schema = "", catalog = "smartcitydb")
public class Point
{
    private Long id;
    private String type;
    private int x;
    private int y;

    public Point() {}

    public Point(Long id, int x, int y, String type)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    @Id
    @Column(name = "pid")
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
    @Column(name = "type")
    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Point that = (Point) o;

        if(id != that.id) return false;
        if(type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (int)(id % Integer.MAX_VALUE);

        result = 31 * result + (type != null ? type.hashCode() : 0);

        return result;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "PointEntity{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
