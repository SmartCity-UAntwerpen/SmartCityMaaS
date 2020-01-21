/**
 */

let delta = 10;

let _smartcityNamespace = 'SC';

var mapCanvas;
var ctx;
var world = [];
var worldLoaded = false;

var map_ready = false;

var xSize = 0;
var ySize = 0;
var pointAset = false;
var pointBset = false;

var pointA;
var pointB;

var pointAId = -10;
var pointBId = -10;

var mapAId = -10;
var mapBId = -10;

var linksDrawn = [];

var currentVehicleID = -1;
var currentVehicleType = 'none';
var currentDeliveryId = -1;

var traveledCarPath = [];

var jobs = [];
var jobLists = [];

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
var lightGreenIcon;
var lightRedIcon;

var vehiclesInterval;
var trafficInterval;


var trackingInterval = 1000;
var iconSize = 8;


window.addEventListener('load', (event) => {
    console.log('## Visualization Map ##');
    // Attach onclick handlers to library objects
    /*for(i = 0;i<libContents.length;++i){
        var item = libContents[i];
        item.click(_menuOnClickHandler);
    }*/

    mapCanvas = document.getElementById("mapcontainer");

    if (!visualization) {
        // Attach onclick handlers to the map-canvas
        mapCanvas.addEventListener('click', _mapOnClickHandler);
        //mapCanvas.addEventListener('mousemove', _mapOnMouseMoveHandler);
        //mapCanvas.addEventListener('mouseup', _mapOnMouseUpHandler);
    }

});

/**
 * The initialize function of the html page.
 */
function initFunction() {
    document.getElementById("loader").style.display = "block";
    showPage();
    //loadImages();
    getWorld();
    getDeliveries();


    if(visualization){
        //trafficInterval = setInterval(getTrafficStatus, 1050);
    }

    if(!only_view)
    {
        // Hide save button, this button will only be showed when both of the points are selected.
        document.getElementById('saveDelivery').style.visibility = 'hidden';

    }
}



/**
 * Retrieve the physical world from the MaaS.
 */
function getWorld(){
    $.getJSON("/retrieveWorld", function(result){
        world = result;
        worldLoaded = true;
        console.log(result);

        var ratio = world.dimensionX/world.dimensionY;
        /*$("#mapCanvas").width($("#myMapCanvas").width()).height( $("#myMapCanvas").width()/ratio ).attr("width", $("#mapCanvas").width()).attr("height", $("#mapCanvas").height());


        console.log("mapX = " + document.getElementById("mapCanvas").offsetWidth + " mapY = " + document.getElementById("mapCanvas").offsetHeight);
        xSize = (document.getElementById("mapCanvas").offsetWidth/world.dimensionX);
        ySize = (document.getElementById("mapCanvas").offsetHeight/world.dimensionY);*/
        console.log(" x size = " + xSize + " y size = " + ySize);
        drawWorld();
        showPage();
    }).fail(function() {
        showError("Could not load world.");
    });
}

function getDeliveries() {
    $.getJSON("/orders", function(result) {
        orders = result;
        print("test")
    })
}


