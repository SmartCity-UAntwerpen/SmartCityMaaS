package be.uantwerpen.visualization.model;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Link {
    public String _startHeading;
    public String _destinationHeading;
    public String _startNode;
    public String _destinationNode;
    public String from;
    public String to;
    public Tile destination;
    public Tile start;
    public double _distance;
    public double _angle;
    public boolean _isLocal;
    public boolean _loopback;
    public int _lockId;
}



