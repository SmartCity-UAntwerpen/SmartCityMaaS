/**
 * Created by Frédéric Melaerts on 11/05/2017.
 */

var mapCanvas;
var mapCanvasContext;
var world = [];
var worldLoaded = false;

// A point index in the html page
var pointA_x = -1;
var pointA_y = -1;
// A point index in the world
var pointA_x_cell = 0;
var pointA_y_cell = 0;
var pointA_ID = -100;

// B point index in the html page
var pointB_x = -1;
var pointB_y = -1;
// B point index in the world
var pointB_x_cell = 0;
var pointB_y_cell = 0;
var pointB_ID = -100;

var pointA_set = false;
var pointB_set = false;
var x_size;
var y_size;

var change_color = "null";
// Redraw the corresponding point or the whole map
var redraw_onPointA = false;
var redraw_onPointB = false;
var map_ready = false;
var control_initDraw = false;
var progress;






var xSize = 0;
var ySize = 0;
var pointAset = false;
var pointBset = false;

var pointAId = -10;
var pointBId = -10;

/**
 * The start function to allow drawing on th mapCanvas of the html.
 * Set the right intervals drawing and progress control.
 */
function start() {
    mapCanvas = document.getElementById("mapCanvas");
    mapCanvasContext = mapCanvas.getContext("2d");
    initdraw();
    //drawMap();
    if(only_view == false)
    {

    }else
    {
        if( visualization == true)
        {
            setInterval(getProgress, 2000);
            setInterval(initdraw, 3000);
        }else
        {
            setInterval(getProgress, 2000);
        }
    }
}


/**
 * Initial draw of the map, where no vehicles are included.
 */
function initdraw()
{
    x_size = mapCanvas.width/world.dimensionY;
    y_size = mapCanvas.height/world.dimensionX;


    console.log("x size = " + x_size);
    for(var i=0; i<world.dimensionY; i++) {
        for (var j = 0; j < world.dimensionX; j++) {
            var cell = world.cells[i].cellList[j];
            var columns = x_size * j;
            var rows = y_size * i;

            if (cell.type.localeCompare("spot") == 0 || cell.type.localeCompare("surrounding_point") == 0) {
                //console.log("Map info x " +cell.specific);
                if (cell.characteristic.localeCompare("INTERSECTION") == 0 || cell.characteristic.localeCompare("LIGHT") == 0) {
                    mapCanvasContext.drawImage(cellRoad, columns, rows, x_size, y_size);
                }else {
                    if (cell.specific.localeCompare("drone") == 0) {
                        mapCanvasContext.drawImage(spot_drone, columns, rows, x_size, y_size);
                    } else if (cell.specific.localeCompare("car") == 0) {
                        mapCanvasContext.drawImage(spot_car, columns, rows, x_size, y_size);
                    } else {
                        mapCanvasContext.drawImage(spot_robot, columns, rows, x_size, y_size);
                    }
                }
            } else if (cell.type.localeCompare("road") == 0) {

                //console.log( " Road cell.specific "+cell.specific);
                if (cell.specific.localeCompare("drone") == 0) {
                    mapCanvasContext.drawImage(spot_drone, columns, rows, x_size, y_size);
                } else if (cell.specific.localeCompare("car") == 0) {
                    mapCanvasContext.drawImage(spot_car, columns, rows, x_size, y_size);
                } else if (cell.specific.localeCompare("robot") == 0){
                    mapCanvasContext.drawImage(spot_robot, columns, rows, x_size, y_size);
                }else
                {
                    mapCanvasContext.drawImage(cellRoad, columns, rows, x_size, y_size);
                }

            } else {
                mapCanvasContext.drawImage(cellBackground, columns, rows, x_size, y_size);
            }
            if(only_view && !visualization)
            {
                if (cell.spotID == pointA_Value) {
                    mapCanvasContext.drawImage(cellPointA, columns, rows, x_size, y_size);
                }
                if(cell.spotID == pointB_Value)
                {
                    mapCanvasContext.drawImage(cellPointB, columns, rows, x_size, y_size);
                }
            }
        }
    }
    mapCanvasContext.drawImage(legend, 0, mapCanvas.height - mapCanvas.height/4, 200, 200);

    map_ready = true;
}
/**
 * Load all the images from the html.
 */