function drawWorld(){

    //ctx = mapCanvas.getContext("2d");

    for(var i=0; i<world.points.length; i++){
        var point = world.points[i];

        var drawing;
        if( point.pointCharacteristic === "INTERSECTION"){
            drawing = visualisationCore.drawIntersection(point.physicalPoisionX*delta, point.physicalPoisionY*delta);
        } else {
            switch (point.type) {
                case "robot":
                    // check for id
                    if (point.pointCharacteristic === "LIGHT") {
                        if (!point.status) { // if it has no status, just make in green (bv in delivery)
                            point.status = lightGreenIcon;
                        }
                        //drawing = visualisationCore.drawRobotCharge(point.physicalPoisionX*delta, point.physicalPoisionY*delta);
                        drawing = visualisationCore.drawRobotTile(point.pointName, point.physicalPoisionX*delta, point.physicalPoisionY*delta);
                    } else {
                        //drawing = visualisationCore.drawRobotCharge(point.physicalPoisionX*delta, point.physicalPoisionY*delta);
                        drawing = visualisationCore.drawRobotTile(point.pointName, point.physicalPoisionX*delta, point.physicalPoisionY*delta);
                    }
                    break;
                case "car":
                    drawing = visualisationCore.drawCarGas(point.physicalPoisionX*delta, point.physicalPoisionY*delta);
                    break;
                case "drone":
                    drawing = visualisationCore.drawDroneHelipad(point.physicalPoisionX*delta, point.physicalPoisionY*delta);
                    break;
            }
        }
        if (typeof drawing !== 'undefined') {
            drawing.attr('id', "svg_"+point.mapId+"_"+point.pointName);
            drawing.attr('mapID', point.mapId, _smartcityNamespace);
            drawing.attr('pointID', point.pointName, _smartcityNamespace);
            drawing.attr('vehicleType', point.type, _smartcityNamespace);
        }
    }

    for(var index=0; index<world.points.length; index++) {
        point = world.points[index];

        for (var j = 0; j < point.neighbours.length; j++) {
            var neigbourID = point.neighbours[j];
            var neigbour = $.grep(world.points, function (e) {
                return e.mapId === point.mapId && e.pointName === neigbourID;
            })[0];
            var linkAlreadyDrawn = false;
            for (var k = 0; k < linksDrawn.length; k++) {
                if (linksDrawn[k].map === point.mapId) {
                    if (linksDrawn[k].end === point.pointName && linksDrawn[k].start === neigbourID) {
                        linkAlreadyDrawn = true;
                        break;
                    } else if (linksDrawn[k].start === point.pointName && linksDrawn[k].end === neigbourID) {
                        linkAlreadyDrawn = true;
                        break;
                    }
                }
            }
            if (!linkAlreadyDrawn) {
                linksDrawn.push({map: point.mapId, start: point.pointName, end: neigbourID});
                let start = SVG.find("#svg_"+point.mapId+"_"+point.pointName)[0];
                let end = SVG.find("#svg_"+point.mapId+"_" + neigbourID)[0];
                drawLink(point.type, start, end, false);
            }
        }
    }

    document.getElementById("loader").style.display = "none";
    map_ready = true;

}

/**
 * Shows a loading animation when data is loaded.
 * When all information is tranferred to the html page, the functional elements are shown while to loading animation
 * is hidden.
 */
function showPage() {
    if(map_ready === false) {
        if(only_view === false)
        {
            // Hide the devices,labels,... and show them when the that og the map is loaded.
            document.getElementById("content").style.visibility = "hidden";
            document.getElementById("passengersLabel").style.visibility = "hidden";
            document.getElementById("passengersSelect").style.visibility = "hidden";
        }else
        {
            if(visualization === false) {
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
                document.getElementById("table_deliveries").style.visibility = "hidden";
            }
        }
    }else
    {
        if(only_view === false)
        {
            document.getElementById("content").style.visibility = "visible";
        }else
        if(visualization === false) {
            document.getElementById("content").style.visibility = "visible";
            document.getElementById("pointALabel").style.visibility = "visible";
            document.getElementById("pointAtext").style.visibility = "visible";
            document.getElementById("pointBLabel").style.visibility = "visible";
            document.getElementById("pointBtext").style.visibility = "visible";
            document.getElementById("deliveryIDLabel").style.visibility = "visible";
            document.getElementById("deliveryID").style.visibility = "visible";
        }else
        {
            document.getElementById("table_deliveries").style.visibility = "visible";
        }
    }
}

