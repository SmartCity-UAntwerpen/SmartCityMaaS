/**
 * Encapsulates methods and properties of a Robot Tile.
 */
import * as builder from './mapbuilder.js';
export default class Tile {
    /**
     * Constructor that takes a SVG.js object as parameter
     * @param {SVG.js object} element . The SVG element associated with this tile
     * @param {int} i row-index of the tile in the grid
     * @param {int} j column-index of the tile in the grid
     */
    constructor(element){
        this._type = parseInt(element.node.getAttributeNS(builder._smartcityNamespace, "type").split("_")[2]);
        this._i = element.attr("id").split("_")[2];
        this._j = element.attr("id").split("_")[3];;
        this._id = element.attr("id");
        this._neighbours =  this._determineNeighbours();
        this._links = [];
        this._points = this._initializePoints();
        //this._arrows = this._generateArrows();
        this._generateArrows();
    }
    get type(){
        return this._type;
    }
    /**
     * Find neighbors of this tile.
     * Algorithm uses <row,col> indexing
     * Assume no neighbors by default
     * Returns dictionary with id's and/or nulls
     */
    _determineNeighbours(){
        var neighbourmap = [];
        // Assume no neighbors by default
        neighbourmap["n"] = null;
        neighbourmap["s"] = null;
        neighbourmap["e"] = null;
        neighbourmap["w"] = null;

        // Check if default case holds

        // North neighbor
        var id = "#robot_wp_" + this._i-1 + "_" + this._j;
        if(SVG.find(id)[0]){
            neighbourmap["n"] = SVG.find(id)[0].attr("id");
        }
        
        // East neighbor
        id = "#robot_wp_" + this._i + "_" + this._j+1;
        if(SVG.find(id)[0]){
            neighbourmap["e"] = SVG.find(id)[0].attr("id");
        }

        // South neighbor
        id = "#robot_wp_" + this._i+1 + "_" + this._j;
        if(SVG.find(id)[0]){
            neighbourmap["s"] = SVG.find(id)[0].attr("id");
        }

        // West neighbor
        id = "#robot_wp_" + this._i + "_" + this._j-1;
        if(SVG.find(id)[0]){
            neighbourmap["w"] = SVG.find(id)[0].attr("id");
        }
        return neighbourmap;
    }
    /**
     * Initialize the points for this tile
     * The configuration of the points depends on the type of the tile.
     * E.g. when having a X-crossing, each wind direction has 2 points: inbound and output
     * Depending on the direction configuration, a point is enabled or disabled
     * By default, points are disabled
     */
    _initializePoints(){
        var points = [];
        // West
        if([1,2,3,5,6,10,12,15,16].includes(this._type)){
            points["w_in"] = false;
            points["w_out"] = false;
        }
        // North
        if([1,2,3,4,7,11,13,16,17].includes(this._type)){
            points["n_in"] = false;
            points["n_out"] = false;
        }
        // East
        if([1,3,4,5,6,8,12,14,17].includes(this._type)){
            points["e_in"] = false;
            points["e_out"] = false;
        }
        // South
        if([1,2,4,5,7,9,13,14,15].includes(this._type)){
            points["s_in"] = false;
            points["s_out"] = false;
        }
        return points;
    }
    /**
     * Update a neighbor id. 
     * @param {int} neighborId 
     * @param {string: n,e,s,w} direction 
     */
    setNeighbor(neighborId, direction){
        this._neighbours[direction] = neighborId;
    }
    getPoint(direction){
        return this._points[direction];
    }
    /**
     * Draw the direction arrows
     * Regarding the DOM, arrows are appended as children of the groupnode of this tile
     */
    _generateArrows(){
        // Get the corresponding SVG node for this tile
        var tileSVGNode = SVG.find("#"+this._id)[0];
        var cellsize = visualisationCore.gridCellSize;
        // NW arrows
        if([1,2,3].includes(this._type)){
            // Attach NW arrows to left top
            var nw_arrows = visualisationCore.drawRobotTileDirectionsNW(0,0,tileSVGNode);
            nw_arrows.scale(0.5,0,0);
        }
        // NE arrows
        if([1,3,4].includes(this._type)){
            // Attach NE arrows to right top
            var ne_arrows = visualisationCore.drawRobotTileDirectionsNE(cellsize/4+10, 0, tileSVGNode);
            ne_arrows.scale(0.5,0,0);
        }

        // ES arrows
        if([1,4,5].includes(this._type)){
            // Attach ES arrows to right bottom
            var es_arrows = visualisationCore.drawRobotTileDirectionsES(cellsize/4+10, cellsize/4+10, tileSVGNode);
            es_arrows.scale(0.5,0,0);
            
        }

        // SW arrows
        if([1,2,5].includes(this._type)){
            // Attach SW arrows to left bottom
            var sw_arrows = visualisationCore.drawRobotTileDirectionsSW(0, cellsize/4+10, tileSVGNode);
            sw_arrows.scale(0.5,0,0);
        }

        // NS arrows
        if([1,2,4,7,9,11].includes(this._type)){
            // Attach NS, vertical right
            var ns_arrows = visualisationCore.drawRobotTileDirectionsNS(cellsize/4-4, 0, tileSVGNode);
            ns_arrows.scale(0.5,0.9,0,0);
        }

        // EW arrows
        if([1,3,5,6,8,10].includes(this._type)){
            // Attach EW, horizontal bottom
            var ew_arrows = visualisationCore.drawRobotTileDirectionsEW(0, cellsize/4-4, tileSVGNode);
            ew_arrows.scale(0.9,0.5,0,0);
        }
    }
}