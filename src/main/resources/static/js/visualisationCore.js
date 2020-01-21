
var mapDrawer;
var _linksgroup;
var svg;
var ratio;
var zoomFactor = 1.5;

// We save the original values from the viewBox
var viewBox = {
    x: 0,
    y: 0,
    width: 2000,
    height: 2000
};

// The distances calculated from the pointer will be stored here
var newViewBox = {
    x: 0,
    y: 0
};

// Calculate the ratio based on the viewBox width and the SVG width
window.addEventListener('resize', function() {
    ratio = viewBox.width / svg.getBoundingClientRect().width;
});

window.addEventListener('load', (event) => {
    console.log('## SmartCity SVG Visualisation Core ##');
    mapDrawer = SVG().addTo('#map').size(visualisationCore.canvasWidth, visualisationCore.canvasHeight).id("mapcontainer");
    mapDrawer.attr("xmlns:ss", "http://smartcity.ddns.net");
    if (typeof hideMapBuilder === 'undefined') {
        libraryDrawer = SVG().addTo('#library').size(180,1000).id("librarycontainer");
    }
    mapDrawer.viewbox(0, 0, visualisationCore.canvasWidth, visualisationCore.canvasHeight);
    _linksgroup = mapDrawer.group();
    _linksgroup.attr("id", "links");
    visualisationCore.robotgridgroup = mapDrawer.group();
    visualisationCore.robotgridgroup.attr("id", "robotgrid");

    if (typeof hideMapBuilder === 'undefined') { return; }

// We select the SVG into the page
    svg = document.querySelector('svg');

// If browser supports pointer events
    if (window.PointerEvent) {
        svg.addEventListener('pointerdown', onPointerDown); // Pointer is pressed
        svg.addEventListener('pointerup', onPointerUp); // Releasing the pointer
        svg.addEventListener('pointerleave', onPointerUp); // Pointer gets out of the SVG area
        svg.addEventListener('pointermove', onPointerMove); // Pointer is moving
    } else {
        // Add all mouse events listeners fallback
        svg.addEventListener('mousedown', onPointerDown); // Pressing the mouse
        svg.addEventListener('mouseup', onPointerUp); // Releasing the mouse
        svg.addEventListener('mouseleave', onPointerUp); // Mouse gets out of the SVG area
        svg.addEventListener('mousemove', onPointerMove); // Mouse is moving

        // Add all touch events listeners fallback
        svg.addEventListener('touchstart', onPointerDown); // Finger is touching the screen
        svg.addEventListener('touchend', onPointerUp); // Finger is no longer touching the screen
        svg.addEventListener('touchmove', onPointerMove); // Finger is moving
    }

    ratio = viewBox.width / svg.getBoundingClientRect().width;




    // Insert your testcode below

    //draw.svg(drone_icon);
    //draw.svg(car_gas);


  });
