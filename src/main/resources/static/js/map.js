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

var linksDrawn = [];

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
 * Load all the images from the html.
 */

var droneDefault;
var dronePointA;
var dronePointB;
var carDefault;
var carPointA;
var carPointB;
var robotDefault;
var robotPointA;
var robotPointB;

function loadImages() {
    droneDefault = new Image();
    droneDefault.src = "../images/map/drone_h.png";
    dronePointA = new Image();
    dronePointA.src = "../images/map/drone_h_pointA.png";
    dronePointB = new Image();
    dronePointB.src = "../images/map/drone_h_pointB.png";
    carDefault = new Image();
    carDefault.src = "../images/map/car_gas.png";
    carPointA = new Image();
    carPointA.src = "../images/map/car_gas_pointA.png";
    carPointB = new Image();
    carPointB.src = "../images/map/car_gas_pointB.png";
    robotDefault = new Image();
    robotDefault.src = "../images/map/robot_charge.png";
    robotPointA = new Image();
    robotPointA.src = "../images/map/robot_charge_pointA.png";
    robotPointB = new Image();
    robotPointB.src = "../images/map/robot_charge_pointB.png";

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


        var ratio = world.dimensionX/world.dimensionY;
        $("#mapCanvas").width($("#myMapCanvas").width()).height( $("#myMapCanvas").width()/ratio ).attr("width", $("#mapCanvas").width()).attr("height", $("#mapCanvas").height());



        console.log("mapX = " + document.getElementById("mapCanvas").offsetWidth + " mapY = " + document.getElementById("mapCanvas").offsetHeight);
        xSize = (document.getElementById("mapCanvas").offsetWidth/world.dimensionX);
        ySize = (document.getElementById("mapCanvas").offsetHeight/world.dimensionY);
        console.log(" x size = " + xSize + " y size = " + ySize);
        drawWorldNC();
        showPage();
        console.log("values"+ linksDrawn);
    });
}


