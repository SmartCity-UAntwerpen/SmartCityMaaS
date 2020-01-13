/**
 * Encapsulates methods and properties of a Robot Tile.
 */
import * as builder from './mapbuilder.js';
import Link from './mapbuilder_link_extension.js';
export default class Tile {
    /**
     * Constructor that takes a SVG.js object as parameter
     * @param {SVG.js object} element . The SVG element associated with this tile
     */
    constructor(element){
        this._type = parseInt(element.node.getAttributeNS(builder._smartcityNamespace, "type").split("_")[2]);
        this._i = element.attr("id").split("_")[2];
        this._j = element.attr("id").split("_")[3];
        this._id = element.attr("id");
        this._neighbors = [];
        this._determineNeighbors();
        this._headings = this._initializeHeadings();
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
        var i = parseInt(this._i);
        var j = parseInt(this._j);

        // Check if default case holds
        // North neighbor
        var id = "#robot_wp_" + String(i-1) + "_" + String(j);
        if(SVG.find(id)[0]){
            this._establishNeighbourship(id.substr(1), "n");
        }
        // East neighbor
        id = "#robot_wp_" + String(i) + "_" + String(j+1);
        if(SVG.find(id)[0]){
            this._establishNeighbourship(id.substr(1), "e");
        }
        // South neighbor
        id = "#robot_wp_" + String(i+1) + "_" + String(j);
        if(SVG.find(id)[0]){
            this._establishNeighbourship(id.substr(1), "s");
        }
        // West neighbor
        id = "#robot_wp_" + String(i) + "_" + String(j-1);
        if(SVG.find(id)[0]){
            this._establishNeighbourship(id.substr(1), "w");
        }
        return neighbors;
    }
    /**
     * Initialize the headings for this tile
     * The configuration of the headings depends on the type of the tile.
     * Each heading acts in two ways: as source and as destination.
     */
    _initializeHeadings(){
        var headings = [];
        // West
        if([1,2,3,5,6,10,12,15,16].includes(this._type)){
            headings["w_s"] = [];
            headings["w_d"] = [];
        }
        // North
        if([1,2,3,4,7,11,13,16,17].includes(this._type)){
            headings["n_s"] = [];
            headings["n_d"] = [];
        }
        // East
        if([1,3,4,5,6,8,12,14,17].includes(this._type)){
            headings["e_s"] = [];
            headings["e_d"] = [];
        }
        // South
        if([1,2,4,5,7,9,13,14,15].includes(this._type)){
            headings["s_s"] = [];
            headings["s_d"] = [];
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
     * Draw the direction arrows.
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
     * A direction is considered as a local link: begin and end are on this tile.
     * If a direction exists, it is removed. If a direction does not exist, it will be created. 
     * When direction is created, back- and forwardpropagation is executed to check if
     * any inbound/oubound links can arise
     * @param {string} direction
     */
    toggleDirection(direction){
        // Direction is interpreted as a link on this tile
        // Check if it exists
        var sourceHeading = direction.split("_")[1].toLowerCase();
        var destinationHeading = direction.split("_")[2].toLowerCase();
        var link = this._headings[sourceHeading+"_s"].find((link)=>{
            return link.isLocal && link.startHeading === sourceHeading && link.destinationHeading === destinationHeading
        });

        // If link exists, remove it
        if(link){
            this.removeLink(link);
        }
        // Link does not exist, add it
        else {
            this.createLink(this._id, sourceHeading, this._id, destinationHeading, 0);
            // Check if outbound links arise
            var outboundLinkProperties = this._getOutboundDestination(destinationHeading);
            if(outboundLinkProperties !== null){
                // Create outboundlink for this tile
                this.createLink(this._id, destinationHeading, outboundLinkProperties.destinationId, outboundLinkProperties.destinationHeading, outboundLinkProperties.distance);
                // Create inboundlink for destination
                var destinationTile = builder.getTile(outboundLinkProperties.destinationId);
                destinationTile.createLink(this._id, destinationHeading, outboundLinkProperties.destinationId, outboundLinkProperties.destinationHeading, outboundLinkProperties.distance)
            }
            // Check if inbound links arise
            var inboundLinkProperties = this._getInboundSource(sourceHeading);
            if(inboundLinkProperties !== null){
                // Create inbound link for this tile
                this.createLink(inboundLinkProperties.sourceId, inboundLinkProperties.sourceHeading, this._id, sourceHeading, inboundLinkProperties.distance);
                // Create outbound link for source
                var sourceTile = builder.getTile(inboundLinkProperties.sourceId);
                sourceTile.createLink(inboundLinkProperties.sourceId, inboundLinkProperties.sourceHeading, this._id, sourceHeading, inboundLinkProperties.distance);
            }
        }
    }

    /**
     * Execute backward propagation to analyse if an inbound link can arise towards heading.
     * If backwards propagation identifies a source, properties for future link are returned. 
     * If no source can be identified, null is returned
     * @param {string} heading . inbound heading of this tile  
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
     * Execute forward propagation to analyse if an outbound link can arise, starting from heading.
     * If forward propagation identifies a destination, properties for future link are returned. 
     * If no destination can be identified, null is returned
     * @param {string} heading . outbound heading of this tile 
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
     * has no headings (i.e. straight line tiles and corner tiles), forward propagation is applied on neighbor
     * A heading can accept inbound links if there exists an internal link starting at this heading
     * @param {string} heading 
     * @param {int} distance. Optional parameter for length calculation when recursing. Zero by default. Units: centimeters
     * @return {boolean, string, string, int} accepts, destinationHeading, destinationId, distance
     */
    accepts(heading, distance=0){
        // TODO: catch cases in which neighbor does not exist
        // TODO: catch cases in which incoming heading does not exist
        // Is this a straight line tile or corner tile?
        if([12,13,14,15,16,17].includes(this._type)){
            // It is. Propagate to neighbor
            var oppositeHeading = this._getOppositeHeading(heading);
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
            // Tile has headings (i.e. crossing, parking, traffic light)
            // Is this tile accepting on heading? Tile accepts when this heading has
            // at least 1 local link which starts at this heading
            var accepting = this._headings[heading+"_s"].some((link) => {
                return link.isLocal;
            });

            if(accepting){
                return {accepts:true, destinationHeading: heading, destinationId: this.id,distance:distance+10};
            }
            else{
                return {accepts:false};
            }
        }
    }

    /**
     * Checks if this node sends outbound links from a certain heading. If this tile 
     * has no headings (i.e. straight line tiles and corner tiles), backward propagation is applied on neighbor.
     * A heading can send outbound links if there exists an internal link ending at this heading.
     * @param {string} heading 
     * @param {int} distance. Optional parameter for length calculation when recursing. Zero by default. Units: centimeters
     * @return {boolean, string, string, int} sends, sourceHeading, sourceId, distance
     */
    sends(heading, distance=0){
        // Is this a straight line tile or corner tile?
        if([12,13,14,15,16,17].includes(this._type)){
            // It is. Propagate to neighbor
            var oppositeHeading = this._getOppositeHeading(heading);
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
            // Tile has headings (i.e. crossing, parking, traffic light)
            // Is this tile able to send on heading? Tile is able to send when this heading has
            // at least 1 local link which arrives at this heading
            var sending = this._headings[heading+"_d"].some((link) => {
                return link.isLocal;
            });
            if(sending){
                return {sends:true, sourceHeading: heading, sourceId: this.id,distance:distance+10};
            }
            else{
                return {sends:false};
            }
        }
    }

    /**
     * Stores a link in this tile. If the source is this tile, link is added to source collection of heading.
     * If the destination is this tile, link is added to destination collection of heading
     * @param {Link} link 
     */
    _saveLink(link){
        if(link.startNode === this.id){
            var heading = link.startHeading;
            this._headings[heading+"_s"].push(link);
        }
        if(link.destinationNode === this.id){
            var heading = link.destinationHeading;
            this._headings[heading+"_d"].push(link);
        }
    }

    /**
     * Deletes a link stored in this tile. 
     * @param {Link} link 
     */
    _deleteLink(link){
        // Check in which arrays it will exist
        // delete da ding
        if(link.startNode === this.id){
            var heading = link.startHeading;
            var index;
            // Search for link to remove
            this._headings[heading+"_s"].forEach((l, i) =>{
                if(link.destinationNode === l.destinationNode &&
                    link.startNode === l.startNode &&
                    link.startHeading == l.startHeading &&
                    link.destinationHeading == l.destinationHeading){
                        index = i;
                    }
            });
            this._headings[heading+"_s"].splice(index, 1);
        }
        if(link.destinationNode === this.id){
            var heading = link.destinationHeading;
            var index;
            // Search for link to remove
            this._headings[heading+"_d"].forEach((l, i) =>{
                if(link.destinationNode === l.destinationNode &&
                    link.startNode === l.startNode &&
                    link.startHeading == l.startHeading &&
                    link.destinationHeading == l.destinationHeading){
                        index = i;
                    }
            });
            this._headings[heading+"_d"].splice(index, 1);
        }
    }

    /**
     * Removes a link in which this tile is involved.
     * If it is a local link, do check for status of headings and notify others which have links arriving
     * at link_to_delete_source_heading. Also notify others which have links starting from link_to_delete_source_heading
     * 
     * If link is not local and arriving, just delete it.
     * If link is not local and departing, just delete it.
     * @param {*} link 
     */
    removeLink(link){
        this._deleteLink(link);
        if(link.isLocal){
            // It is a local link, so we need to check the status of the headings in which
            // this link is involved. 
            // Check if source heading can still accept
            var sourceHeading = link.startHeading;
            var accepting = this._headings[sourceHeading+"_s"].some((link) => {
                return link.isLocal;
            });
            if(!accepting){
                // Remove external inbound links to this heading
                this._headings[sourceHeading+"_d"].forEach((link) => {
                    if(!link.isLocal) {
                        this._deleteLink(link); // Remove locally
                        var externalSourceId = link.startNode; 
                        var externalSourceTile = builder.getTile(externalSourceId);
                        externalSourceTile.removeLink(link); // Remove externally
                    } 
                });
            }
            // Check if destination heading can still send 
            var destinationHeading = link.destinationHeading;
            var sending = this._headings[destinationHeading+"_d"].some((link) => {
                return link.isLocal;
            });
            if(!sending){
                // Remove external outbound links starting from this heading
                this._headings[sourceHeading+"_s"].forEach((link) => {
                    if(!link.isLocal) {
                        this._deleteLink(link); // Remove locally
                        var externalDestinationId = link.destinationNode; 
                        var externalDestinationTile = builder.getTile(externalDestinationId);
                        externalDestinationTile.removeLink(link); // Remove externally
                    } 
                });
                // Change color of arrow: link is removed, so arrow must become disabled color
                builder.changeDirectionArrowColor(this._id, sourceHeading+"_"+destinationHeading, "disabled");
            }
        }
        else {
            // Link is not local (i.e. external)
            // Is it an outbound link?
            if(link.startNode === this._id){
                // Source heading of link has lost his outbound connection, it should be marked invalid because
                // it points to nothing (IRL: robot will drive off the grid)
                // Consequence: local links arriving at this heading, must be marked invalid
                this._headings[link.startHeading+"_d"].forEach((l) =>{
                    var direction = l.startHeading + "_" + l.destinationHeading;
                    if(l.isLocal) builder.changeDirectionArrowColor(this._id, direction, "invalid");
                }) ;               
            }
        }
        
    }

    /**
     * Creates a new link in which this tile is involved.
     * Changes color of direction arrows involved with this link to "valid"
     * @param {string} sourceHeading 
     * @param {string} destinationId 
     * @param {String} destinationHeading 
     * @param {number} distance 
     * @param {string} sourceId
     */
    createLink(sourceId, sourceHeading, destinationId, destinationHeading, distance){
        // Create link
        var angle = this._calculateDirectionAngle(sourceHeading, destinationHeading);
        var link = new Link(sourceId, sourceHeading, destinationId, destinationHeading, distance, angle);
        this._saveLink(link);

        // If local link is established, change color of this arrow.
        if(link.isLocal){
            var direction = link.startHeading + "_" + link.destinationHeading;
            // Invalid by default. Link will become valid when outbound is created
            builder.changeDirectionArrowColor(this.id, direction, "invalid");
        }
        // Check if it is an outbound link
        else if (link.startNode === this.id) {
            // Any invalid internal links pointing to sourceHeading will become valid. 
            // Must change color of this arrows.
            this._headings[link.startHeading+"_d"].forEach((l) =>{
                if(l.isLocal) {
                    var direction = l.startHeading + "_" + l.destinationHeading;
                    builder.changeDirectionArrowColor(this.id, direction, "valid");
                }
            });
        }
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