function loadImages() {
    /*cellBackground = new Image();
    cellBackground.src = cellImageBackGround;
    cellRoad = new Image();
    cellRoad.src = cellImageRoad;
    cellPointA = new Image();
    cellPointA.src = pointAImage;
    cellPointB = new Image();
    cellPointB.src = pointBImage;
    spot_robot = new Image();
    spot_robot.src = spotImage;
    surrounding = new Image();
    surrounding.src = surroundImage;
    spot_drone = new Image();
    spot_drone.src = cellImageSpotDrone;
    road_drone = new Image();
    road_drone.src = cellImageRoadDrone;
    vehicle = new Image();
    vehicle.src = cellImageDrone;
    spot_car = new Image();
    spot_car.src = cellImageSpotCar;
    legend = new Image();
    legend.src = legendeWorld;*/

}

/**
 * Retrieve the physical world from the MaaS.
 */
function getWorld(){
    $.getJSON("/retrieveWorld", function(result){
        world = result;
        worldLoaded = true;
        console.log("JSON WORLD");
        //start();
        console.log("allo" + world.points[1].type);
        console.log("world x = " + world.dimensionX + " world y = " + world.dimensionY );
        console.log("mapX = " + document.getElementById("mapCanvas").offsetWidth + " mapY = " + document.getElementById("mapCanvas").offsetHeight);
        xSize = (document.getElementById("mapCanvas").offsetWidth/world.dimensionX);
        ySize = (document.getElementById("mapCanvas").offsetHeight/world.dimensionY);
        console.log(" x size = " + xSize + " y size = " + ySize);
        drawWorldNC();
    });
}


function drawWorldNC(){
    console.log("world point size = " + world.points.length);
    mapCanvas = document.getElementById("mapCanvas");
    mapCanvas.addEventListener("mousedown", clickedOnCanvas, false);
    var ctx = mapCanvas.getContext("2d");

    for(var i=0; i<world.points.length; i++){
        var point = world.points[i];
        console.log("point " + i + " x = " +  point.physicalPoisionX + " y = "+point.physicalPoisionY + " charachterestic = " + point.pointCharacteristic + " name = " + point.pointName);
        ctx.fillRect(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize,xSize,ySize); // fill in the pixel at (10,10)
        for(var j=0; j < point.neighbours.length; j++){
            var neigbourID = point.neighbours[j];
            var neigbour = world.points[neigbourID];
            if(true){
                ctx.beginPath();
                if(point.physicalPoisionX < neigbour.physicalPoisionX){
                    ctx.moveTo(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize);
                    var middle = ((neigbour.physicalPoisionX - point.physicalPoisionX)/2) + point.physicalPoisionX;
                    ctx.lineTo(middle*xSize,neigbour.physicalPoisionY*ySize);
                    ctx.lineTo(neigbour.physicalPoisionX*xSize,neigbour.physicalPoisionY*ySize);
                } else if(point.physicalPoisionX > neigbour.physicalPoisionX ){
                    ctx.moveTo(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize);
                    var middle = ((point.physicalPoisionX - neigbour.physicalPoisionX )/2) + neigbour.physicalPoisionX;
                    ctx.lineTo(middle*xSize,neigbour.physicalPoisionY*ySize);
                    ctx.lineTo(neigbour.physicalPoisionX*xSize,neigbour.physicalPoisionY*ySize);


                } else{
                    ctx.moveTo(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize);
                    ctx.lineTo(neigbour.physicalPoisionX*xSize,neigbour.physicalPoisionY*ySize);
                }
                switch (point.type) {
                    case "robot":
                        ctx.strokeStyle = '#e74c3c';
                        break;
                    case "car":
                        ctx.strokeStyle = '#2980b9';
                        break;
                    case "drone":
                        ctx.strokeStyle = '#27ae60';
                }
                ctx.stroke();
            }
        }
    }

    map_ready = true;


}
/**
 * JQuery that retrieves the progress for a vehicle of a specific delivery.
 * All vehicles progress is requested when visualisation is true.
 */