function getJobVehicles(){
    if (jobs.length === 0){
        clearInterval(vehiclesInterval);
    }
    //reDrawWorld();
    var allDone = false;
    for (var i = 0; i < jobs.length; i++) {
        (function(i){
            $.getJSON("/world1/progress/" + jobs[i].id + "/" + jobs[i].idMap + "/" + jobs[i].idStart + "/" + jobs[i].idEnd + "/" + jobs[i].startPoint.type, function (progress) {
                //progress = [x, y, progress, status]
                switch (progress.status) {
                    case "TODO":
                        //ctx.strokeStyle = '#d90d1d';
                        drawLink(jobs[i].startPoint.type, jobs[i].startPoint, jobs[i].endPoint, true);
                        drawVehicle(jobs[i].startPoint.type, jobs[i].startPoint.physicalPoisionX, jobs[i].startPoint.physicalPoisionY, false, jobs[i].startPoint);
                        break;
                    case "DONE":
                        if(i === jobs.length-1){
                            allDone = true;
                        }
                        //ctx.strokeStyle = '#00d900';
                        drawLink(jobs[i].startPoint.type, jobs[i].startPoint, jobs[i].endPoint, true);
                        drawVehicle(jobs[i].endPoint.type, jobs[i].endPoint.physicalPoisionX, jobs[i].endPoint.physicalPoisionY, false, jobs[i].startPoint);
                        break;
                    case "BUSY":
                        /*ctx.strokeStyle = '#d90d1d';
                        drawLink(jobs[i].startPoint.type, jobs[i].startPoint, jobs[i].endPoint, true);*/
                        var endPoint = {
                            "physicalPoisionX": progress.x,
                            "physicalPoisionY": progress.y,
                            "pointName": jobs[i].endPoint.pointName
                        };
                        console.log("New position:");
                        console.log(endPoint);
                        //ctx.strokeStyle = '#00d900';
                        if (jobs[i].startPoint.type === "car") { // car difficult to track
                            //trackCarLink(jobs[i].startPoint, endPoint, jobs[i].endPoint, progress.progress/100);
                        } else {
                            drawLink(jobs[i].startPoint.type, jobs[i].startPoint, endPoint, true);
                        }
                        drawVehicle(jobs[i].startPoint.type, progress.x, progress.y, true, jobs[i].startPoint);
                        break;
                    case "FAILED":
                        showError("DRONE FAILED");
                        break;
                }

                $("#"+currentDeliveryId + "-jobs ."+jobs[i].id+" .prog").text(progress.progress);
                $("#"+currentDeliveryId + "-jobs ."+jobs[i].id+" .stat").text(progress.status);
            }).fail(function() {
                showError("Delivery job " + jobs[i].id + " not found!");
            });
        })(i);

        if(0 < i && i < jobs.length) {
            /*ctx.beginPath();
            ctx.setLineDash([20, 5]);
            ctx.strokeStyle = '#b99239';
            ctx.lineWidth = 3;
            ctx.moveTo(jobs[i-1].endPoint.physicalPoisionX*xSize,jobs[i-1].endPoint.physicalPoisionY*ySize);
            ctx.lineTo(jobs[i].startPoint.physicalPoisionX*xSize,jobs[i].startPoint.physicalPoisionY*ySize);
            ctx.stroke();*/
        }
    }
    if(allDone){
        clearInterval(vehiclesInterval);
    }
}

// is used inline: visualization_map.html
function trackDelivery(deliveryId){
    $('.track').text("TRACK");
    if(currentDeliveryId !== deliveryId) {
        currentDeliveryId = deliveryId;
        $("#"+deliveryId + " .track").text('UNTRACK');
        var startPoint;
        var endPoint;

        $("#"+currentDeliveryId + "-jobs .deliveryJobs").empty();
        $.getJSON("/world1/delivery/" + deliveryId).done(function (delivery) {
            console.log(delivery);
            jobLists = delivery.jobLists;
            for (var j = 0; j < jobLists.length; j++) {
                jobs = jobLists[j].jobs;
                for (var i = 0; i < jobs.length; i++) {

                    startPoint = $.grep(world.points, function (e) {
                        return e.pointName === jobs[i].idStart && e.mapId === jobs[i].idMap;
                    })[0];
                    jobs[i].startPoint = startPoint;

                    endPoint = $.grep(world.points, function (e) {
                        return e.pointName === jobs[i].idEnd && e.mapId === jobs[i].idMap;
                    })[0];
                    jobs[i].endPoint = endPoint;

                    //SHOW IN FRONTEND:
                    $("#" + currentDeliveryId + "-jobs .deliveryJobs").append(
                        "<p class=" + jobs[i].id + ">Start: " + jobs[i].idStart +
                        " - End: " + jobs[i].idEnd +
                        " - Map: " + jobs[i].idMap +
                        " - Progress: <span class='prog'>0</span>% (<span class='stat'>" + jobs[i].status + "</span>)</p>"
                    );
                }
            }
        }).fail(function() {
            showError("Delivery "+ deliveryId +" not found!");
        });
        vehiclesInterval = setInterval(getJobVehicles, trackingInterval);
    } else {
        currentDeliveryId = -1;
        reDrawWorld();
        clearInterval(vehiclesInterval);
        jobs = [];
        var trackedVehicles = SVG.find(".vehicleTrack_svg");
        trackedVehicles.forEach(element => element.remove());

    }
}

