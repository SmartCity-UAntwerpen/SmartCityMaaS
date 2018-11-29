/**
 */


var allBots = [];
var mapCanvas;
var ctx;
var world = [];
var worldLoaded = false;

var map_ready = false;

var xSize = 0;
var ySize = 0;
var pointAset = false;
var pointBset = false;

var pointAId = -10;
var pointBId = -10;

var linksDrawn = [];

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

/**
 * Load all the images from the html.
 */

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
        console.log("world x = " + world.dimensionX + " world y = " + world.dimensionY );


        var ratio = world.dimensionX/world.dimensionY;
        $("#mapCanvas").width($("#myMapCanvas").width()).height( $("#myMapCanvas").width()/ratio ).attr("width", $("#mapCanvas").width()).attr("height", $("#mapCanvas").height());


        console.log("mapX = " + document.getElementById("mapCanvas").offsetWidth + " mapY = " + document.getElementById("mapCanvas").offsetHeight);
        xSize = (document.getElementById("mapCanvas").offsetWidth/world.dimensionX);
        ySize = (document.getElementById("mapCanvas").offsetHeight/world.dimensionY);
        console.log(" x size = " + xSize + " y size = " + ySize);
        drawWorld();
        showPage();
    });
}


function drawWorld(){
    mapCanvas = document.getElementById("mapCanvas");


    if(!visualization && !only_view){
        mapCanvas.addEventListener("mousedown", clickedOnCanvas, false);
    }

    ctx = mapCanvas.getContext("2d");

    for(var i=world.points.length-1; i>=0; i--){
        var point = world.points[i];

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

                ctx.moveTo(point.physicalPoisionX*xSize,point.physicalPoisionY*ySize);

                switch (point.type) {
                    case "robot":
                        ctx.setLineDash([]);
                        ctx.strokeStyle = '#e67e22';
                        ctx.lineWidth=5;
                        //90 graden
                        ctx.lineTo(point.physicalPoisionX * xSize, neigbour.physicalPoisionY * ySize);
                        break;
                    case "car":
                        ctx.setLineDash([]);
                        ctx.strokeStyle = '#95a5a6';
                        ctx.lineWidth=7;

                        var distX = (neigbour.physicalPoisionX - point.physicalPoisionX);
                        var distY = (neigbour.physicalPoisionY - point.physicalPoisionY);
                        var distXpiece = distX/8;
                        var distYpiece = distY/8;
                        var pointX = 0;
                        var pointY = 0;

                        /*if(distX > 0) { // neighbour to the right (left city is higher than right city)
                            pointX = point.physicalPoisionX + 5 * distXpiece; // go on x-axis 5/8
                            pointY = point.physicalPoisionY + distYpiece; // go on y-axis 1/8
                            ctx.lineTo(pointX * xSize, pointY * ySize);
                            pointX = point.physicalPoisionX + 7 * distXpiece; // go further on x-as until 7/8
                            pointY = point.physicalPoisionY + 3 * distYpiece; // go further on y-axis until 3/8
                            ctx.lineTo(pointX * xSize, pointY * ySize);*/
                        //} else {
                            // CAR POINTS ID GIVEN FROM BOTTOM COUNTER CLOCK WISE (better this way)
                            pointX = point.physicalPoisionX + distXpiece;
                            pointY = point.physicalPoisionY + 5 * distYpiece;
                            ctx.lineTo(pointX * xSize, pointY * ySize);
                            pointX = point.physicalPoisionX + 3 * distXpiece;
                            pointY = point.physicalPoisionY + 7 * distYpiece;
                            ctx.lineTo(pointX * xSize, pointY * ySize);
                        //}
                        break;
                    case "drone":
                        ctx.setLineDash([5, 15]);
                        ctx.strokeStyle = '#2980b9';
                        ctx.lineWidth= 2;
                }

                ctx.lineTo(neigbour.physicalPoisionX*xSize,neigbour.physicalPoisionY*ySize);
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
    document.getElementById("loader").style.display = "none";
    map_ready = true;

}

/**
 * This function is performed on every mouse click
 * It controls the if the position of the mouse click is set on a spot/surrouding point and if a point A or B is
 * activated or deactivated
 * @param e
 */


function clickedOnCanvas(event){
    var rect = mapCanvas.getBoundingClientRect();
    var canvasX = event.clientX - rect.left;
    var canvasY = event.clientY - rect.top;
    console.log("Point clicked x = " + canvasX + ", y = " + canvasY);
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
                            ctx.drawImage(dronePointB,((point.physicalPoisionX*xSize) - (xSize*3/2)),(point.physicalPoisionY*ySize) - ySize*3/2,xSize*3,ySize*3);
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
    document.getElementById("loader").style.display = "block";
    showPage();
    loadImages();
    getWorld();

    if(visualization){
        $.getJSON("/world1/allVehicles").done(function(allVehicles) {
            allBots = allVehicles;
        });
        setInterval(getVehiclesVN, 1000);
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
            document.getElementById("content").style.visibility = "visible";
            document.getElementById("passengersLabel").style.visibility = "visible";
            document.getElementById("passengersSelect").style.visibility = "visible";
        }else
            if(visualization == false) {
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
                document.getElementById("table_vehicles").style.visibility = "visible";
            }
    }
}

