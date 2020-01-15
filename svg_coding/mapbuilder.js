import Tile from './mapbuilder_robot_extension.js';
import * as builder from './mapbuilder.js';
import Link from './mapbuilder_link_extension.js';
window.builder = builder;

var _smartcityNamespace = "ss";

var map = null;
var selectedWaypointType = null;
var _counterCarWaypoint = 0;
var _counterDroneWaypoint = 0;
var _activeHoverElement = null;
var _activeWaypointElement = null;
var _dragging = false;
var _draggingTarget = null;
var _gridActive = false;
var _tiles = {};
var _linklocks = [];

/**
 * Add eventlisteners
 */
window.addEventListener('load', (event) => {
    console.log('## MapBuilder ##');
    // Draw library
    var libContents = visualisationCore.drawLibrary();
    // Attach onclick handlers to library objects
    for(var i = 0;i<libContents.length;++i){
        var item = libContents[i];
        item.click(_menuOnClickHandler);
    }

    // Draw grid for robot tiles
    visualisationCore.drawRobotGrid();
    visualisationCore.robotgridgroup.attr("visibility", "hidden");
    visualisationCore.robotgridgroup.children().forEach((gridpoint)=>{
        gridpoint.on('mouseover', _gridpointMouseOver);
        gridpoint.on('mouseout', _gridpointMouseOut);
    })

    // Attach onclick handlers to the map-canvas
    map = document.getElementById("mapcontainer");
    map.addEventListener('click', _mapOnClickHandler);
    map.addEventListener('mousemove', _mapOnMouseMoveHandler);
    map.addEventListener('mouseup', _mapOnMouseUpHandler);
});

/**
 * Handles the click on library items
 * @param {*} event 
 */
function _menuOnClickHandler(event){
    var type = event.currentTarget.getAttribute('type');
    console.log("menu click happened" + type);
    if(type.includes("car_wp")){
        // change mouse pointer to car gas icon
        document.body.style.cursor = "url(car_gas.png) 0 0,auto";
        selectedWaypointType = "car_wp";
    }
    else if(type.includes("drone_wp")){
        // change mouse pointer to drone_helipad icon
        document.body.style.cursor = "url(drone_h.png) 0 0,auto";
        selectedWaypointType = "drone_wp";
    }
    else if(type.includes("robot_tile")){
        // change mouse pointer to drone_helipad icon
        document.body.style.cursor = "url("+type+".png) 0 0,auto";
        selectedWaypointType = type;
        // Activate the grid
        visualisationCore.robotgridgroup.attr("visibility", "visible");
        _gridActive = true;
    }
    else{
        console.error("No valid type detected in _menuOnClickHandler")
    }

}

function _transformScreenCoordsToMapCoords(screenX, screenY){
    var mapOriginX = map.getBoundingClientRect().x;
    var mapOriginY = map.getBoundingClientRect().y;
    var mapX = screenX - mapOriginX;
    var mapY = screenY - mapOriginY;
    return {x:mapX,y:mapY};
}

/**
 * Handles and dispatches click events. A onclick attribute is added to the SVG map as a whole
 * @param {} event 
 */
function _mapOnClickHandler(event){
    // Examine the target:
    // 1) empty spot: target is the svg root element. User wants to create a new waypoint when a selection in library has been made 
    if(event.currentTarget.id === "mapcontainer" && selectedWaypointType !== null){
        // Create new waypoint
        // Translate the coordinates of the mouse pointer to the relative position within
        // the canvas
        const {x,y} = _transformScreenCoordsToMapCoords(event.clientX, event.clientY);
        createNewWaypoint(x,y);
        selectedWaypointType = null;
    }
    // Is the target a waypoint?
    else if(event.rangeParent.getAttributeNS(_smartcityNamespace, "type")){
        var type = event.rangeParent.getAttributeNS(_smartcityNamespace, "type");
        var shiftActive = event.shiftKey;
        // 2) Waypoint: link creation. Target must become active for edit or must become the endpoint of a link creation. 
        // When a waypoint is targetted, we have to select the rangeParent-attribute of the event as target
        if(type.indexOf("wp")>-1 ||type.indexOf("robot_tile") > -1){
            _waypointOnClickHandler(event.rangeParent, shiftActive);
        }
        
    }
    // Is the target a direction-arrow?
    else if(event.target.getAttribute("type")){
        var type = event.target.getAttribute("type");
        if(type.includes("arrow_")){
            console.log("Directional arrow clicked");
            _directionArrowOnClickHandler(event.target);
        }
    }
    // Is the target a drone or racecar link?
    else if(event.target.getAttributeNS(_smartcityNamespace, "type")){
        var type = event.target.getAttributeNS(_smartcityNamespace, "type");
        // 3) Link: show properties.
        if(type.indexOf("link") > -1){
            var object = SVG.find("#"+event.target.id)[0];
            _refreshPropertiesWindow(object);
        }
    }
    // 3) Cancel click
    else{
        _activeWaypointElement = null;
        _clearPropertiesWindow();
        //_activeHoverElement = null;
        console.log("No action needed for click, reset _active properties");
    }
}