function reDrawWorld(){
    //ctx.clearRect(0, 0, mapCanvas.width, mapCanvas.height);
    linksDrawn = [];
    //drawWorld();
}

function drawLink(type, startpoint, endpoint, track){
    //if (startpoint.node.attributes["vehicleType"] !== endpoint.node.attributes["vehicleType"]) { return; }
    /*ctx.beginPath();
    if(track){
        ctx.setLineDash([20, 5]);
        ctx.lineWidth = 3;
    }


    ctx.moveTo(startpoint.physicalPoisionX*xSize,startpoint.physicalPoisionY*ySize);
*/
    if (typeof startpoint.node === 'undefined') {
        startpoint = SVG.find("#svg_"+startpoint.mapId+"_"+startpoint.pointName)[0];
        endpoint = SVG.find("#svg_"+endpoint.mapId+"_" + endpoint.pointName)[0];
    }

    if (typeof startpoint === 'undefined' || typeof endpoint === 'undefined') { return; }


    switch (type) {
        case "robot":
            if (!track) {
                //ctx.setLineDash([]);
                //ctx.strokeStyle = '#e67e22';
                //ctx.lineWidth = 5;
            }
            //90 graden
            if (startpoint.pointName < endpoint.pointName) {
                //ctx.lineTo(endpoint.physicalPoisionX * xSize, startpoint.physicalPoisionY * ySize);
                visualisationCore.drawRobotLink(startpoint, endpoint);
            } else {
                //ctx.lineTo(startpoint.physicalPoisionX * xSize, endpoint.physicalPoisionY * ySize);
            }
            break;
        case "car":
            visualisationCore.drawCarLink(startpoint, endpoint);
            /*if (!track) {
                ctx.setLineDash([]);
                ctx.strokeStyle = '#95a5a6';
                ctx.lineWidth = 7;
            }
            var distX = (endpoint.physicalPoisionX - startpoint.physicalPoisionX);
            var distY = (endpoint.physicalPoisionY - startpoint.physicalPoisionY);
            var distXpiece = distX / 8;
            var distYpiece = distY / 8;
            var pointX = 0;
            var pointY = 0;

            // LINKS ARE DRAWN IN ORDER, LOWEST ID's FIRST
            if (startpoint.pointName > endpoint.pointName) {
                pointX = startpoint.physicalPoisionX + distXpiece;
                pointY = startpoint.physicalPoisionY + 5 * distYpiece;
                ctx.lineTo(pointX * xSize, pointY * ySize);
                pointX = startpoint.physicalPoisionX + 3 * distXpiece;
                pointY = startpoint.physicalPoisionY + 7 * distYpiece;
            } else {
                pointX = startpoint.physicalPoisionX + 5 * distXpiece;
                pointY = startpoint.physicalPoisionY + distYpiece;
                ctx.lineTo(pointX * xSize, pointY * ySize);
                pointX = startpoint.physicalPoisionX + 7 * distXpiece;
                pointY = startpoint.physicalPoisionY + 3 * distYpiece;
            }
            ctx.lineTo(pointX * xSize, pointY * ySize);*/
            break;
        case "drone":
            visualisationCore.drawDroneLink(startpoint, endpoint);
            /*if (!track) {
                ctx.setLineDash([5, 15]);
                ctx.strokeStyle = '#2980b9';
                ctx.lineWidth = 2;
            }*/
            break;
    }

    //ctx.lineTo(endpoint.physicalPoisionX*xSize,endpoint.physicalPoisionY*ySize);
    //ctx.stroke();
}