function getVehiclesVN(){
    var prevProg = [];
    var progression = [];
    if(currentVehicleID != -1){
        $.when(
            $.getJSON("/world1/progress/null/" + currentVehicleID).done(function (progress) {
                prevProg = progression;
                progression = progress;
            })
        ).then(function () {
            if(progression[0] != -1) { // if progression arrived
                ctx.clearRect(0, 0, mapCanvas.width, mapCanvas.height);
                drawWorld();
                if (progression[3] == prevProg[3]) { // if vehicle ID is same
                    if(progression[0] != prevProg[0] && progression[1] != prevProg[1]){ // if position isn't same
                        drawVehicle(currentVehicleType, progression[0], progression[1], true);
                    }
                } else {
                    drawVehicle(currentVehicleType, progression[0], progression[1], true);
                }
            }
        })
    } else if(currentVehicleID == -1 && prevProg[3] != 0) {
        prevProg[3] = 0;
        ctx.clearRect(0, 0, mapCanvas.width, mapCanvas.height);
        drawWorld();
    }
}


function drawVehicle(type, x, y,selected){
    if(type == "robot") {
        if (selected) {
            ctx.drawImage(robotIconTarget, (x * xSize) - xSize * 3 / 2, (y * ySize) - ySize * 3 / 2, xSize * 3, ySize * 3);
        } else {
            ctx.drawImage(robotIcon, (x * xSize) - xSize * 3 / 2, (y * ySize) - ySize * 3 / 2, xSize * 3, ySize * 3);
        }
    }else if(type == "car") {
        if (selected) {
            ctx.drawImage(racecarIconTarget, ((x * xSize) - (xSize * 3 / 2)), ((y * ySize) - (ySize * 3 / 2)), xSize * 3, ySize * 3);
        } else {
            ctx.drawImage(racecarIcon, ((x * xSize) - (xSize * 3 / 2)), ((y * ySize) - (ySize * 3 / 2)), xSize * 3, ySize * 3);
        }
    } else if(type == "drone") {
        if (selected) {
            ctx.drawImage(droneIconTarget, ((x * xSize) + xSize * 3 / 2), (y * ySize) - ySize * 3 / 2, xSize * 3, ySize * 3);
        } else {
            ctx.drawImage(droneIcon, ((x * xSize) + xSize * 3 / 2), (y * ySize) - ySize * 3 / 2, xSize * 3, ySize * 3);
        }
    }else{
        console.log(" vehicle type not supported");
    }

}