/**
 * Processes the click on a waypoint. 2 possible use cases:
 * 1) no other waypoint active. This waypoint becomes active.
 * 2) One other waypoint active and shifting. This waypoint becomes the endpoint of a new link
 * 3) One other waypoint active and not shifting: this waypoint becomes new active waypoint. No link is drawn.
 * @param {svg dom node} waypoint 
 * @param {*} shiftActive: shift has been pressed when clicking
 */
function _waypointOnClickHandler(waypoint, shiftActive){
    if((!_activeWaypointElement) || (!shiftActive && _activeWaypointElement)){
        // Use case 1: no other waypoint active
        // Use case 3: not shifting and already an active waypoint
        _activeWaypointElement = SVG.find("#"+waypoint.id)[0];
        // Update properties window
        _refreshPropertiesWindow(_activeWaypointElement);  
        return;
    }
    else if(shiftActive && _activeWaypointElement){
        // Use case 2: other waypoint already active
        var previousActiveWaypointElement = _activeWaypointElement;
        _activeWaypointElement = SVG.find("#"+waypoint.id)[0];

        // Update properties window
        _refreshPropertiesWindow(_activeWaypointElement);

        // Create a new link between the _activeWaypointElement as source and the targeted element as destination
        _establishLink(previousActiveWaypointElement, _activeWaypointElement);
    }
}

/**
 * Move the drag element to destination x,y. When dragging a node, move the links too
 * @param {*} destX 
 * @param {*} destY 
 */
function _dragElement(destX, destY){
    // update position of _draggingTarget
    _draggingTarget.transform({translateX:destX, translateY:destY});
    var id = _draggingTarget.attr("id");
    // Get center of drag element
    var centerX = _draggingTarget.transform().translateX+(_draggingTarget.width()/2);
    var centerY = _draggingTarget.transform().translateY+(_draggingTarget.height()/2);

    // Find its links
    // Find links with draggingTarget as destination
    var arrivingLinks = SVG.find("[to="+id+"]");
    // Find links with draggingTarget as source
    var departingLinks = SVG.find("[from="+id+"]");
    // Set new position for each link
    arrivingLinks.forEach(link => {
        // set x2, y2 to center of dragging target
        link.attr("x2", centerX);
        link.attr("y2", centerY);
    });
    departingLinks.forEach(link => {
        // set x1, y1 to center of dragging target
        link.attr("x1", centerX);
        link.attr("y1", centerY);
    });
}

/**
 * Highlight gridpoint red when hovering
 * @param {} event 
 */
function _gridpointMouseOver(event){
    var object = SVG.find("#"+event.target.id)[0];
    object.attr("fill", "Red");
}

/**
 * Reset color of gridpoint when mouseover ends
 * @param {*} event 
 */
function _gridpointMouseOut(event){
    var object = SVG.find("#"+event.target.id)[0];
    object.attr("fill", "SlateGrey");
}

/**
 * Handles mousemove event on the map
 * @param {} event 
 */
function _mapOnMouseMoveHandler(event){
    const {x,y} = _transformScreenCoordsToMapCoords(event.clientX, event.clientY);
    if(_dragging){
        // Call draghandler
        _dragElement(x,y);
    }
    else if(_gridActive){
        _toggleGridCellHighlight(x,y);
    }
}