function trackCarLink(startpoint, currentpoint, endpoint, progress){
    // Similar code in World.java
    ctx.beginPath();
    ctx.setLineDash([20, 5]);
    ctx.lineWidth = 3;

    ctx.moveTo(startpoint.physicalPoisionX*xSize,startpoint.physicalPoisionY*ySize);

    var distX = (endpoint.physicalPoisionX - startpoint.physicalPoisionX);
    var distY = (endpoint.physicalPoisionY - startpoint.physicalPoisionY);
    var distXpiece = distX/8;
    var distYpiece = distY/8;
    var pointX;
    var pointY;
    // LENGTH OF SEPARATE LINE PIECES (Pythagoras):
    var line1 =  Math.sqrt(Math.pow(Math.abs(5*distXpiece), 2) +  Math.pow(Math.abs(distYpiece),2));
    var line2 =  Math.sqrt(Math.pow(Math.abs(2*distXpiece), 2) +  Math.pow(Math.abs(2*distYpiece),2));
    var line3 =  Math.sqrt(Math.pow(Math.abs(distXpiece), 2) +  Math.pow(Math.abs(5*distYpiece),2));
    // HOW MUCH PERCENT OF TOTAL LINE IS THIS LINE:
    var totalLine = line1 + line2 + line3;
    var line1prog = line1/totalLine;
    var line2prog = line2/totalLine;

    if (progress < line1prog) {
        ctx.lineTo(currentpoint.physicalPoisionX * xSize, currentpoint.physicalPoisionY * ySize);
    } else if(progress < line1prog+line2prog) {
        if (startpoint.pointName > endpoint.pointName) {
            pointX = startpoint.physicalPoisionX + distXpiece;
            pointY = startpoint.physicalPoisionY + 5 * distYpiece;
        } else {
            pointX = startpoint.physicalPoisionX + 5 * distXpiece;
            pointY = startpoint.physicalPoisionY + distYpiece;
        }
        ctx.lineTo(pointX * xSize, pointY * ySize);
        ctx.lineTo(currentpoint.physicalPoisionX * xSize, currentpoint.physicalPoisionY * ySize);
    } else {
        if (startpoint.pointName > endpoint.pointName) {
            pointX = startpoint.physicalPoisionX + distXpiece;
            pointY = startpoint.physicalPoisionY + 5 * distYpiece;
            ctx.lineTo(pointX * xSize, pointY * ySize);
            pointX = startpoint.physicalPoisionX + 3 * distXpiece;
            pointY = startpoint.physicalPoisionY + 7 * distYpiece;
        } else {
            pointX = startpoint.physicalPoisionX + 5 * distXpiece;
            pointY = startpoint.physicalPoisionY + distYpiece;
            ctx.lineTo(pointX * xSize, pointY * ySize);
            pointX = startpoint.physicalPoisionX + 7 * distXpiece;
            pointY = startpoint.physicalPoisionY + 3 * distYpiece;
        }
        ctx.lineTo(pointX * xSize, pointY * ySize);
        ctx.lineTo(currentpoint.physicalPoisionX * xSize, currentpoint.physicalPoisionY * ySize);
    }
    ctx.stroke();
}

