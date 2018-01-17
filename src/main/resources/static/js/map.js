/**
 * Created by Frédéric Melaerts on 11/05/2017.
 */

var mapCanvas;
var mapCanvasContext;
var world = [];
var worldLoaded = false;

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
var robotIcon;
var robotIconTarget;
var droneIcon;
var droneIconTarget;
var racecarIcon;
var racecarIconTarget;

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

    robotIcon = new Image();
    robotIcon.src = "../images/map/robotIcon.png";
    robotIconTarget = new Image();
    robotIconTarget.src = "../images/map/robotTargetted.png";
    droneIcon = new Image();
    droneIcon.src = "../images/map/droneIcon.png";
    droneIconTarget = new Image();
    droneIconTarget.src = "../images/map/droneTargetted.png";
    racecarIcon = new Image();
    racecarIcon.src = "../images/map/racecarIcon.png";
    racecarIconTarget = new Image();
    racecarIconTarget.src = "../images/map/raceCarTargetted.png";

}

/**
 * Retrieve the physical world from the MaaS.
 */
function getWorld(){
    $.getJSON("/retrieveWorld", function(result){
        world = result;
        worldLoaded = true;
        console.log("JSON WORLD");
        console.log("world x = " + world.dimensionX + " world y = " + world.dimensionY );


        var ratio = world.dimensionX/world.dimensionY;
        $("#mapCanvas").width($("#myMapCanvas").width()).height( $("#myMapCanvas").width()/ratio ).attr("width", $("#mapCanvas").width()).attr("height", $("#mapCanvas").height());


        console.log("mapX = " + document.getElementById("mapCanvas").offsetWidth + " mapY = " + document.getElementById("mapCanvas").offsetHeight);
        xSize = (document.getElementById("mapCanvas").offsetWidth/world.dimensionX);
        ySize = (document.getElementById("mapCanvas").offsetHeight/world.dimensionY);
        console.log(" x size = " + xSize + " y size = " + ySize);
        drawWorld();
        showPage();
        console.log("values"+ linksDrawn);
    });
}