/**
 * Highlights the corners of the cell in which the mousepointer is located
 * @param {int} x . X coordinate of mousepointer (mapcoordinates)
 * @param {int} y . Y coordinate of mousepointer (mapcoordinates)
 */
function _toggleGridCellHighlight(x,y){
    // Determine in which gridcell the mousepointer is,
    // so the corners of this cell kan light up
    const {i,j} = _getGridPosition(x,y);
    //  Reset colors of previously colored corners
    _removeAllGridCellHighlights();   

    // Loop over the corners of this cell
    for(var k = i; k<i+2; k++){
        for(var l = j; l<j+2; l++){
            var cellcorner = SVG.find("#gp_"+k+"_"+l);
            cellcorner.attr("fill", "Red");
        }
    }
}

/**
 *  Reset colors of previously colored corners
 * 
 */
function _removeAllGridCellHighlights(){
    visualisationCore.robotgridgroup.children().forEach((gridpoint)=>{
        gridpoint.attr("fill", "SlateGrey");
    });
}

/**
 * Returns the gridcell in which a coordinate (x,y) is located. Return as {i,j}, i.e. row-i and column-j
 * @param {int} x . X coordinate in mapcoords
 * @param {int} y . X coordinate in mapcoords
 */
function _getGridPosition(x,y){
    var cellI = Math.floor(y/visualisationCore.gridCellSize);
    var cellJ = Math.floor(x/visualisationCore.gridCellSize);
    return {i:cellI,j:cellJ};
}

/**
 * This is a catch for when a user drags too fast, i.e. the mouse up event
 * happens outside the drag target. 
 * @param {} event 
 */
function _mapOnMouseUpHandler(event){
    _elementMouseUpHandler(event);
}

function _elementMouseDownHandler(event){
    console.log("start dragging");
    _dragging = true;
    _draggingTarget = SVG.find("#"+event.currentTarget.id)[0];
}

function _elementMouseUpHandler(event){
    if(_dragging){
        _dragging = false;
        _draggingTarget = null;
        console.log("stop dragging");
    }
}

/**
 * Draw a new link between two waypoints.
 * Assert that a link is valid (equal waypoint types, no robot)
 * @param {'SVG.js object'} pointA . source
 * @param {'SVG.js object'} pointB . destination
 */
function _establishLink(pointA, pointB){
    // Get type
    var pointAType = pointA.node.getAttributeNS(_smartcityNamespace, "type");
    var pointBType = pointB.node.getAttributeNS(_smartcityNamespace, "type");
    var linktype;
    if(pointAType === pointBType){
        linktype = pointAType;
    }
    var link;
    if(linktype === "car_wp"){
        link = visualisationCore.drawCarLink(pointA, pointB);
        link.attr("id", "car_link_" + pointA.attr("id") + "_" + pointB.attr("id"));
        link.attr('type', "car_link", _smartcityNamespace);    
    }
    else if(linktype === "drone_wp"){
        link = visualisationCore.drawDroneLink(pointA, pointB);
        link.attr("id", "drone_link_" + pointA.attr("id") + "_" + pointB.attr("id"));
        link.attr('type', "drone_link", _smartcityNamespace);
    }
    else{
        // Type is Transitlink
        link = visualisationCore.drawTransitLink(pointA, pointB);
        link.attr("id", "transit_link-" + pointA.attr("id") + "-" + pointB.attr("id"));
        link.attr('type', "transit_link", _smartcityNamespace);
    }
    // Set from and to attributes
    link.attr("from", pointA.attr("id"));
    link.attr("to", pointB.attr("id"));
    // Attach eventlisteners for hovering
    link.on('mouseover', _elementHoverInHandler);
    link.on('mouseout', _elementHoverOutHandler);

    // Link is established, end previewing
    _activeWaypointElement = null;
}

/**
 * 
 * @param {*} pointA 
 * @param {*} pointB 
 */
function _previewLink(pointA, pointB){
    // Get type
    var pointAType = pointA.node.getAttributeNS(_smartcityNamespace, "type");
    var pointBType = pointB.node.getAttributeNS(_smartcityNamespace, "type");
    var linktype;
    if(pointAType === pointBType){
        linktype = pointAType;
    }
    else{
        linktype = "transit";
    }
    var previewLink;
    if(linktype === "car_wp"){
        previewLink = visualisationCore.drawCarLink(pointA, pointB);
    }
    else if(linktype === "drone_wp"){
        previewLink = visualisationCore.drawDroneLink(pointA, pointB);
    }
    else{
        // Linktype is Transit
        previewLink = visualisationCore.drawTransitLink(pointA, pointB);
    }
    previewLink.attr("id", "linkpreview");
}