/**
 * This function is performed on every mouse click
 * It controls the if the position of the mouse click is set on a spot/surrouding point and if a point A or B is
 * activated or deactivated
 * @param e
 */

$('#myMapCanvas').onmousedown( clickedOnCanvas(event) );
//$('#myMapCanvas').click( function(){ alert("test"); } );

function clickedOnCanvas(event){
    var mapCanvas = document.getElementById("mapCanvas");
    var ctx = mapCanvas.getContext("2d");
    var rect = mapCanvas.getBoundingClientRect();
    var canvasX = event.clientX - rect.left;
    var canvasY = event.clientY - rect.top;
    console.log("Point clicked x = " + canvasX + "y = " + canvasY);
    if(pointAset){
        var point = world.points[pointAId];
        if(canvasX >= point.physicalPoisionX*xSize && canvasX <= point.physicalPoisionX*xSize + xSize && canvasY >= point.physicalPoisionY*ySize && canvasY <= point.physicalPoisionY*ySize + ySize){
            console.log("turning off point A");
            pointAset = false;
            pointAId = -10;
            ctx.fillStyle="#000000";
            ctx.fillRect(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize,xSize,ySize);
            document.getElementById('saveDelivery').style.visibility = 'hidden';
            return;
        }
    }
    if(pointBset){
        var point = world.points[pointBId];
        if(canvasX >= point.physicalPoisionX*xSize && canvasX <= point.physicalPoisionX*xSize + xSize && canvasY >= point.physicalPoisionY*ySize && canvasY <= point.physicalPoisionY*ySize + ySize){
            console.log("turning off point B");
            pointBset = false;
            pointBId = -10;
            ctx.fillStyle="#000000";
            ctx.fillRect(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize,xSize,ySize);
            document.getElementById('saveDelivery').style.visibility = 'hidden';
            return;
        }
    }
    if(!pointAset || !pointBset){
        for(var i=0; i<world.points.length; i++) {
            var point = world.points[i];
            if(canvasX >= point.physicalPoisionX*xSize && canvasX <= point.physicalPoisionX*xSize + xSize && canvasY >= point.physicalPoisionY*ySize && canvasY <= point.physicalPoisionY*ySize + ySize){
                if(!pointAset){
                    console.log("Point A id = " + i);
                    ctx.fillStyle="#FF0000";
                    pointAId = i;
                    pointAset = true;
                    document.getElementById("inputA").value = point.pointName;
                    if(pointBset){
                        document.getElementById('saveDelivery').style.visibility = 'visible';
                    }
                }
                else{
                    console.log("Point B id = " + i);
                    ctx.fillStyle="#0000FF";
                    pointBId = i;
                    pointBset = true;
                    document.getElementById("inputB").value = point.pointName;
                    document.getElementById('saveDelivery').style.visibility = 'visible';

                }
                ctx.fillRect(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize,xSize,ySize);
                return;
           }
        }
    }
}

