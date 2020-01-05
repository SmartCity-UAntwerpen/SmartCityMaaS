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
    switch(type){
        case "car_gas":
            // change mouse pointer to car gas icon
            document.body.style.cursor = "url(car_gas.png) 0 0,auto";
            selectedWaypointType = "car_gas";
            break;
        case "drone_h":
            // change mouse pointer to drone_helipad icon
            document.body.style.cursor = "url(drone_h.png) 0 0,auto";
            selectedWaypointType = "drone_h";
            break;
        default:
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
            _updatePropertiesWindow(event.rangeParent);
        }
    }
    // 3) Cancel click
    else{
        _activeWaypointElement = null;
        //_activeHoverElement = null;
        console.log("No action needed for click, reset _active properties");

    }
}

/**
 * Show the properties of element in the properties window
 * @param {} element 
 */
function _updatePropertiesWindow(element){
    // Treat the element as a SVG.js object, not as a SVG DOM element
    var object = SVG.find("#"+waypoint.id)[0];

    // Parsing of properties depends on object type
    // Todo: implement propertieswindow
}

/**
 * Processes the click on a waypoint. 2 possible use cases:
 * 1) no other waypoint active. This waypoint becomes active.
 * 2) One other waypoint active and shifting. This waypoint becomes the endpoint of a new link
 * 3) One other waypoint active and not shifting: this waypoint becomes new active waypoint. No link is drawn.
 * @param {*} waypoint 
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
 * Handles mousemove event on the map
 * @param {} event 
 */
function _mapOnMouseMoveHandler(event){
    if(_dragging){
        const {x,y} = _transformScreenCoordsToMapCoords(event.clientX, event.clientY);
        _dragElement(x,y);
    }
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
            _showDroneLinkProperties(object);
            break;
        case "drone_wp":
            _showDroneWaypointProperties(object);
            break;
        default:
            console.error("Unknown object type: cannot refresh properties window");
    }
    
}

function _showDroneWaypointProperties(link){
    var wpId = link.attr("id");
    var nameTxt = document.getElementById("name");
    nameTxt.value = wpId;
    var deleteButton = document.getElementById("deleteBtn");
    deleteButton.setAttribute("onclick", "_deleteElement('"+wpId+"')")
}

function _showDroneLinkProperties(link){
    var linkId = link.attr("id");
    var nameTxt = document.getElementById("name");
    nameTxt.value = linkId;
    var deleteButton = document.getElementById("deleteBtn");
    deleteButton.setAttribute("onclick", "_deleteElement('"+linkId+"')")
}

function _deleteElement(elementId){
    if(!elementId){
        console.log("helloooo");
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
 * @param {*} x 
 * @param {*} y 
 */
function createNewWaypoint(x, y){
    // Place a new waypoint on the map
    switch (selectedWaypointType){
        case "car_gas":
            // Call viscore to create new item
            var newWaypoint = visualisationCore.drawCarGas(x, y);
            newWaypoint.attr('id', "car_wp_"+_counterCarWaypoint++);
            newWaypoint.attr('type', "car_wp", _smartcityNamespace);
            document.body.style.cursor = "default";
            break;
        case "drone_h":
            // Call viscore to create new item
            var newWaypoint = visualisationCore.drawDroneHelipad(x, y);
            newWaypoint.attr('id', "drone_wp_"+_counterDroneWaypoint++);
            // We want type as an attribute in our own XML namespace
            newWaypoint.attr('type', "drone_wp", _smartcityNamespace);
            document.body.style.cursor = "default";
            break;
        default:
            console.error("Unknown selected waypoint type");
    }
    newWaypoint.on('mouseover', _elementHoverInHandler);
    newWaypoint.on('mouseout', _elementHoverOutHandler);
    newWaypoint.on('mousedown', _elementMouseDownHandler);
    newWaypoint.on('mouseup', _elementMouseUpHandler);
}