function _removeLinkPreview(){
    var oldlink = SVG.find("#linkpreview");
    if(oldlink[0]){
        console.log("removing old link");
        oldlink[0].remove();
    }
}


/**
 * Refresh the properties window for given object
 * @param {SVG.js node} object 
 */
function _refreshPropertiesWindow(object){
    var type = object.node.getAttributeNS(_smartcityNamespace, "type")
    if(type === "drone_link" || type === "car_link"){
        _showLinkProperties(object);
    }
    else {
        _showWaypointProperties(object);
    }
}

/**
 * Clear the fields in the propertieswindow
 */
function _clearPropertiesWindow(){
    var nameTxt = document.getElementById("name");
    nameTxt.value = "";
    var deleteButton = document.getElementById("deleteBtn");
    deleteButton.setAttribute("onclick", "builder.deleteElement('null')")
}

function _showWaypointProperties(link){
    var wpId = link.attr("id");
    var nameTxt = document.getElementById("name");
    nameTxt.value = wpId;
    var deleteButton = document.getElementById("deleteBtn");
    deleteButton.setAttribute("onclick", "builder.deleteElement('"+wpId+"')");
}

function _showLinkProperties(link){
    var linkId = link.attr("id");
    var nameTxt = document.getElementById("name");
    nameTxt.value = linkId;
    var deleteButton = document.getElementById("deleteBtn");
    deleteButton.setAttribute("onclick", "builder.deleteElement('"+linkId+"')");
}

/**
 * Remove an SVG element
 * @param {string} elementId: id of the element to remove
 */
export function deleteElement(elementId){
    if(!elementId){
        console.log("Cannot delete null element");
        return;
    }
    var element = SVG.find("#"+elementId)[0];
    // If element is a waypoint, we need to delete its links
    if(element.node.getAttributeNS(_smartcityNamespace, "type") === "drone_wp" || element.node.getAttributeNS(_smartcityNamespace, "type") === "car_wp"){
        // Find its links
        // Find links with draggingTarget as destination
        var arrivingLinks = SVG.find("[to="+elementId+"]");
        // Find links with draggingTarget as source
        var departingLinks = SVG.find("[from="+elementId+"]");
        // Remove links
        arrivingLinks.forEach((link) => {link.remove()});
        departingLinks.forEach((link) => {link.remove()});
    }
   
    // Remove element itself
    element.remove();

    // Clear the propertieswindow
    _clearPropertiesWindow();

}

/**
 * Mousein on an element. Start hovering, increase scale
 * If there is an active waypoint, draw a provisoir link
 * @param {} event 
 */
function _elementHoverInHandler(event){
    if(_dragging) return;
    if(!_activeHoverElement){
        var target = event.currentTarget;
        var element = SVG.find("#"+target.id)[0];
        if(!element.node.getAttributeNS(_smartcityNamespace, "type").includes("tile")) _scaleElementUp(element);
        _activeHoverElement = element;
        // Get the type of the element: link or waypoint
        var elementType = element.node.getAttributeNS(_smartcityNamespace, "type");

        // Type is waypoint
        // Conditions: an activewaypoint element has been chosen, not being equally to the hover target, being a waypoint type and shiftkey is active
        if(_activeWaypointElement && _activeWaypointElement !== element && (elementType.indexOf("wp") > -1 || elementType.indexOf("tile") > -1)&& event.shiftKey){
            // User is choosing target for new link. Visualize a preview
            _previewLink(_activeWaypointElement, _activeHoverElement);
        }
        // Type is link
        else if(elementType.indexOf("link") > -1){
            // nothing special should happen
        }
    }
}

/**
 * Mouseout on an element. Means end of hovering, decrease scale, remove link preview (if any)
 * @param {} event 
 */
function _elementHoverOutHandler(event){
    if(_activeHoverElement){
        //console.log("element mouseout happened");
        var target = event.currentTarget;
        var element = SVG.find("#"+target.id);
        if(!element[0].node.getAttributeNS(_smartcityNamespace, "type").includes("tile")) _scaleElementDown(element);
        _activeHoverElement = null;
        _removeLinkPreview();
    }
}