function drawIcon(point) {
    var drawing;
    if( point.pointCharacteristic === "INTERSECTION"){
        drawing = visualisationCore.drawIntersection(point.physicalPoisionX, point.physicalPoisionY);
    } else {
        switch (point.type) {
            case "robot":
                if (point.pointCharacteristic === "LIGHT") {
                    if (!point.status) { // if it has no status, just make in green (bv in delivery)
                        point.status = lightGreenIcon;
                    }
                    drawing = visualisationCore.drawRobotCharge(point.physicalPoisionX, point.physicalPoisionY);
                } else {
                    drawing = visualisationCore.drawRobotCharge(point.physicalPoisionX, point.physicalPoisionY);
                }
                break;
            case "car":
                drawing = visualisationCore.drawCarGas(point.physicalPoisionX, point.physicalPoisionY);
                break;
            case "drone":
                drawing = visualisationCore.drawDroneHelipad(point.physicalPoisionX, point.physicalPoisionY);
                break;
        }
    }
    if (typeof drawing !== 'undefined') {
        drawing.attr('id', "svg_"+point.mapId+"_"+point.pointName);
        drawing.attr('mapID', point.mapId, _smartcityNamespace);
        drawing.attr('pointID', point.pointName, _smartcityNamespace);
        drawing.attr('vehicleType', point.type, _smartcityNamespace);
    }
}


function drawVehicle(type, x, y,selected, point){
    //if (x === 0 && y === 0) { return; }
    var drawing;
    drawing = SVG.find("#svg_"+point.mapId+"_"+point.pointName+"_"+"vehicle")[0];
    if (typeof drawing !== 'undefined') {
        //drawing.transform({translate: {x: x, y: y}});
        //drawing.animate().move(x,y);
        drawing.animate().transform({translateX:x*delta,translateY:y*delta});
        //drawing.remove()
    }
    else {
        if(type === "robot") {
            if (selected) {
                //ctx.drawImage(robotIconTarget, (x * xSize) - iconSize * 3 / 2, (y * ySize) - iconSize * 3 / 2, iconSize * 3, iconSize * 3);
            } else {
                //ctx.drawImage(robotIcon, (x * xSize) - iconSize * 3 / 2, (y * ySize) - iconSize * 3 / 2, iconSize * 3, iconSize * 3);
            }
            drawing = visualisationCore.drawRobotIcon(x*delta,y*delta);
        }else if(type === "car") {
            if (selected) {
                //ctx.drawImage(racecarIconTarget, ((x * xSize) - (iconSize * 3 / 2)), ((y * ySize) - (iconSize * 3 / 2)), iconSize * 3, iconSize * 3);
            } else {
                //ctx.drawImage(racecarIcon, ((x * xSize) - (iconSize * 3 / 2)), ((y * ySize) - (iconSize * 3 / 2)), iconSize * 3, iconSize * 3);
            }
            drawing = visualisationCore.drawRaceCar(x*delta,y*delta);
        } else if(type === "drone") {
            if (selected) {
                //ctx.drawImage(droneIconTarget, ((x * xSize) + iconSize * -1), (y * ySize) - iconSize * 1, iconSize * 3, iconSize * 3);
            } else {
                //ctx.drawImage(droneIcon, ((x * xSize) + iconSize * -1), (y * ySize) - iconSize * 1, iconSize * 3, iconSize * 3);
            }
            drawing = visualisationCore.drawDrone(x*delta,y*delta);
        }else{
            console.log(" vehicle type not supported");
        }
    }
    if (typeof drawing !== 'undefined') {
        drawing.attr('id', "svg_"+point.mapId+"_"+point.pointName+"_"+"vehicle");
        drawing.addClass("vehicleTrack_svg");
        drawing.attr('mapID', point.mapId, _smartcityNamespace);
        drawing.attr('pointID', point.pointName, _smartcityNamespace);
        drawing.attr('vehicleType', point.type, _smartcityNamespace);
    }

}