function onClick(e) {
    console.log("MOUSE CLICK x "+ e.pageX + " en y " +e.pageY);
    var map = document.getElementById('myMapCanvas');
    console.log("Map offset x "+ map.offsetLeft + " en y " +map.offsetTop);
    console.log("DELIVERY FROM THYMLEAF " +pointA);
     var x_click = e.pageX-map.offsetLeft ;
   // var x_click = e.pageX;
    var y_click = e.pageY-map.offsetTop;
    console.log("COMBO info x "+ x_click + " en y " +y_click);
    var sideVariation_X = (0.5 + world.surround_layer)*x_size;
    var sideVariation_Y = (0.5 + world.surround_layer)*y_size;
    if(legitArea(x_click, y_click) == true) {
        var midpointA_X = pointA_x+(x_size/2);
        var midpointA_Y = pointA_y+(y_size/2);
        if (pointA_set == true) {
            if (midpointA_X - sideVariation_X < x_click && x_click < midpointA_X + sideVariation_X && midpointA_Y-sideVariation_Y < y_click && y_click < midpointA_Y + sideVariation_Y) {
                console.log(":'(");
                pointA_x = x_click;
                pointA_y = y_click;
                pointA_ID = -100;
                redraw_onPointA = false;
                pointA_set = false;
                if (pointB_set == true) {
                    pointB_x = x_click;
                    pointB_y = y_click;
                    pointB_set = false;
                    pointB_ID = -100;
                    redraw_onPointB = false;
                }
                document.getElementById('saveDelivery').style.visibility = 'hidden';
                initdraw();
            } else {
                if (pointB_set == true) {
                    var midpointB_X = pointB_x+(x_size/2);
                    var midpointB_Y = pointB_y+(y_size/2);
                    if (midpointB_X - sideVariation_X < x_click && x_click < midpointB_X + sideVariation_X && midpointB_Y - sideVariation_Y < y_click && y_click < midpointB_Y + sideVariation_Y) {
                        pointB_x = x_click;
                        pointB_y = y_click;
                        pointB_set = false;
                        pointB_ID = -100;
                        redraw_onPointB = false;
                        document.getElementById('saveDelivery').style.visibility = 'hidden';
                        // Draw first init, so that the original B point is certainly cleared from the canvas.
                        initdraw();
                    }
                } else {
                    pointB_x = x_click;
                    pointB_y = y_click;
                    pointB_set = true;
                    document.getElementById('saveDelivery').style.visibility = 'visible';
                }
                drawMap();
            }
        } else {
            pointA_x = x_click;
            pointA_y = y_click;
            pointA_set = true;
            document.getElementById('saveDelivery').style.visibility = 'hidden';
            drawMap();
        }
    }
    console.log("current user "+document.getElementById("userName").value)
}


/**
 * The initialize function of the html page.
 */
function initFunction() {
    showPage();
    loadImages();
    getWorld();

    if(visualization){
        setInterval(getVehiclesVN, 250);
    }

    if(!only_view)
    {
        // Hide save button, this button will only be showed when both of the points are selected.
        document.getElementById('saveDelivery').style.visibility = 'hidden';
    }
    // Execute start after 700 milliseconds
    //setTimeout(start, 1000)

}

/**
 * Shows a loading animation when data is loaded.
 * When all information is tranferred to the html page, the functional elements are shown while to loading animation
 * is hidden.
 */