function _scaleElementUp(element){
    element.scale(1.2);
}

function _scaleElementDown(element){
    element.scale(1/1.2);
}

export function directionArrowHoverIn(event){
    console.log("hovering");
    var node = SVG(event.target);
    node.attr("stroke-opacity", 0.5);
}

export function directionArrowHoverOut(event){
    var node = SVG(event.target);
    node.attr("stroke-opacity", 1);
}

/**
 * Creates a new waypoint on the map. The type of the waypoint is stored in selectedwaypointtype
 * @param {int} x . xcoord in map coordinates
 * @param {int} y . ycoord in map coordinates
 * @param {string} id . Optional: if provided, element is given this id
 */
function createNewWaypoint(x, y, id=null){
    // Place a new waypoint on the map
    var newWaypoint = null;
    if(selectedWaypointType.includes("car_wp")){
        // Call viscore to create new item
        newWaypoint = visualisationCore.drawCarGas(x, y);
        if(!id) {
            id = "car_wp_"+_counterCarWaypoint++;
        }
        else {
            _counterCarWaypoint++;
        }

        newWaypoint.attr('id', id);
        newWaypoint.attr('type', "car_wp", _smartcityNamespace);
    }
    else if(selectedWaypointType.includes("drone_wp")){
        // Call viscore to create new item
        newWaypoint = visualisationCore.drawDroneHelipad(x, y);
        if(!id){
            id = "drone_wp_"+_counterDroneWaypoint++;
        } 
        else{
            _counterDroneWaypoint++;
        }
        newWaypoint.attr('id', id);
        // We want type as an attribute in our own XML namespace
        newWaypoint.attr('type', "drone_wp", _smartcityNamespace);
    }
    else if(selectedWaypointType.includes("robot_tile")){
        // Determine in which cellgrid this tile is dropped
        const {i,j} = _getGridPosition(x,y);
        // Calculate X and Y coordinate of left-top of gridcell
        // At this coordinate, the tile will be placed
        const xTile = j*visualisationCore.gridCellSize;
        const yTile = i*visualisationCore.gridCellSize;
        _removeAllGridCellHighlights();
        switch(selectedWaypointType){
            case "robot_tile_1":        
                newWaypoint = visualisationCore.drawRobotTile1(xTile,yTile);
                break;
            case "robot_tile_2":        
                newWaypoint = visualisationCore.drawRobotTile2(xTile,yTile);
                break;
            case "robot_tile_3":        
                newWaypoint = visualisationCore.drawRobotTile3(xTile,yTile);
                break;
            case "robot_tile_4":        
                newWaypoint = visualisationCore.drawRobotTile4(xTile,yTile);
                break;
            case "robot_tile_5":        
                newWaypoint = visualisationCore.drawRobotTile5(xTile,yTile);
                break;
            case "robot_tile_6":        
                newWaypoint = visualisationCore.drawRobotTile6(xTile,yTile);
                break;
            case "robot_tile_7":        
                newWaypoint = visualisationCore.drawRobotTile7(xTile,yTile);
                break;
            case "robot_tile_8":        
                newWaypoint = visualisationCore.drawRobotTile8(xTile,yTile);
                break;
            case "robot_tile_9":        
                newWaypoint = visualisationCore.drawRobotTile9(xTile,yTile);
                break;
            case "robot_tile_10":        
                newWaypoint = visualisationCore.drawRobotTile10(xTile,yTile);
                break;
            case "robot_tile_11":        
                newWaypoint = visualisationCore.drawRobotTile11(xTile,yTile);
                break;
            case "robot_tile_12":        
                newWaypoint = visualisationCore.drawRobotTile12(xTile,yTile);
                break;
            case "robot_tile_13":        
                newWaypoint = visualisationCore.drawRobotTile13(xTile,yTile);
                break;
            case "robot_tile_14":        
                newWaypoint = visualisationCore.drawRobotTile14(xTile,yTile);
                break;
            case "robot_tile_15":        
                newWaypoint = visualisationCore.drawRobotTile15(xTile,yTile);
                break;
            case "robot_tile_16":        
                newWaypoint = visualisationCore.drawRobotTile16(xTile,yTile);
                break;
            case "robot_tile_17":        
                newWaypoint = visualisationCore.drawRobotTile17(xTile,yTile);
                break;
            default:
                console.error("Unknown robot tile type");
                return;
        }
        if(!id) id = "robot_wp_" + i + "_" + j
        newWaypoint.attr("id", id);
        newWaypoint.attr("type", selectedWaypointType, _smartcityNamespace);
        newWaypoint.attr("type", selectedWaypointType);
        var tile = new Tile(newWaypoint);
        _tiles[tile.id] = tile;
        // Size of figures is to small, this is a workaround
        // Should fix in future version
        // TODO
        newWaypoint.scale(1.99,0,0);
    }
    
    newWaypoint.on('mouseover', _elementHoverInHandler);
    newWaypoint.on('mouseout', _elementHoverOutHandler);
    newWaypoint.on('mousedown', _elementMouseDownHandler);
    newWaypoint.on('mouseup', _elementMouseUpHandler);

    document.body.style.cursor = "default";
    _gridActive = false;
    visualisationCore.robotgridgroup.attr("visibility", "visible");
}

