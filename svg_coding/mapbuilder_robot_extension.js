/**
 * Encapsulates methods and properties of a Robot Tile.
 */
import * as builder from './mapbuilder.js';
import Link from './mapbuilder_link_extension.js';
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
        this._neighbors = [];
        this._determineNeighbors();
        this._localLinks = [];
        this._outboundLinks = [];
        this._headings = this._initializeHeadings();
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
    _determineNeighbors(){
        var neighbors = [];
        // Assume no neighbors by default
        neighbors["n"] = null;
        neighbors["s"] = null;
        neighbors["e"] = null;
        neighbors["w"] = null;

        // Check if default case holds

        // North neighbor
        var id = "#robot_wp_" + String(this._i-1) + "_" + String(this._j);
        if(SVG.find(id)[0]){
            this._establishNeighbourship(id.substr(1), "n");
            // neighbors["n"] = id.substr(1);
            // var neighborTile = builder.getTile(id.substr(1));
            // neighborTile.setNeighbor(this.id, "s")
        }
        
        // East neighbor
        id = "#robot_wp_" + String(this._i) + "_" + String(this._j+1);
        if(SVG.find(id)[0]){
            // neighbors["e"] = id.substr(1);
            this._establishNeighbourship(id.substr(1), "e");
        }

        // South neighbor
        id = "#robot_wp_" + String(this._i+1) + "_" + String(this._j);
        if(SVG.find(id)[0]){
            // neighbors["s"] = SVG.find(id)[0].attr("id");
            this._establishNeighbourship(id.substr(1), "s");

        }

        // West neighbor
        id = "#robot_wp_" + String(this._i) + "_" + String(this._j-1);
        if(SVG.find(id)[0]){
            // neighbors["w"] = SVG.find(id)[0].attr("id");
            this._establishNeighbourship(id.substr(1), "w");

        }
        return neighbors;
    }
    /**
     * Initialize the points for this tile
     * The configuration of the points depends on the type of the tile.
     * E.g. when having a X-crossing, each wind direction has 2 points: inbound and output
     * Depending on the direction configuration, a point is enabled or disabled
     * By default, points are disabled
     */
    _initializeHeadings(){
        var headings = [];
        // West
        if([1,2,3,5,6,10,12,15,16].includes(this._type)){
            headings["w_in"] = false;
            headings["w_out"] = false;
        }
        // North
        if([1,2,3,4,7,11,13,16,17].includes(this._type)){
            headings["n_in"] = false;
            headings["n_out"] = false;
        }
        // East
        if([1,3,4,5,6,8,12,14,17].includes(this._type)){
            headings["e_in"] = false;
            headings["e_out"] = false;
        }
        // South
        if([1,2,4,5,7,9,13,14,15].includes(this._type)){
            headings["s_in"] = false;
            headings["s_out"] = false;
        }
        return headings;
    }
    /**
     * Update neighbor id for a certain heading
     * @param {int} neighborId 
     * @param {string: n,e,s,w} heading 
     */
    setNeighbor(neighborId, heading){
        this._neighbors[heading] = neighborId;
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
            nw_arrows.on('mouseover', builder.directionArrowHoverIn);
            nw_arrows.on('mouseout', builder.directionArrowHoverOut);
        }
        // NE arrows
        if([1,3,4].includes(this._type)){
            // Attach NE arrows to right top
            var ne_arrows = visualisationCore.drawRobotTileDirectionsNE(cellsize/4+10, 0, tileSVGNode);
            ne_arrows.scale(0.5,0,0);
            ne_arrows.on('mouseover', builder.directionArrowHoverIn);
            ne_arrows.on('mouseout', builder.directionArrowHoverOut);
        }

        // ES arrows
        if([1,4,5].includes(this._type)){
            // Attach ES arrows to right bottom
            var es_arrows = visualisationCore.drawRobotTileDirectionsES(cellsize/4+10, cellsize/4+10, tileSVGNode);
            es_arrows.scale(0.5,0,0);
            es_arrows.on('mouseover', builder.directionArrowHoverIn);
            es_arrows.on('mouseout', builder.directionArrowHoverOut);
            
        }

        // SW arrows
        if([1,2,5].includes(this._type)){
            // Attach SW arrows to left bottom
            var sw_arrows = visualisationCore.drawRobotTileDirectionsSW(0, cellsize/4+10, tileSVGNode);
            sw_arrows.scale(0.5,0,0);
            sw_arrows.on('mouseover', builder.directionArrowHoverIn);
            sw_arrows.on('mouseout', builder.directionArrowHoverOut);
        }

        // NS arrows
        if([1,2,4,7,9,11].includes(this._type)){
            // Attach NS, vertical right
            var ns_arrows = visualisationCore.drawRobotTileDirectionsNS(cellsize/4-4, 0, tileSVGNode);
            ns_arrows.scale(0.5,0.9,0,0);
            ns_arrows.on('mouseover', builder.directionArrowHoverIn);
            ns_arrows.on('mouseout', builder.directionArrowHoverOut);
        }

        // EW arrows
        if([1,3,5,6,8,10].includes(this._type)){
            // Attach EW, horizontal bottom
            var ew_arrows = visualisationCore.drawRobotTileDirectionsEW(0, cellsize/4-4, tileSVGNode);
            ew_arrows.scale(0.9,0.5,0,0);
            ew_arrows.on('mouseover', builder.directionArrowHoverIn);
            ew_arrows.on('mouseout', builder.directionArrowHoverOut);
        }
    }

    /**
     * Enables or disables a direction on this tile.
     * This direction will internally be represented as a link with
     * the begin and end on this tile
     * If a direction exists, it is removed
     * If a direction does not exist, it will be created. The direction is 
     * marked as valid when starting from the endpoint, a link can be made to another tile.
     * If no link can be made (i.e. non-accepting point at neighbor or no neighbor at endpoint),
     * the link is marked as invalid.
     * @param {string} direction
     */
    toggleDirection(direction){
        // Return value
        var directionStatus = "";

        // Direction is interpreted as a link on this tile
        // Check if it exists
        var from = direction.split("_")[1].toLowerCase();
        var to = direction.split("_")[2].toLowerCase();
        var link = this._localLinks.find((v)=>{
            return v.from===from && v.to===to;
        });
        // If link exists, remove it
        if(link){
            var index = this._localLinks.indexOf(link);
            // If status of in and out points has to change, we need action:


            // in-status changes: inbound link will break. Notify neighbor to take action
            // (because he owns this link). he needs to remove the link and change
            // its internal directions accordingly (will become invalid)

            // out-status chagnes: outbound link will break. Remove this link
            // TODO: notify
            this._localLinks.splice(index, 1);
            directionStatus = "disabled";
        }
        // Link does not exist, add it
        else {
            var angle = this._calculateDirectionAngle(from, to);
            link = new Link(this._id, from, this._id, to, 0, angle);
            this._localLinks.push(link);
            // Check if external links can arise
            if(this._isOutboundPossible(to)){
                // Create external link
                var linkProperties = this._getOutboundDestination(to);
                var outboundLink = new Link(this._id, from, linkProperties.destinationId, linkProperties.destinationHeading, linkProperties.distance, 0);
                this._outboundLinks.push(outboundLink);
                directionStatus = "valid";
            }
            else{
                // No outbound links can arise, this link is invalid
                directionStatus = "invalid";
            }
            // Check if inbound links arise
            if(this._isInboundPossible(from)){
                // Creation of inbound link by its source
                var linkProperties = this._getInboundSource(from);
                // var sourceHeading = this._getOppositeHeading(from);
                var sourceTile = builder.getTile(linkProperties.sourceId);
                // sourceTile.createLink(sourceId, sourceHeading, this.id, from);
                sourceTile.createLink(linkProperties.sourceHeading, this._id, from, linkProperties.distance);
            }
            // To-heading is now sending
            this._headings[to+"_out"] = true;
            // from-heading is now accepting
            this._headings[from+"_in"] = true;

            return directionStatus;
        }
    }
    /**
     * Analyse if an outbound link can be established through a certain direction
     * @param {*} heading . Outbound heading
     */
    _isOutboundPossible(heading){
        return (this._getOutboundDestination(heading) !== null);
    }

    /**
     * Analyse if an inbound link can be established through a certain direction
     * @param {*} heading . Inbound heading
     */
    _isInboundPossible(heading){
        return (this._getInboundSource(heading) !== null);
    }

    /**
     * Returns the ID of source if an inbound link can be established for inbound heading
     * @param {string} heading . inbound heading  
     */
    _getInboundSource(heading){
        // Get opposite heading, this will be the outbound heading of the neighbor
        var oppositeHeading = this._getOppositeHeading(heading);
        // Get neighbor for heading
        var neighborId = this._neighbors[heading];
        if(!neighborId){
            return null;
        }
        var neighbor = builder.getTile(neighborId);
        // TODO: adjust initial distance to be dynamic (trafficlight, crossing, parkeerplaatske)
        const answer = neighbor.sends(oppositeHeading, 10)
        if(answer.sends){
            return answer;
        }
        else{
            return null;
        }
    }

    /**
     * Returns the ID of destination if an outbound link can be established for outbound heading.
     * If no outboundlink is possible, return is null.
     * @param {string} heading . outbound heading 
     * @returns {int} id of outbound destination
     */
    _getOutboundDestination(heading){
        // Get opposite heading, this will be the inbound heading of the neighbor
        var oppositeHeading = this._getOppositeHeading(heading);
        // Get neighbor for heading
        var neighborId = this._neighbors[heading];
        if(!neighborId){
            return null;
        }

        var neighbor = builder.getTile(neighborId);
        // TODO: adjust initial distance to be dynamic (trafficlight, crossing, parkeerplaatske)
        const answer = neighbor.accepts(oppositeHeading, 10);
        if(answer.accepts){
            return answer;
        }
        else{
            return null;
        }
    }

    /**
     * Checks if this node accepts inbound links on a certain heading. If this tile 
     * has no headings (i.e. straight line tiles and corner tiles), recursion is applied on neighbor
     * @param {string} heading 
     * @param {int} distance. Optional parameter for length calculation when recursing. Zero by default. Units: centimeters
     * @return {boolean, string, string, int} accepts, destinationHeading, destinationId, distance
     */
    accepts(heading, distance=0){
        // Is this a straight line tile or corner tile?
        if([12,13,14,15,16,17].includes(this._type)){
            var oppositeHeading = this._getOppositeHeading(heading);
            // It is. Propagate to neighbor
            // Straight line
            if(this._type === 12 || this._type ===13){
                    var neighborId = this._neighbors[oppositeHeading];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.accepts(heading,distance+30);
            }
            // Corners
            else if (this._type === 14){
                if(heading === "e"){
                    // Ask south neighbor
                    var neighborId = this._neighbors["s"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.accepts("n", distance+25);
                }
                else if(heading === "s"){
                    // Ask east neighbor
                    var neighborId = this._neighbors["e"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.accepts("w", distance+25);
                }
            }
            else if (this._type === 15){
                if(heading === "w"){
                    // Ask south neighbor
                    var neighborId = this._neighbors["s"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.accepts("n", distance+25);
                }
                else if(heading === "s"){
                    // Ask east neighbor
                    var neighborId = this._neighbors["w"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.accepts("e", distance+25);
                }
            }
            else if (this._type === 16){
                if(heading === "w"){
                    // Ask south neighbor
                    var neighborId = this._neighbors["n"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.accepts("s", distance+25);
                }
                else if(heading === "n"){
                    // Ask east neighbor
                    var neighborId = this._neighbors["w"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.accepts("e", distance+25);
                }
            }
            else if (this._type === 17){
                if(heading === "e"){
                    // Ask south neighbor
                    var neighborId = this._neighbors["n"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.accepts("s", distance+25);
                }
                else if(heading === "n"){
                    // Ask east neighbor
                    var neighborId = this._neighbors["e"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.accepts("w", distance+25);
                }
            }
        }
        else{
            // Tile has headings
            if(this._headings[heading+"_in"]){
                return {accepts:true, destinationHeading: heading, destinationId: this.id,distance:distance+10};
            }
            else{
                return {accepts:false};
            }
        }
    }

    /**
     * Checks if this node sends outbound links from a certain heading. If this tile 
     * has no headings (i.e. straight line tiles and corner tiles), recursion is applied on neighbor
     * @param {string} heading 
     * @param {int} distance. Optional parameter for length calculation when recursing. Zero by default. Units: centimeters
     * @return {boolean, string, string, int} sends, sourceHeading, sourceId, distance
     */
    sends(heading, distance=0){
        // Is this a straight line tile or corner tile?
        if([12,13,14,15,16,17].includes(this._type)){
            var oppositeHeading = this._getOppositeHeading(heading);
            // It is. Propagate to neighbor
            // Straight line
            if(this._type === 12 || this._type ===13){
                    var neighborId = this._neighbors[oppositeHeading];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.sends(heading,distance+30);
            }
            // Corners
            else if (this._type === 14){
                if(heading === "e"){
                    // Ask south neighbor
                    var neighborId = this._neighbors["s"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.sends("n", distance+25);
                }
                else if(heading === "s"){
                    // Ask east neighbor
                    var neighborId = this._neighbors["e"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.sends("w", distance+25);
                }
            }
            else if (this._type === 15){
                if(heading === "w"){
                    // Ask south neighbor
                    var neighborId = this._neighbors["s"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.sends("n", distance+25);
                }
                else if(heading === "s"){
                    // Ask east neighbor
                    var neighborId = this._neighbors["w"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.sends("e", distance+25);
                }
            }
            else if (this._type === 16){
                if(heading === "w"){
                    // Ask south neighbor
                    var neighborId = this._neighbors["n"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.sends("s", distance+25);
                }
                else if(heading === "n"){
                    // Ask east neighbor
                    var neighborId = this._neighbors["w"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.sends("e", distance+25);
                }
            }
            else if (this._type === 17){
                if(heading === "e"){
                    // Ask south neighbor
                    var neighborId = this._neighbors["n"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.sends("s", distance+25);
                }
                else if(heading === "n"){
                    // Ask east neighbor
                    var neighborId = this._neighbors["e"];
                    var neighborTile = builder.getTile(neighborId);
                    return neighborTile.sends("w", distance+25);
                }
            }
        }
        else{
            // Tile has headings
            if(this._headings[heading+"_out"]){
                return {sends:true, sourceHeading: heading, sourceId: this.id,distance:distance+10};
            }
            else{
                return {sends:false};
            }
        }
    }

    /**
     * Callback function for when this tile can solve an invalid outbound direction
     * @param {string} sourceHeading 
     * @param {number} destinationId 
     * @param {String} destinationHeading 
     * @param {number} distance 
     */
    createLink(sourceHeading, destinationId, destinationHeading, distance){
        // Create link
        var link = new Link(this.id, sourceHeading, destinationId, destinationHeading, distance, 0);
        this._outboundLinks.push(link);
        // Adjust color of direction arrows
        // Find local links with this heading as destination
        this._localLinks.forEach((link) => {
            if(link.destinationHeading === sourceHeading){
                var direction = link.startHeading + "_" + link.destinationHeading;
                builder.changeDirectionArrowColor(this.id, direction, "valid");
            }
        });

    }

    /**
     * Returns the opposite of a direction. E.g. N <-> S, E<->W
     * @param {string} direction 
     */
    _getOppositeHeading(direction){
        switch(direction){
            case "n":
                return "s";
            case "s":
                return "n";
            case "w":
                return "e";
            case "e":
                return "w";
        }
    }

    /**
     * Get the angle which a robot has to turn for a direction
     * @param {string} from n|e|s|w
     * @param {string} to n|e|s|w
     * @return {int} angle in degrees
     */
    _calculateDirectionAngle(from, to){
        var direction = from+to;
        if (["wn", "ne", "es", "sw"].includes(direction)){
            return 90;
        }
        else if(["ws", "nw", "en", "en", "se"].includes(direction)){
            return -90;
        }
        else{
            return 0;
        }
    }

    /**
     * Establishes a neighborship between this node and neighbor with neighborId
     * This involves registering the neighborId with the heading in this tile
     * and registering this Id with the opposite heading in the neighbor
     * @param {string} heading n | e | s | w
     * @param {string} neighborId . Format: robot_wp_i_j
     */
    _establishNeighbourship(neighborId, heading){
        this._neighbors[heading] = neighborId;
        var neighborTile = builder.getTile(neighborId);
        neighborTile.setNeighbor(this.id, this._getOppositeHeading(heading));
    }

    get id(){
        return this._id;
    }

}