var visualisationCore = {
    canvasWidth : 1300,
    canvasHeight : 1300,
    robotgridgroup: null,
    gridCellSize : 100,
    iconSize: 60,
    droneCircleRadius : 200,
    helloWorld: "Hi there",
    sayHelloWorld : function(a){
        console.log(a);
        return "You asked me to say hello world. Hello world.";
    },
    sayHelloWorldByConsultant : function(){
        return _sayHelloWorldByConsultant();
    },
    drawCarGas : function(x, y){
        return this._drawAndTranslate(x,y,car_gas,mapDrawer);
    },
    drawCarGasStartPoint : function(x,y){
        return this._drawAndTranslate(x,y,car_gas_A,mapDrawer);
    },
    drawCarGasEndPoint : function(x,y){
        return this._drawAndTranslate(x,y,car_gas_B,mapDrawer);
    },
    drawDroneHelipad : function(x,y){
        return this._drawAndTranslate(x,y,drone_h,mapDrawer);
    },
    drawDroneHelipadStartPoint : function(x,y){
        return this._drawAndTranslate(x,y,drone_h_pointA,mapDrawer);
    },
    drawDroneHelipadEndPoint : function(x,y){
        return this._drawAndTranslate(x,y,drone_h_pointB,mapDrawer);
    },
    drawDrone : function(x,y){
        return this._drawTranslateAndCenter(x,y,drone2,mapDrawer, 40, 50);
    },
    drawDroneTargetted : function(x,y){
        return this._drawAndTranslate(x,y,drone_icon_targetted,mapDrawer);
    },
    drawRaceCar : function(x,y){
        var obj = this._drawAndTranslate(x,y,intersection2,mapDrawer);
        //obj.scale(8);
        return obj;
    },
    drawRaceCarTargetted : function(x,y){
        var obj = this._drawAndTranslate(x,y,raceCarTargetted,mapDrawer);
        //obj.transform({scale:10})
        obj.scale(8); // Racecar is drawed on a smaller size
        return obj;
    },
    drawRobotCharge : function(x,y){
        return this._drawAndTranslate(x,y,robotcharge,mapDrawer);
    },
    drawRobot : function(x,y){
        return this._drawTranslateAndCenter(x,y,robot2 ,mapDrawer, 40, 30);
    },
    drawRobotStartPoint : function(x,y){
        return this._drawAndTranslate(x,y,robotcharge_pointA,mapDrawer);
    },
    drawRobotEndPoint : function(x,y){
        return this._drawAndTranslate(x,y,robotcharge_pointB,mapDrawer);
    },
    drawRobotIcon : function(x,y){
        var obj = this._drawAndTranslate(x,y,roboticon,mapDrawer);
        obj.height(40);
        obj.height(40);
        return obj;
    },
    drawRobotIconTargetted : function(x,y){
        return this._drawAndTranslate(x,y,robotTargetted,mapDrawer);
    },
    drawDroneLink : function(pointA, pointB){
        // Calculate center of start and endpoint
        var xStart = pointA.transform().translateX+(pointA.width()/2);
        var yStart = pointA.transform().translateY+(pointA.height()/2);
        var xEnd = pointB.transform().translateX+(pointB.width()/2);
        var yEnd = pointB.transform().translateY+(pointB.height()/2);
        var link = _linksgroup.line(xStart, yStart, xEnd, yEnd).stroke({ color: "RoyalBlue",width: 3, dasharray:"10,10" });
        return link;
    },
    drawCarLink : function(pointA, pointB){
        // Calculate center of start and endpoint
        var xStart = pointA.transform().translateX+(pointA.width()/2);
        var yStart = pointA.transform().translateY+(pointA.height()/2);
        var xEnd = pointB.transform().translateX+(pointB.width()/2);
        var yEnd = pointB.transform().translateY+(pointB.height()/2);
        var link = _linksgroup.line(xStart, yStart, xEnd, yEnd).stroke({ color: "grey",width: 4});
        return link;
    },
    drawCar : function(x,y){
        return this._drawTranslateAndCenter(x,y,racecarIcon2 ,mapDrawer, 40, 70);
    },
    drawTransitLink : function(pointA, pointB){
        // Calculate center of start and endpoint
        var xStart = pointA.transform().translateX+(pointA.width()/2);
        var yStart = pointA.transform().translateY+(pointA.height()/2);
        var xEnd = pointB.transform().translateX+(pointB.width()/2);
        var yEnd = pointB.transform().translateY+(pointB.height()/2);
        var link = _linksgroup.line(xStart, yStart, xEnd, yEnd).stroke({ color: "Orange",width: 4});
        return link;
    },
    drawRobotGrid : function(){
        var columns = Math.floor(this.canvasWidth/this.gridCellSize);
        var rows = Math.floor(this.canvasHeight/this.gridCellSize);
        for(var i = 0; i<rows;i++){
            for(var j = 0; j<columns; j++){
                var x = i*this.gridCellSize;
                var y = j*this.gridCellSize;
                var gridcell = this._drawAndTranslate(x,y, gridpoint, this.robotgridgroup);
                gridcell.attr("id", "gp_"+j+"_"+i);
            }
        }
    },

    drawRobotTile : function(tileID, x, y) {
        if (tileID < 1 || tileID > 17) {
            return this.drawRobotCharge(x,y);
        }
        var obj = this._drawAndTranslate(x,y,eval("robottile_"+tileID),mapDrawer);
        return obj.scale(1.99,0,0);


    },

    drawRobotTile1 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_1,mapDrawer);
    },
    drawRobotTile2 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_2,mapDrawer);
    },
    drawRobotTile3 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_3,mapDrawer);
    },
    drawRobotTile4 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_4,mapDrawer);
    },
    drawRobotTile5 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_5,mapDrawer);
    },
    drawRobotTile6 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_6,mapDrawer);
    },
    drawRobotTile7 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_7,mapDrawer);
    },
    drawRobotTile8 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_8,mapDrawer);
    },
    drawRobotTile9 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_9,mapDrawer);
    },
    drawRobotTile10 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_10,mapDrawer);
    },
    drawRobotTile11 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_11,mapDrawer);
    },
    drawRobotTile12 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_12,mapDrawer);
    },
    drawRobotTile13: function(x,y){
        return this._drawAndTranslate(x,y,robottile_13,mapDrawer);
    },
    drawRobotTile14 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_14,mapDrawer);
    },
    drawRobotTile15 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_15,mapDrawer);
    },
    drawRobotTile16 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_16,mapDrawer);
    },
    drawRobotTile17 : function(x,y){
        return this._drawAndTranslate(x,y,robottile_17,mapDrawer);
    },
    drawRobotTileDirectionsSW : function(x,y, tile){
        return this._drawAndTranslate(x,y,robottile_directions_SW,tile);
    },
    drawRobotTileDirectionsEW : function(x,y, tile){
        return this._drawAndTranslate(x,y,robottile_directions_EW,tile);
    },
    drawRobotTileDirectionsES : function(x,y, tile){
        return this._drawAndTranslate(x,y,robottile_directions_ES,tile);
    },
    drawRobotTileDirectionsNW : function(x,y, tile){
        return this._drawAndTranslate(x,y,robottile_directions_NW,tile);
    },
    drawRobotTileDirectionsNS : function(x,y, tile){
        return this._drawAndTranslate(x,y,robottile_directions_NS,tile);
    },
    drawRobotTileDirectionsNE : function(x,y, tile){
        return this._drawAndTranslate(x,y,robottile_directions_NE,tile);
    },
    drawDroneCircle : function(x,y){
        return this._drawAndTranslate(x,y,droneWaypointCircle, mapDrawer);
    },

    drawLibrary : function(){
        // Plot a library for the mapbuilder
        // Car gas, drone helipad, robot tiles
        // Displayed in a grid of 2 by ..
        // Dimensions of each cell are 90px*90px
        var library = [car_gas, drone_h, robottile_1,robottile_2,
            robottile_3, robottile_4, robottile_5, robottile_6,
            robottile_7, robottile_8, robottile_9, robottile_10,
            robottile_11, robottile_12,robottile_13, robottile_14,
            robottile_15, robottile_16,robottile_17];
        //var library = [car_gas, drone_h, robottile_1];
        var objects = [];
        // todo: add robot tiles to this library
        var hMax = 1000;
        var hDelta = 90;
        var wMax = 180;
        var wDelta = 90;
        var x=0;
        var y = 0;
        for(const figure in library){
            var object = this._drawAndTranslate(x,y,library[figure],libraryDrawer);
            x += wDelta;
            if(x >= wMax){
                x = 0;
                y += hDelta;
            }
            objects.push(object);
        }
        return objects;

    },
    _drawAndTranslate : function(x,y, template, drawer){
        var obj = drawer.svg(template);
        obj = obj.last();
        obj.transform({translateX:x,translateY:y});
        return obj;
    },

    _drawTranslateAndCenter : function(x,y, template, drawer, height, width){
        let offsetX = this.iconSize/2;
        let offsetY = this.iconSize/2;
        var obj = drawer.svg(template);
        obj = obj.last();
        obj.height(height);
        obj.width(width);
        obj.center(x + offsetX, y + offsetY);
        //obj.transform({translateX:x+offsetX,translateY:y+offsetY});
        return obj;
    },

    // Moves the center of the 'drawing' object to the given coordinates.
    moveTo : function(drawing, x, y, inGrid) { // 'inGrid' will determine if the size of the grid is important
        let offsetX;
        let offsetY;
        if (inGrid) {
            offsetX = this.gridCellSize/2;
            offsetY = this.gridCellSize/2;
        } else {
            offsetX = this.iconSize/2;
            offsetY = this.iconSize/2;
        }

        drawing.animate().center(x+offsetX,y+offsetY);
    },

    zoomIn : function () {
        let box = mapDrawer.viewbox();
        mapDrawer.animate().viewbox(box.x, box.y, box.width / zoomFactor, box.height / box.width * (box.width / zoomFactor))
    },

    zoomOut : function () {
        let box = mapDrawer.viewbox();
        mapDrawer.animate().viewbox(box.x, box.y, box.width * zoomFactor, box.height / box.width * (box.width * zoomFactor))
    },
    drawIntersection : function(x,y){
        return this._drawAndTranslate(x,y,intersection,mapDrawer);
    },
    drawRobotLink : function(pointA, pointB){
        // Calculate center of start and endpoint
        var xStart = pointA.transform().translateX+(pointA.width()/2);
        var yStart = pointA.transform().translateY+(pointA.height()/2);
        var xEnd = pointB.transform().translateX+(pointB.width()/2);
        var yEnd = pointB.transform().translateY+(pointB.height()/2);
        var link = _linksgroup.line(xStart, yStart, xEnd, yEnd).stroke({ color: "orange",width: 8});
        return link;
    },
};