/**
 * Handles the click op a direction arrow of a robot tile. 
 * The status of the link (enabled or disabled) is toggled.
 * @param {DOM node} arrow 
 */
function _directionArrowOnClickHandler(arrow){
    // Get SVG.js node for arrow
    var node = SVG(arrow);
    // Find parent tile of this arrow
    // The tile to which this arrow belongs, is located 2 
    // levels up in the DOM tree
    var parentNode = node.parent().parent();

    // Get tile object
    var tile = _tiles[parentNode.attr("id")];
    // Get status of this direction from the tile
    tile.toggleDirection(node.attr("type"));
}

/**
 * Colors a direction arrow according to its status
 * @param {string} status . valid | invalid | disabled
 * @param {string} tileId . robot_wp_i_j
 * @param {string} direction . N_E, N_W, E_N, E_S, S_E, S_W, W_N, W_E, N_S, S_N, E_W, W_E
 * 
 */
export function changeDirectionArrowColor(tileId, direction, status){
    // Find SVG node for robottile
    var tileNode = SVG.find("#"+tileId);
    direction = direction.toUpperCase();
    direction = "arrow_"+direction;
    var arrowNode = tileNode.find("[type="+direction+"]")[0];
    _colorDirectionArrow(arrowNode, status);
}

/**
 * Changes color of a direction arrow, which is passed as an SVG.js node
 * @param {*} node 
 * @param {*} status 
 */
function _colorDirectionArrow(node, status){
    if(status === "valid"){
        // Set color to green
        node.attr("stroke", "YellowGreen");
    }
    else if(status === "disabled"){
        // Set color to blue
        node.attr("stroke", "CornflowerBlue");
    }
    else if(status === "invalid"){
        // Set color to red
        node.attr("stroke", "IndianRed");
    }
}

/**
 * Returns tile
 * @param {String} id . ID of the tile
 * @return {Tile} tile with corresponding id
 */
export function getTile(id){
    return _tiles[id];
}

/**
 * Exports the contents of the map to a JSON format
 * @returns {Object} map structure
 */