function getTrafficStatus() {
    var lights =  $.grep(world.points, function(e){return e.pointCharacteristic === "LIGHT"});
    $.getJSON("/getTrafficLightStats", function(result){
        var status = lightGreenIcon;
        for (var i = 0; i < result.length; i++) {
            var point =  $.grep(lights, function(e){return e.pointName === result[i].point.id})[0];
            if(point) {
                if (result[i].state !== "GREEN") {
                    status = lightRedIcon;
                }
                point.status = status;
                ctx.drawImage(status, (point.physicalPoisionX * xSize) - xSize, (point.physicalPoisionY * ySize) - ySize * 3 / 2, xSize * 3, ySize * 3);
            }
        }
    }).fail(function() {
        showError("Could not update traffic-lights.");
    });
}

/**
 * Load all the images from the html.
 */

function loadImages() {
    droneDefault = new Image();
    droneDefault.src = "/images/map/drone_h.png";
    dronePointA = new Image();
    dronePointA.src = "/images/map/drone_h_pointA.png";
    dronePointB = new Image();
    dronePointB.src = "/images/map/drone_h_pointB.png";
    carDefault = new Image();
    carDefault.src = "/images/map/car_gas.png";
    carPointA = new Image();
    carPointA.src = "/images/map/car_gas_pointA.png";
    carPointB = new Image();
    carPointB.src = "/images/map/car_gas_pointB.png";
    robotDefault = new Image();
    robotDefault.src = "/images/map/robot_charge.png";
    robotPointA = new Image();
    robotPointA.src = "/images/map/robot_charge_pointA.png";
    robotPointB = new Image();
    robotPointB.src = "/images/map/robot_charge_pointB.png";

    robotIcon = new Image();
    robotIcon.src = "/images/map/robotIcon.png";
    robotIconTarget = new Image();
    robotIconTarget.src = "/images/map/robotTargetted.png";
    droneIcon = new Image();
    droneIcon.src = "/images/map/droneIcon.png";
    droneIconTarget = new Image();
    droneIconTarget.src = "/images/map/droneTargetted.png";
    racecarIcon = new Image();
    racecarIcon.src = "/images/map/racecarIcon.png";
    racecarIconTarget = new Image();
    racecarIconTarget.src = "/images/map/raceCarTargetted.png";

    lightGreenIcon = new Image();
    lightGreenIcon.src = "/images/map/traffic_green.png";
    lightRedIcon = new Image();
    lightRedIcon.src = "/images/map/traffic_red.png";

}

function  showError(error){
    $("#error-bar").remove();
    $(".dashboard-con .row").append(
        "<div class='col-md-12 alert alert-danger' id='error-bar'>" +
        "<span>"+error+"</span>" +
        "</div>"
    );
}

/**
 * Handles and dispatches click events. A onclick attribute is added to the SVG map as a whole
 * @param {} event
 */
function _mapOnClickHandler(event){
    console.log("map click happened");
    var target = event.target;
    var point = target.parentNode;
    //var point = event.rangeParent;
    var type = point.getAttributeNS(_smartcityNamespace, "vehicleType");

    if (type !== "car" && type !== "drone" && type !== "robot") { return; }
    var pointID = point.getAttributeNS(_smartcityNamespace, "pointID");
    var mapID = point.getAttributeNS(_smartcityNamespace, "mapID");

    if (!pointA) {
        pointA = point;
        pointAset = true;
        document.getElementById("inputA").value = pointID;
        document.getElementById("mapA").value = mapID;
    } else if (!pointB) {
        pointB = point;
        pointBset = true;
        document.getElementById("inputB").value = pointID;
        document.getElementById("mapB").value = mapID;
        document.getElementById('saveDelivery').style.visibility = 'visible';
    } else {
        pointA = null;
        pointAset = false;
        pointB = null;
        pointBset = false;
        document.getElementById("inputA").value = null;
        document.getElementById("mapA").value = null;
        document.getElementById("inputB").value = null;
        document.getElementById("mapB").value = null;
        document.getElementById('saveDelivery').style.visibility = 'hidden';
    }
    console.log("pointA: ");
    console.log(pointA);
    console.log("pointB: ");
    console.log(pointB);
}

function zoomIn() {
    visualisationCore.zoomIn();
}

function zoomOut() {
    visualisationCore.zoomOut();
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


