/**
 * Add eventlisteners
 */
var map = null;
var selectedWaypointType = null;
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
    else{
        console.log("No action needed for click");
    }

    // do switch thing here to examine target

    
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
            document.body.style.cursor = "default";
            break;
        case "drone_h":
            // Call viscore to create new item
            var newWaypoint = visualisationCore.drawDroneHelipad(x, y);
            document.body.style.cursor = "default";
            break;
        default:
            console.error("Unknown selected waypoint type");
    }
}