function showPage() {
    if(map_ready == false) {
        if(only_view == false)
        {
            // Hide the devices,labels,... and show them when the that og the map is loaded.
            document.getElementById("content").style.visibility = "hidden";
            document.getElementById("passengersLabel").style.visibility = "hidden";
            document.getElementById("passengersSelect").style.visibility = "hidden";
            document.getElementById("closeButton").style.visibility = "hidden";
        }else
        {
            if(visualization == false) {
                document.getElementById("content").style.visibility = "hidden";
                document.getElementById("passengersLabel").style.visibility = "hidden";
                document.getElementById("passengersNumber").style.visibility = "hidden";
                document.getElementById("pointALabel").style.visibility = "hidden";
                document.getElementById("pointAtext").style.visibility = "hidden";
                document.getElementById("pointBLabel").style.visibility = "hidden";
                document.getElementById("pointBtext").style.visibility = "hidden";
                document.getElementById("closeButton").style.visibility = "hidden";
                document.getElementById("deliveryIDLabel").style.visibility = "hidden";
                document.getElementById("deliveryID").style.visibility = "hidden";
                document.getElementById("deliveryDone").style.visibility = "hidden";

            }else
            {
                document.getElementById("closeButton").style.visibility = "hidden";
                document.getElementById("reloadButton").style.visibility = "hidden";
                document.getElementById("table_vehicles").style.visibility = "hidden";
            }
        }
    }else
    {
        if(only_view == false)
        {
            document.getElementById("loader").style.display = "none";
            document.getElementById("content").style.visibility = "visible";
            document.getElementById("passengersLabel").style.visibility = "visible";
            document.getElementById("passengersSelect").style.visibility = "visible";
            document.getElementById("closeButton").style.visibility = "visible";
        }else
            if(visualization == false) {
                document.getElementById("loader").style.display = "none";
                document.getElementById("content").style.visibility = "visible";
                document.getElementById("passengersLabel").style.visibility = "visible";
                document.getElementById("passengersNumber").style.visibility = "visible";
                document.getElementById("pointALabel").style.visibility = "visible";
                document.getElementById("pointAtext").style.visibility = "visible";
                document.getElementById("pointBLabel").style.visibility = "visible";
                document.getElementById("pointBtext").style.visibility = "visible";
                document.getElementById("closeButton").style.visibility = "visible";
                document.getElementById("deliveryIDLabel").style.visibility = "visible";
                document.getElementById("deliveryID").style.visibility = "visible";
            }else
            {
                document.getElementById("loader").style.display = "none";
                document.getElementById("closeButton").style.visibility = "visible";
                document.getElementById("reloadButton").style.visibility = "visible";
                document.getElementById("table_vehicles").style.visibility = "visible";
            }
            if(!control_initDraw)
            {
                console.log("Start drawing map");
                control_initDraw = true;
                //initdraw();
                console.log("Drawing complete");
            }
    }
}

/*function getProgress(){

    if( visualization == true)
    {

        //console.log("URL requested "+ URL_Progress)




    }else
    {
        var URL_Progress = "/world1/progress/"+id_delivery+"/0";
        //console.log("URL requested "+ URL_Progress)
        $.getJSON(URL_Progress, function(result){
            progress = result;
            // console.log("Result: "+progress[0]+" - " + progress[1]);
            // Controls if x index of progress is an allowed value
            if(progress[0] != -1)
            {
                mapCanvasContext.drawImage(vehicle, progress[0]*x_size, progress[1]*y_size, x_size, y_size);
            }else
            {
                document.getElementById("deliveryDone").style.visibility = "visible";
            }
        });
    }
}*/



function getVehiclesVN(){
    var mapCanvas = document.getElementById("mapCanvas");
    var ctx = mapCanvas.getContext("2d");
    var URL_Progress = "/world1/allVehicles";
    $.getJSON(URL_Progress, function(result){
        var allVehicles = result;
        for(var j=0; j<allVehicles.length; j++) {
            var URL_Progress = "/world1/progress/null/"+allVehicles[j];
            console.log("progress of vehicle " + j );
            $.getJSON(URL_Progress, function(result){
                //progress values = x,y,vehicleID
                progress = result;
                console.log("Result: "+progress[0]+" - " + progress[1]);
                // Controls if x index of progress is an allowed value
                if(progress[0] != -1)
                {
                    if(currentVehicleID != -1)
                    {

                        if(currentVehicleID == progress[2])
                        {
                            ctx.fillStyle="#9b59b6";
                            ctx.fillRect(progress[0]*xSize,progress[1]*ySize,xSize,ySize);
                            console.log("Vehicle 1 "+ currentVehicleID +" allVehicles[j] "+ progressVehicleID);
                        }else
                        {
                            ctx.fillStyle="#95a5a6";
                            ctx.fillRect(progress[0]*xSize,progress[1]*ySize,xSize,ySize);
                            console.log("Vehicle 2 "+ currentVehicleID +" allVehicles[j] "+ progressVehicleID);
                        }
                    }else
                    {
                        console.log("no vehicle clicked");
                        ctx.fillStyle="#ffffff";
                        ctx.fillRect(progress[0]*xSize,progress[1]*ySize,xSize,ySize);

                    }
                }
            });
        }
    });


}