function drawWorldNC(){
    console.log("world point size = " + world.points.length);
    mapCanvas = document.getElementById("mapCanvas");
    mapCanvas.addEventListener("mousedown", clickedOnCanvas, false);
    var ctx = mapCanvas.getContext("2d");

    for(var i=world.points.length-1; i>=0; i--){
        var point = world.points[i];
        console.log("point " + i + " x = " +  point.physicalPoisionX + " y = "+point.physicalPoisionY + " charachterestic = " + point.pointCharacteristic + " name = " + point.pointName);

        //ctx.fillRect(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize,xSize,ySize); // fill in the pixel at (10,10)
        for(var j=0; j < point.neighbours.length; j++){
            var neigbourID = point.neighbours[j];
            var neigbour = world.points[neigbourID];
            var linkAlreadyDrawn = false;
            for(var k=0; k < linksDrawn.length; k++){
                if(linksDrawn[k].end == i && linksDrawn[k].start == neigbourID){
                    linkAlreadyDrawn = true;
                }
            }
            if(!linkAlreadyDrawn){
                ctx.beginPath();
                if(point.physicalPoisionX < neigbour.physicalPoisionX){
                    ctx.moveTo(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize);
                    var middle = ((neigbour.physicalPoisionX - point.physicalPoisionX)/2) + point.physicalPoisionX;
                    //ctx.lineTo(middle*xSize,neigbour.physicalPoisionY*ySize);
                    ctx.lineTo(neigbour.physicalPoisionX*xSize,neigbour.physicalPoisionY*ySize);
                } else if(point.physicalPoisionX > neigbour.physicalPoisionX ){
                    ctx.moveTo(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize);
                    var middle = ((point.physicalPoisionX - neigbour.physicalPoisionX )/2) + neigbour.physicalPoisionX;
                    //ctx.lineTo(middle*xSize,neigbour.physicalPoisionY*ySize);
                    ctx.lineTo(neigbour.physicalPoisionX*xSize,neigbour.physicalPoisionY*ySize);


                } else{
                    ctx.moveTo(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize);
                    ctx.lineTo(neigbour.physicalPoisionX*xSize,neigbour.physicalPoisionY*ySize);
                }
                switch (point.type) {
                    case "robot":
                        ctx.strokeStyle = '#e67e22';
                        break;
                    case "car":
                        ctx.strokeStyle = '#95a5a6';
                        break;
                    case "drone":
                        ctx.setLineDash([5, 15]);
                        ctx.strokeStyle = '#2980b9';
                }
                ctx.lineWidth=5;
                ctx.stroke();
                linksDrawn.push({start:i,end: neigbourID});
            }
        }

    }

    for(var i=0; i<world.points.length; i++) {
        var point = world.points[i];
        switch (point.type) {
            case "robot":
                if( point.pointCharacteristic == "INTERSECTION"){
                    ctx.strokeStyle = "#95A6A6";
                    ctx.fillRect((point.physicalPoisionX * xSize)- xSize*1.5/2, (point.physicalPoisionY * ySize)- ySize*1.5/2, xSize*1.5, ySize*1.5); // fill in the pixel at (10,10)
                }else if( point.pointCharacteristic == "LIGHT" ){
                    ctx.strokeStyle = "";
                    ctx.fillRect((point.physicalPoisionX * xSize)- xSize*1.5/2, (point.physicalPoisionY * ySize)- ySize*1.5/2, xSize*1.5, ySize*1.5); // fill in the pixel at (10,10)
                } else {
                ctx.drawImage(robotDefault, (point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                }
                break;
            case "car":
                ctx.drawImage(carDefault,(point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                break;
            case "drone":
                ctx.drawImage(droneDefault,(point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                break;
        }

    }

    map_ready = true;



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
        if(canvasX >= point.physicalPoisionX*xSize - xSize*3/2 && canvasX <= point.physicalPoisionX*xSize + xSize*3/2 && canvasY >= point.physicalPoisionY*ySize - ySize*3/2 && canvasY <= point.physicalPoisionY*ySize + ySize*3/2){
            switch (point.type) {
                case "robot":
                    if( point.pointCharacteristic == "INTERSECTION" || point.pointCharacteristic == "LIGHT" ){
                        return;
                    }
                    ctx.drawImage(robotDefault, (point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                    break;
                case "car":
                    ctx.drawImage(carDefault,(point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                    break;
                case "drone":
                    ctx.drawImage(droneDefault,(point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                    break;
            }
            document.getElementById('saveDelivery').style.visibility = 'hidden';
            console.log("turning off point A");
            pointAset = false;
            pointAId = -10;
            return;
        }
    }
    if(pointBset){
        var point = world.points[pointBId];
        if(canvasX >= point.physicalPoisionX*xSize - xSize*3/2 && canvasX <= point.physicalPoisionX*xSize + xSize*3/2 && canvasY >= point.physicalPoisionY*ySize - ySize*3/2 && canvasY <= point.physicalPoisionY*ySize + ySize*3/2){
            switch (point.type) {
                case "robot":
                    if( point.pointCharacteristic == "INTERSECTION" || point.pointCharacteristic == "LIGHT"){
                        return;
                    }
                    ctx.drawImage(robotDefault, (point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                    break;
                case "car":
                    ctx.drawImage(carDefault,(point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                    break;
                case "drone":
                    ctx.drawImage(droneDefault,(point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                    break;
            }
            console.log("turning off point B");
            pointBset = false;
            pointBId = -10;
            document.getElementById('saveDelivery').style.visibility = 'hidden';
            return;
        }
    }
    if(!pointAset || !pointBset){
        for(var i=0; i<world.points.length; i++) {
            var point = world.points[i];
            if(canvasX >= point.physicalPoisionX*xSize - xSize*3/2 && canvasX <= point.physicalPoisionX*xSize + xSize*3/2 && canvasY >= point.physicalPoisionY*ySize - ySize*3/2 && canvasY <= point.physicalPoisionY*ySize + ySize*3/2){
                if(!pointAset){
                    console.log("Point A id = " + i);
                    switch (point.type) {
                        case "robot":
                            if( point.pointCharacteristic == "INTERSECTION" || point.pointCharacteristic == "LIGHT" ){
                                return;
                            }
                            ctx.drawImage(robotPointA, (point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                            break;
                        case "car":
                            ctx.drawImage(carPointA,(point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                            break;
                        case "drone":
                            ctx.drawImage(dronePointA,(point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                            break;
                    }
                    pointAId = i;
                    pointAset = true;
                    document.getElementById("inputA").value = point.pointName;
                    if(pointBset){
                        document.getElementById('saveDelivery').style.visibility = 'visible';
                    }
                }
                else{
                    switch (point.type) {
                        case "robot":
                            if( point.pointCharacteristic == "INTERSECTION" || point.pointCharacteristic == "LIGHT" ){
                                return;
                            }
                            ctx.drawImage(robotPointB, (point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                            break;
                        case "car":
                            ctx.drawImage(carPointB,(point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                            break;
                        case "drone":
                            ctx.drawImage(dronePointB,(point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                            break;
                    }
                    console.log("Point B id = " + i);
                    ctx.fillStyle="#0000FF";
                    pointBId = i;
                    pointBset = true;
                    document.getElementById("inputB").value = point.pointName;
                    document.getElementById('saveDelivery').style.visibility = 'visible';

                }
                return;
           }
        }
    }
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