function _sayHelloWorldByConsultant(){
    return "ciao";
}

// This function returns an object with X & Y values from the pointer event
function getPointFromEvent (event) {
    var point = {x:0, y:0};
    // If even is triggered by a touch event, we get the position of the first finger
    if (event.targetTouches) {
        point.x = event.targetTouches[0].clientX;
        point.y = event.targetTouches[0].clientY;
    } else {
        point.x = event.clientX;
        point.y = event.clientY;
    }

    return point;
}

// This variable will be used later for move events to check if pointer is down or not
var isPointerDown = false;

// This variable will contain the original coordinates when the user start pressing the mouse or touching the screen
var pointerOrigin = {
    x: 0,
    y: 0
};

// Function called by the event listeners when user start pressing/touching
function onPointerDown(event) {
    isPointerDown = true; // We set the pointer as down

    // We get the pointer position on click/touchdown so we can get the value once the user starts to drag
    var pointerPosition = getPointFromEvent(event);
    pointerOrigin.x = pointerPosition.x;
    pointerOrigin.y = pointerPosition.y;
}



// Function called by the event listeners when user start moving/dragging
function onPointerMove (event) {
    // Only run this function if the pointer is down
    if (!isPointerDown) {
        return;
    }
    // This prevent user to do a selection on the page
    event.preventDefault();

    var box = mapDrawer.viewbox();

    // Get the pointer position
    var pointerPosition = getPointFromEvent(event);

    // We calculate the distance between the pointer origin and the current position
    // The viewBox x & y values must be calculated from the original values and the distances
    newViewBox.x = viewBox.x - ((pointerPosition.x - pointerOrigin.x) * ratio);
    newViewBox.y = viewBox.y - ((pointerPosition.y - pointerOrigin.y) * ratio);

    // We create a string with the new viewBox values
    // The X & Y values are equal to the current viewBox minus the calculated distances
    var viewBoxString = `${newViewBox.x} ${newViewBox.y} ${box.width} ${box.height}`;
    // We apply the new viewBox values onto the SVG
    svg.setAttribute('viewBox', viewBoxString);

    //document.querySelector('.viewbox').innerHTML = viewBoxString;
}

function onPointerUp() {
    // The pointer is no longer considered as down
    isPointerDown = false;

    // We save the viewBox coordinates based on the last pointer offsets
    viewBox.x = newViewBox.x;
    viewBox.y = newViewBox.y;
}