export function exportJSONMap(){
    var succes = true;
    // Output structure
    var output = {drone:{points: [], links: []},
                    racecar: {points: [], links: []},
                    robot: {tiles: [], links: [], locks: []},
                    transitlinks: []};

    // Loop over drone waypoints
    var drone_waypoints = SVG.find("[type=drone_wp]");
    drone_waypoints.forEach((element) =>{
        if(element.attr("id")){
            var transform = element.transform();
            var drone_point  = {x:transform.translateX, y: transform.translateY, id: element.attr("id")}
            output.drone.points.push(drone_point);
        }
    });
    // Loop over racecar waypoints
    var racecar_waypoints = SVG.find("[type=car_wp]");
    racecar_waypoints.forEach((element) =>{
        if(element.attr("id")){
            var transform = element.transform();
            var racecar_point  = {x:transform.translateX, y: transform.translateY, id: element.attr("id")}
            output.racecar.points.push(racecar_point);
        }
    });
    // Loop over drone- and car- and transitlinks
    var carAndDroneLinks = SVG.find("#links")[0].children();
    carAndDroneLinks.forEach((element)=>{
        var link = {from: element.attr("from"), to:element.attr("to"), id:element.attr("id")}
        if (element.attr("type") === "drone_link"){
            output.drone.links.push(link);
        }
        else if(element.attr("type") === "car_link"){
            output.racecar.links.push(link);
        }
        else if(element.attr("type") === "transit_link"){
            output.transitlinks.push(link);
        }
    });
    // Loop over Tiles
    for(var id in _tiles){
        var tile = _tiles[id];
        var transform = SVG.find("#"+id)[0].transform();
        output.robot.tiles.push({type: tile.type, x: transform.translateX, y:transform.translateY, id:tile.id});
        // Consider only links at heading_start, both local and external types
        // Links at heading_destination are duplicates from other (external) links at heading_start
        for (var headingId in tile.headings){
            if(headingId.split("_")[1] === "s"){
                // Loop over links in this heading
                tile.headings[headingId].forEach((link) =>{
                    // Check if this link is valid
                    if(link.status === "valid") {
                        // Get linklock
                        var lockId = _getLinkLockId(link);
                        output.robot.locks.push({id:lockId});
                        link._lockId = lockId;
                        output.robot.links.push(link);
                    }
                    if(link.status === "invalid"){
                        succes = false;
                        window.alert("Please solve invalid robot links before exporting the map.");
                    }
                });
                
            }
        }
    }
    if(succes) {
        return (output)
    }
    else{
        return {error: "Could not parse JSON due to invalid Robot links"};
    }
}

/**
 * Construct map which is defined in JSON structure.
 * @param {Object} map . 
 */
export function importJSONMap(map){
    // Import drone waypoints
    selectedWaypointType = "drone_wp";
    map.drone.points.forEach((point) => {
        createNewWaypoint(point.x, point.y, point.id);
    });
    // Import drone links
    map.drone.links.forEach((link)=>{
        // startpoint of node
        var from = SVG.find("#"+link.from)[0];
        var to = SVG.find("#"+link.to)[0];
        _establishLink(from, to);
    });
    // Import racecar waypoints
    selectedWaypointType = "car_wp";
    map.racecar.points.forEach((point) => {
        createNewWaypoint(point.x, point.y, point.id);
    });
    // Import racecar links
    map.racecar.links.forEach((link) =>{
        var from = SVG.find("#"+link.from)[0];
        var to = SVG.find("#"+link.to)[0];
        _establishLink(from, to);
    });
    // Import robot tiles
    map.robot.tiles.forEach((tile)=>{
        selectedWaypointType = "robot_tile_"+tile.type;
        createNewWaypoint(tile.x, tile.y, tile.id);
    });
    // Import transit links
    map.transitlinks.forEach((link) =>{
        var from = SVG.find("#"+link.from)[0];
        var to = SVG.find("#"+link.to)[0];
        _establishLink(from, to);
    });
    // Import robot links
    map.robot.links.forEach((link)=>{
        // We construct only local links, a.k.a. directions. Non-local links will be
        // generated implicitly by the tiles itself
        // Get link headings to deduct type
        if(link._isLocal){
            var type = "arrow_" + link._startHeading.toUpperCase() + "_" + link._destinationHeading.toUpperCase();
            var tileId = link._startNode;
            var tile = getTile(tileId);
            tile.toggleDirection(type);
        }
    });

}

/**
 * Ask locknumber for link. Each tile combination of start and destination has a unique lock.
 * E.g.: Local links (i.e. links within the same tile) have the same linklock. Two external links between
 * same tiles share also the a linklock.
 * 
 * @param {Link} link 
 */
function _getLinkLockId(link){
    // Sort start and destination alphabetically. A lock is multi-directional.
    var tileCombo = (link.startNode < link.destinationNode) ? {from: link.startNode, to: link.destinationNode} : {from: link.destinationNode, to: link.startNode}
    var index = _linklocks.indexOf(tileCombo);
    if(index > -1){
        return index;
    }
    else {
        // array.push returns the new length of the array. We want the index to which
        // this newly inserted tileCombo belongs. Index of last element = length-1
        return _linklocks.push(tileCombo)-1;
    }
}