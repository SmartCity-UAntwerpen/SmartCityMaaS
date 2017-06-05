package be.uantwerpen.visualization.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 11/05/2017.
 */
public class DummyMap {

    private List<DummyPoint> dummyPoints;
    private int unit = 3;

    public DummyMap() {
        //Dries wereld is 4 meter
        //Quentin wereld is 2 meter

        unit = 4;
        /*dummyPoints = new ArrayList<DummyPoint>();
        List<Integer> neighbourList = new ArrayList<Integer>();
        neighbourList.add(1);
        neighbourList.add(2);
        DummyPoint A0 = new DummyPoint(0,neighbourList,0,0);
        dummyPoints.add(A0);

        List<Integer> neighbourList1 = new ArrayList<Integer>();
        neighbourList1.add(0);
        neighbourList1.add(2);
        DummyPoint B1 = new DummyPoint(1,neighbourList1,0,10);
        dummyPoints.add(B1);
        List<Integer> neighbourList2 = new ArrayList<Integer>();
        neighbourList2.add(0);
        neighbourList2.add(1);
        DummyPoint C2 = new DummyPoint(2,neighbourList2,10,10);
        dummyPoints.add(C2);
        List<Integer> neighbourList3 = new ArrayList<Integer>();
        neighbourList3.add(0);
        neighbourList3.add(2);
        DummyPoint D4  = new DummyPoint(3,neighbourList3,10,0);
        dummyPoints.add(D4);
        List<Integer> neighbourList4 = new ArrayList<Integer>();
        neighbourList4.add(2);
        neighbourList4.add(3);
        DummyPoint E5  = new DummyPoint(4,neighbourList4,20,5);
        dummyPoints.add(E5);*/
    }

    public void loadDummyMap2() {
        unit = 3; // 5 meter
        dummyPoints = null;
        dummyPoints = new ArrayList<DummyPoint>();
        List<Integer> neighbourList = new ArrayList<Integer>();
        neighbourList.add(1);
        neighbourList.add(2);
        DummyPoint A0 = new DummyPoint(0,neighbourList,10,10);
        dummyPoints.add(A0);
        List<Integer> neighbourList1 = new ArrayList<Integer>();
        neighbourList1.add(0);
        neighbourList1.add(2);
        DummyPoint B1 = new DummyPoint(1,neighbourList1,30,10);
        dummyPoints.add(B1);
        List<Integer> neighbourList2 = new ArrayList<Integer>();
        neighbourList2.add(0);
        neighbourList2.add(1);
        DummyPoint C2 = new DummyPoint(2,neighbourList2,30,20);
        dummyPoints.add(C2);
    }
    public DummyMap(List<DummyPoint> dummyPoints, int unit) {
        this.dummyPoints = dummyPoints;
        this.unit = unit;
    }

    public List<DummyPoint> getDummyPoints() {
        return dummyPoints;
    }

    public void setDummyPoints(List<DummyPoint> dummyPoints) {
        this.dummyPoints = dummyPoints;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
}