function drawWorld(){
    console.log("world point size = " + world.points.length);
    mapCanvas = document.getElementById("mapCanvas");


    if(!visualization && !only_view){
        mapCanvas.addEventListener("mousedown", clickedOnCanvas, false);
    }

    var ctx = mapCanvas.getContext("2d");

    for(var i=world.points.length-1; i>=0; i--){
        var point = world.points[i];
        console.log("point " + i + " x = " +  point.physicalPoisionX + " y = "+point.physicalPoisionY + " charachterestic = " + point.pointCharacteristic + " name = " + point.pointName);

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
                    // hiermee tekenen we bochten van 90 graden -> niet geimplementeerd in final build omdat het moeilijk is
                    // om de progress te visualiseren dan
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

}

/**
 * This function is performed on every mouse click
 * It controls the if the position of the mouse click is set on a spot/surrouding point and if a point A or B is
 * activated or deactivated
 * @param e
 */


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
            console.log("pointBset");
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
                    console.log("last else");
                    switch (point.type) {
                        case "robot":
                            if( point.pointCharacteristic == "INTERSECTION" || point.pointCharacteristic == "LIGHT" ){
                                return;
                            }
                            ctx.drawImage(robotPointB, (point.physicalPoisionX*xSize) - xSize*3/2,(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
                            break;
                        case "car":
                            console.log("x: " + (parseFloat((point.physicalPoisionX*xSize)) - parseFloat(xSize*3/2)));
                            var pointx = point.physicalPoisionX*xSize;
                            var offsetx = xSize*3/2;
                            console.log("testp "+ pointx);
                            console.log("testo "+ offsetx);
                            console.log("test "+ (pointx + offsetx*(-1)));
                            console.log("y: " + ((point.physicalPoisionY*ySize) - ySize*3/2));
                            console.log("xsize: " + xSize*3/2);
                            console.log("ysize: " + ySize*3);
                            ctx.drawImage(carPointB,((point.physicalPoisionX*xSize) - (xSize*3/2)),((point.physicalPoisionY*ySize) - (ySize*3/2)),(xSize*3),(ySize*3));
                            break;
                        case "drone":
                            ctx.drawImage(dronePointB,((point.physicalPoisionX*xSize) + xSize*3/2),(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
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
                document.getElementById("deliveryIDLabel").style.visibility = "hidden";
                document.getElementById("deliveryID").style.visibility = "hidden";
                document.getElementById("deliveryDone").style.visibility = "hidden";

            }else
            {
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
                document.getElementById("deliveryIDLabel").style.visibility = "visible";
                document.getElementById("deliveryID").style.visibility = "visible";
            }else
            {
                document.getElementById("loader").style.display = "none";
                document.getElementById("table_vehicles").style.visibility = "visible";
            }
            /*if(!control_initDraw)
            {
                console.log("Start drawing map");
                control_initDraw = true;
                //initdraw();
                console.log("Drawing complete");
            }*/
    }
}

function getVehiclesVN(){

    var vehicleID = -10;
    var getVehiclesURL = "/world1/allVehicles";
    var allVehiclesData = $.getJSON(getVehiclesURL);


    $.when(allVehiclesData).then(function(allVehicles) {
        for(var j=0; j<allVehicles.length; j++) {
            vehicleID = allVehicles[j];

            var progressVehicleURL = "/world1/progress/null/" + vehicleID;
            console.log("progress of vehicle " + vehicleID);

            var progressVehicle = $.getJSON(progressVehicleURL);
            $.when(progressVehicle).then(function (progress) {
                progress = result1;
                console.log("Result: " + progress[0] + " - " + progress[1]);

                var vehicleTypeURL = "/vehicletype/" + vehicleID;
                console.log("URL: = " + vehicleTypeURL);

                var vehicleType = $.getJSON(vehicleTypeURL);
                $.when(vehicleType).then(function (type) {
                    console.log("type of vehicle " + j + " = " + type);

                    path.push(progress[0]);
                    path.push(progress[1]);

                    if (progress[0] != -1) {
                        if (currentVehicleID == vehicleID) {
                            drawVehicle(type, progress[0], progress[1], true);
                        } else {
                            drawVehicle(type, progress[0], progress[1], false);
                        }
                    }

                });

            });
        }
    });

    /* DRAW PATH */
    if( path.length > 2){
        var mapCanvas = document.getElementById("mapCanvas");
        var ctx = mapCanvas.getContext("2d");
        ctx.moveTo(path[0]*xSize,path[1]*ySize);
        for(var i = 2; i < path.length; i = i + 2)
        {
            ctx.lineTo(path[i]*xSize,path[i+1]*ySize);
        }
        ctx.strokeStyle = '#e74c3c';ß
        ctx.lineWidth=5;
        ctx.stroke();


    }

}


function drawVehicle(type, x, y,selected){
    var mapCanvas = document.getElementById("mapCanvas");
    var ctx = mapCanvas.getContext("2d");
    switch (type) {
        case "robot":
            if(selected){
                ctx.drawImage(robotIconTarget, (x*xSize) - xSize*3/2,(y*ySize) - ySize*3/2,xSize*3,ySize*3);
            }else {
                ctx.drawImage(robotIcon, (x*xSize) - xSize*3/2,(y*ySize) - ySize*3/2,xSize*3,ySize*3);
            }
            break;
        case "car":
            if(selected){
                ctx.drawImage(racecarIconTarget,((x*xSize) - (xSize*3/2)),((y*ySize) - (ySize*3/2)),xSize*3,ySize*3);
            }else {
                ctx.drawImage(racecarIcon,((x*xSize) - (xSize*3/2)),((y*ySize) - (ySize*3/2)),xSize*3,ySize*3);
            }
            break;
        case "drone":
            if(selected){
                ctx.drawImage(droneIconTarget,((x*xSize) + xSize*3/2),(y*ySize) - ySize*3/2,xSize*3,ySize*3);
            }else {
                ctx.drawImage(droneIcon,((x*xSize) + xSize*3/2),(y*ySize) - ySize*3/2,xSize*3,ySize*3);
            }
            break;
    }

}


