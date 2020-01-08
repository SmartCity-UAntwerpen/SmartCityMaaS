var _smartcityNamespace = "ss";

var map = null;
var selectedWaypointType = null;
var _counterCarWaypoint = 0;
var _counterDroneWaypoint = 0;
var _counterRobotWaypoint = 0;
var _activeHoverElement = null;
var _activeWaypointElement = null;
var _previewingLink = false;
var _dragging = false;
var _draggingTarget = null;
var _gridActive = false;
const _constants = {
    gridCellSize : 100
}

/**
 * Add eventlisteners
 */
window.addEventListener('load', (event) => {
    console.log('## MapBuilder ##');
    // Draw library
    var libContents = visualisationCore.drawLibrary();
    // Attach onclick handlers to library objects
    for(i = 0;i<libContents.length;++i){
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
    if(type.includes("car_gas")){
        // change mouse pointer to car gas icon
        document.body.style.cursor = "url(car_gas.png) 0 0,auto";
        selectedWaypointType = "car_gas";
    }
    else if(type.includes("drone_h")){
        // change mouse pointer to drone_helipad icon
        document.body.style.cursor = "url(drone_h.png) 0 0,auto";
        selectedWaypointType = "drone_h";
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
    console.log("map click happened");
    var target = event.currentTarget;
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
        if(type.indexOf("wp")>-1){
            _waypointOnClickHandler(event.rangeParent, shiftActive);
        }
        
    }
    // Is the target a link?
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
        //_establishLink(_activeWaypointElement, SVG.find("#"+waypoint.id)[0]);
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
    for(k = i; k<i+2; k++){
        for(l = j; l<j+2; l++){
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
 * Draw a new link between two waypoints
 * Assert that a link is valid (equal waypoint types, no robot)
 * @param {source} pointA 
 * @param {destination} pointB 
 */
function _establishLink(pointA, pointB){
    var linktype = pointA.node.getAttributeNS(_smartcityNamespace, "type");
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
    // Todo: Assert type
   
    // Get type
    var linktype = pointA.node.getAttributeNS(_smartcityNamespace, "type");
    var previewLink;
    if(linktype === "car_wp"){
        previewLink = visualisationCore.drawCarLink(pointA, pointB);
    }
    else if(linktype === "drone_wp"){
        previewLink = visualisationCore.drawDroneLink(pointA, pointB);
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
    switch (object.node.getAttributeNS(_smartcityNamespace, "type")){
        case "drone_link":
            _showLinkProperties(object);
            break;
        case "car_link":
            _showLinkProperties(object);
            break;
        case "drone_wp":
            _showWaypointProperties(object);
            break;
        case "car_wp":
            _showWaypointProperties(object);
            break;
        default:
            console.error("Unknown object type: cannot refresh properties window");
    }
}

/**
 * Clear the fields in the propertieswindow
 */
function _clearPropertiesWindow(){
    var nameTxt = document.getElementById("name");
    nameTxt.value = "";
    var deleteButton = document.getElementById("deleteBtn");
    deleteButton.setAttribute("onclick", "_deleteElement('null')")
}

function _showWaypointProperties(link){
    var wpId = link.attr("id");
    var nameTxt = document.getElementById("name");
    nameTxt.value = wpId;
    var deleteButton = document.getElementById("deleteBtn");
    deleteButton.setAttribute("onclick", "_deleteElement('"+wpId+"')");
}

function _showLinkProperties(link){
    var linkId = link.attr("id");
    var nameTxt = document.getElementById("name");
    nameTxt.value = linkId;
    var deleteButton = document.getElementById("deleteBtn");
    deleteButton.setAttribute("onclick", "_deleteElement('"+linkId+"')");
}



/**
 * Remove an SVG element
 * @param {string} elementId: id of the element to remove
 */
function _deleteElement(elementId){
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
        if(element.node.getAttributeNS(_smartcityNamespace, "type").includes("tile")) return;
        _scaleElementUp(element);
        _activeHoverElement = element;
        // Get the type of the element: link or waypoint
        var elementType = element.node.getAttributeNS(_smartcityNamespace, "type");

        // Type is waypoint
        // Conditions: an activewaypoint element has been chosen, not being equally to the hover target, being a waypoint type and shiftkey is active
        if(_activeWaypointElement && _activeWaypointElement !== element && elementType.indexOf("wp") > -1 && event.shiftKey){
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
        _scaleElementDown(element);
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

/**
 * Creates a new waypoint on the map. The type of the waypoint is stored in selectedwaypointtype
 * @param {int} x xcoord in map coordinates
 * @param {int} y ycoord in map coordinates
 */
function createNewWaypoint(x, y){
    // Place a new waypoint on the map
    // switch (selectedWaypointType){
    //     case "car_gas":
            
    //     case "drone_h":
            
    //     default:
    //         console.error("Unknown selected waypoint type");
    // }
    var newWaypoint = null;
    if(selectedWaypointType.includes("car_gas")){
        // Call viscore to create new item
        newWaypoint = visualisationCore.drawCarGas(x, y);
        newWaypoint.attr('id', "car_wp_"+_counterCarWaypoint++);
        newWaypoint.attr('type', "car_wp", _smartcityNamespace);
    }
    else if(selectedWaypointType.includes("drone_h")){
        // Call viscore to create new item
        newWaypoint = visualisationCore.drawDroneHelipad(x, y);
        newWaypoint.attr('id', "drone_wp_"+_counterDroneWaypoint++);
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
        newWaypoint.attr("id", "robot_wp_" + _counterRobotWaypoint++);
        newWaypoint.attr("type", selectedWaypointType, _smartcityNamespace);
        newWaypoint.attr("type", selectedWaypointType);
        // Size of figures is to small, this is a workaround
        // Should fix in final version
        // TODO
        newWaypoint.scale(1.99,0,0);

        // Attach direction arrows to tile
        _addTileDirectionArrows(newWaypoint);
    
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
 * Adds direction arrows to the tile element (if applicable)
 * @param {*} tileElement . SVG.js element
 */
function _addTileDirectionArrows(tileElement){
    // Get left-top coordinates
    var x = tileElement.transform().translateX;
    var y = tileElement.transform().translateY;
    var cellsize = visualisationCore.gridCellSize;
    switch(tileElement.node.getAttributeNS(_smartcityNamespace, "type")){
        case "robot_tile_2":
            // Attach NW arrows to left top
            var nw_arrows = visualisationCore.drawRobotTileDirectionsNW(0,0,tileElement);
            nw_arrows.scale(0.5,0,0);
            // Attach SW arrows to left bottom
            var sw_arrows = visualisationCore.drawRobotTileDirectionsSW(0, cellsize/4+10, tileElement);
            sw_arrows.scale(0.5,0,0);

            // Attach NS arrows to the right
            // var ns_arrows = visualisationCore.drawRobotTileDirectionsNS(cellsize/4+10, cellsize/4-cellsize/7, tileElement);
            var ns_arrows = visualisationCore.drawRobotTileDirectionsNS(cellsize/4-4, 0, tileElement);
            ns_arrows.scale(0.5,0.9,0,0);

            break;
        case "robot_tile_1":
            // Attach NW arrows to left top
            var nw_arrows = visualisationCore.drawRobotTileDirectionsNW(0,0,tileElement);
            nw_arrows.scale(0.5,0,0);
            // Attach SW arrows to left bottom
            var sw_arrows = visualisationCore.drawRobotTileDirectionsSW(0, cellsize/4+10, tileElement);
            sw_arrows.scale(0.5,0,0);
            // Attach NE arrows to right top
            var nw_arrows = visualisationCore.drawRobotTileDirectionsNE(cellsize/4+10, 0, tileElement);
            nw_arrows.scale(0.5,0,0);
            // Attach ES arrows to right bottom
            var es_arrows = visualisationCore.drawRobotTileDirectionsES(cellsize/4+10, cellsize/4+10, tileElement);
            es_arrows.scale(0.5,0,0);

            // Attach NS, vertical right
            var ns_arrows = visualisationCore.drawRobotTileDirectionsNS(cellsize/4-4, 0, tileElement);
            ns_arrows.scale(0.5,0.9,0,0);
            // Attach EW, horizontal bottom 
            var ew_arrows = visualisationCore.drawRobotTileDirectionsEW(0, cellsize/4-4, tileElement);
            ew_arrows.scale(0.9,0.5,0,0);

            break;
        default:
            // Only crossings and trafficlights can have direction arrows
            break;

    }
}
