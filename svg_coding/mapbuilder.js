var _smartcityNamespace = "http://smartcity.dyndns.net";

var map = null;
var selectedWaypointType = null;
var _counterCarWaypoint = 0;
var _counterDroneWaypoint = 0;
var _counterRobotWaypoint = 0;
var _activeHoverElement = null;
var _activeWaypointElement = null;
var _previewingLink = false;

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
});

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

function _mapOnClickHandler(event){
    console.log("map click happened");
    var target = event.currentTarget;
    // Examine the target:
    // 1) empty spot: target is the svg root element. User wants to create a new waypoint
    if(event.currentTarget.id === "mapcontainer" && selectedWaypointType !== null){
        // Create new waypoint
        // Translate the coordinates of the mouse pointer to the relative position within
        // the canvas
        var mapOriginX = map.getBoundingClientRect().x;
        var mapOriginY = map.getBoundingClientRect().y;
        var x = event.clientX - mapOriginX;
        var y = event.clientY - mapOriginY;
        createNewWaypoint(x,y);
        selectedWaypointType = null;
    }
    // 2) Waypoint: target must become active for edit or must become the endpoint of a link creation. 
    // When a waypoint is targetted, we have to select the rangeParent-attribute of the event as target
    else if(event.rangeParent.getAttributeNS(_smartcityNamespace, "type")){
        if(event.rangeParent.getAttributeNS(_smartcityNamespace, "type").indexOf("wp")>0){
            _waypointOnClickHandler(event.rangeParent);
        }
    }
    else{
        _activeWaypointElement = null;
        _activeHoverElement = null;
        console.log("No action needed for click, reset _active properties");

    }

    // do switch thing here to examine target

    
}

/**
 * Processes the click on a waypoint. 2 possible use cases:
 * 1) no other waypoint active. This waypoint becomes active.
 * 2) One other waypoint active. This waypoint becomes the endpoint of a new link
 * @param {*} waypoint 
 */
function _waypointOnClickHandler(waypoint){
    if(!_activeWaypointElement){
        // Use case 1: no other waypoint active
        _activeWaypointElement = SVG.find("#"+waypoint.id)[0];
        // Update properties window
        _refreshPropertiesWindow(_activeWaypointElement);
    }
    else{
        // Use case 2: other waypoint already active
        // Create a new link
        _establishLink(_activeWaypointElement, waypoint);
    }
}

/**
 * Draw a new link between two waypoints
 * Assert that a link is valid (equal waypoint types, no robot)
 * @param {source} pointA 
 * @param {destination} pointB 
 */
function _establishLink(pointA, pointB){
    console.log("should establish link function implement");
}

/**
 * 
 * @param {*} pointA 
 * @param {*} pointB 
 */
function _previewLink(pointA, pointB){
    console.log("Todo: implement preview link");
    // Assert type
   
    // Get type
    var linktype = pointA.node.getAttributeNS(_smartcityNamespace, "type");
    if(linktype === "car_wp"){
        var previewLink = visualisationCore.drawDroneLink(pointA, pointB);
        previewLink.attr("id", "linkpreview")
    }
    

}

function _removeLinkPreview(){
    var oldlink = SVG.find("#linkpreview");
    if(oldlink[0]){
        console.log("removing old link");
        oldlink[0].remove();
    }
}


/**
 * Refresh the properties window
 * @param {element under consideration} eUC 
 */
function _refreshPropertiesWindow(eUC){
    console.log("Todo: implement refreshpropertieswindow");
}

/**
 * Mousein on an element. Start hovering, increase scale
 * If there is an active waypoint, draw a provisoir link
 * @param {} event 
 */
function _elementHoverInHandler(event){
    console.log("element mousein happened");
    if(!_activeHoverElement){
        var target = event.currentTarget;
        var element = SVG.find("#"+target.id)[0];
        _scaleElementUp(element);
        _activeHoverElement = element;
        if(_activeWaypointElement && _activeWaypointElement !== element){
            // User is choosing target for new link. Visualize a preview
            _previewLink(_activeWaypointElement, _activeHoverElement);
        }
    }
}

/**
 * Mouseout on an element. Means end of hovering, decrease scale.
 * @param {} event 
 */
function _elementHoverOutHandler(event){
    if(_activeHoverElement){
        console.log("element mouseout happened");
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
    

    // Draw a new element on the map
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
            newWaypoint.attr('type', "drone_wp", _smartcityNamespace);
            document.body.style.cursor = "default";
            break;
        default:
            console.error("Unknown selected waypoint type");
    }
    newWaypoint.on('mouseover', _elementHoverInHandler);
    newWaypoint.on('mouseout', _elementHoverOutHandler);
}
