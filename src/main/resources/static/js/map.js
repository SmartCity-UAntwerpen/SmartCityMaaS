/**
 * Created by Frédéric Melaerts on 11/05/2017.
 */

var mapCanvas;
var mapCanvasContext;
var world = [];
var pointA_x,pointA_y;
var pointA_x_cell,pointA_y_cell;
var pointB_x_cell,pointB_y_cell;
var pointB_x, pointB_y;
var pointA_set = false;
var pointB_set = false;
var x_size;
var y_size;
var progress;

// 146.175.140.44

function start() {

    mapCanvas = document.getElementById("mapCanvas");
    mapCanvasContext = mapCanvas.getContext("2d");
    drawMap();
    mapCanvas.addEventListener("click", onClick, false);

    // firstDraw()
    //setInterval(loop, 15);
}
/*
function drawMap() {
    x_size = mapCanvas.width/world.dimensionY;
    y_size = mapCanvas.height/world.dimensionX;
    var cell;
    for(var i=0; i<world.dimensionY; i++){
        for(var j=0; j<world.dimensionX; j++){
            cell = world.cells[i].cellList[j];

        }
    }
}*/
function drawMap() {
    x_size = mapCanvas.width/world.dimensionY;
    y_size = mapCanvas.height/world.dimensionX;
    console.log("--- Information ---");
    console.log("ROWS "+world.dimensionY);
    console.log("COLUMNS "+world.dimensionX);
    console.log("x_size "+x_size);
    console.log("y_size "+y_size);
    var cell;
    var redraw;
    getProgress();
    for(var i=0; i<world.dimensionY; i++){
        for(var j=0; j<world.dimensionX; j++){
            cell = world.cells[i].cellList[j];
            redraw = drawCell( j, i,cell);
            if(redraw == true)
            {
                i = -1;
                j = world.dimensionX;
            }
        }
    }
    document.getElementById('inputA').value = ""+pointA_x+pointA_y;
    document.getElementById('inputB').value = ""+pointB_x+pointB_y;

}
/*
function loop()
{
    //console.log("loop is on going");
    clear();
    drawMap();
}
*/
function clear() {/*
    mapCanvasContext.fillStyle="#ffffff";
    mapCanvasContext.fillRect(0,0,mapCanvas.width,mapCanvas.height);*/
    /*canvas.fillStyle="#888888";
    canvas.strokeRect(0,0,width,height);*/
}

function drawCell(j, i, cell) {

    var columns = x_size * j;
    var rows = y_size * i;
    var change_color = "null";
    if(cell.type.localeCompare("background") != 0 && cell.type.localeCompare("road") != 0 ) {
        if (pointA_set == true) {
            if (columns <= pointA_x && pointA_x < columns + x_size && rows <= pointA_y && pointA_y < rows + y_size) {

                if (cell.type.localeCompare("surrounding_point") == 0) {
                    console.log("FUCK IT3é");
                    pointA_x = cell.sur_x*x_size;
                    pointA_y = cell.sur_y*y_size;
                    mapCanvasContext.drawImage(cellPointB, columns, rows, x_size, y_size);
                    return true;
                }else
                {
                    pointA_x_cell = columns;
                    pointA_y_cell = rows;
                    pointA_x = cell.sur_x*x_size;
                    pointA_y = cell.sur_y*y_size;
                    change_color = "A";
                }
            } else {
                if (pointB_set == true) {
                    if (columns <= pointB_x && pointB_x < columns + x_size && rows <= pointB_y && pointB_y < rows + y_size) {
                        if (cell.type.localeCompare("surrounding_point") == 0) {
                            console.log("FUCK IT3é");
                            if( cell.sur_x*x_size == pointB_x && cell.sur_y*y_size == pointB_y)
                            {
                                pointB_set = false;
                                return true;
                            }else
                            {
                                pointB_x = cell.sur_x*x_size;
                                pointB_y = cell.sur_y*y_size;
                                mapCanvasContext.drawImage(cellPointB, columns, rows, x_size, y_size);
                                return true;
                            }
                        }else
                        {
                            pointB_x_cell = columns;
                            pointB_y_cell = rows;
                            pointB_x = cell.sur_x*x_size;
                            pointB_y = cell.sur_y*y_size;
                            change_color = "B";
                        }
                    }
                }
            }
        }
    }
    if(rows == pointA_y && columns == pointA_x)
    {
        if(change_color == "A") {
            console.log("-----------------------------------------------------------------");
            console.log("ROWS " + rows);
            console.log("COLUMNS " + columns);
            console.log("pointA_x " + pointA_x);
            console.log("pointA_y " + pointA_y);
            console.log("pointB_x " + pointB_x);
            console.log("pointB_y " + pointB_y);
            console.log("-----------------------------------------------------------------");
            mapCanvasContext.drawImage(cellPointA, columns, rows, x_size, y_size);
        }
    }else if(rows == pointB_y && columns == pointB_x)
    {
        if(change_color == "B") {
            console.log("-----------------------------------------------------------------");
            console.log("ROWS " + rows);
            console.log("COLUMNS " + columns);
            console.log("pointA_x " + pointA_x);
            console.log("pointA_y " + pointA_y);
            console.log("pointB_x " + pointB_x);
            console.log("pointB_y " + pointB_y);
            console.log("-----------------------------------------------------------------");
            mapCanvasContext.drawImage(cellPointB, columns, rows, x_size, y_size);
        }

    }else

    /*
     * Type defenition
     * type: 1 = background
     * type: 2 = spot_robot
     * type: 3 = surrounding_point
     * type: 4 = road_robot
     */
    {
        if(cell.type.localeCompare("spot") == 0)
        {
            console.log("Map info x " +cell.specific);

            if(cell.specific.localeCompare("drone") == 0)
            {
                mapCanvasContext.drawImage(spot_drone, columns, rows, x_size, y_size);
            }else
            {
                mapCanvasContext.drawImage(spot_robot, columns, rows, x_size, y_size);
            }
        }else if(cell.type.localeCompare("surrounding_point") == 0) {

            if (cell.specific.localeCompare("drone") == 0)
            {
                mapCanvasContext.drawImage(spot_drone, columns, rows, x_size, y_size);
            }else
            {
                mapCanvasContext.drawImage(surrounding, columns, rows, x_size, y_size);
            }
        }else if (cell.type.localeCompare("road") == 0)
        {
            mapCanvasContext.drawImage(cellRoad, columns, rows, x_size, y_size);
        }else
        {
            mapCanvasContext.drawImage(cellBackground, columns, rows, x_size, y_size);
        }
    }
    return false;
}


function redrawCell(j, i, cell) {

    var columns = x_size * j;
    var rows = y_size * i;
    var change_color = "null";
    if(cell.type.localeCompare("background") != 0 && cell.type.localeCompare("road") != 0 ) {
        if (pointA_set == true) {
            if (columns <= pointA_x && pointA_x < columns + x_size && rows <= pointA_y && pointA_y < rows + y_size) {

                if (cell.type.localeCompare("surrounding_point") == 0) {
                    console.log("FUCK IT3é");
                    pointA_x = cell.sur_x*x_size;
                    pointA_y = cell.sur_y*y_size;
                    mapCanvasContext.drawImage(cellPointB, columns, rows, x_size, y_size);
                    return true;
                }else
                {
                    pointA_x_cell = columns;
                    pointA_y_cell = rows;
                    pointA_x = cell.sur_x*x_size;
                    pointA_y = cell.sur_y*y_size;
                    change_color = "A";
                }
            } else {
                if (pointB_set == true) {
                    if (columns <= pointB_x && pointB_x < columns + x_size && rows <= pointB_y && pointB_y < rows + y_size) {
                        if (cell.type.localeCompare("surrounding_point") == 0) {
                            console.log("FUCK IT3é");
                            if( cell.sur_x*x_size == pointB_x && cell.sur_y*y_size == pointB_y)
                            {
                                pointB_set = false;
                                return true;
                            }else
                            {
                                pointB_x = cell.sur_x*x_size;
                                pointB_y = cell.sur_y*y_size;
                                mapCanvasContext.drawImage(cellPointB, columns, rows, x_size, y_size);
                                return true;
                            }
                        }else
                        {
                            pointB_x_cell = columns;
                            pointB_y_cell = rows;
                            pointB_x = cell.sur_x*x_size;
                            pointB_y = cell.sur_y*y_size;
                            change_color = "B";
                        }
                    }
                }
            }
        }
    }
    if(rows == pointA_y && columns == pointA_x)
    {
        if(change_color == "A") {
            console.log("-----------------------------------------------------------------");
            console.log("ROWS " + rows);
            console.log("COLUMNS " + columns);
            console.log("pointA_x " + pointA_x);
            console.log("pointA_y " + pointA_y);
            console.log("pointB_x " + pointB_x);
            console.log("pointB_y " + pointB_y);
            console.log("-----------------------------------------------------------------");
            mapCanvasContext.drawImage(cellPointA, columns, rows, x_size, y_size);
        }
    }else if(rows == pointB_y && columns == pointB_x)
    {
        if(change_color == "B") {
            console.log("-----------------------------------------------------------------");
            console.log("ROWS " + rows);
            console.log("COLUMNS " + columns);
            console.log("pointA_x " + pointA_x);
            console.log("pointA_y " + pointA_y);
            console.log("pointB_x " + pointB_x);
            console.log("pointB_y " + pointB_y);
            console.log("-----------------------------------------------------------------");
            mapCanvasContext.drawImage(cellPointB, columns, rows, x_size, y_size);
        }

    }else

    /*
     * Type defenition
     * type: 1 = background
     * type: 2 = spot_robot
     * type: 3 = surrounding_point
     * type: 4 = road_robot
     */
    {
        if(cell.type.localeCompare("spot") == 0)
        {
            console.log("Map info x " +cell.specific);

            if(cell.specific.localeCompare("drone") == 0)
            {
                mapCanvasContext.drawImage(spot_drone, columns, rows, x_size, y_size);
            }else
            {
                mapCanvasContext.drawImage(spot_robot, columns, rows, x_size, y_size);
            }
        }else if(cell.type.localeCompare("surrounding_point") == 0) {

            if (cell.specific.localeCompare("drone") == 0)
            {
                mapCanvasContext.drawImage(spot_drone, columns, rows, x_size, y_size);
            }else
            {
                mapCanvasContext.drawImage(surrounding, columns, rows, x_size, y_size);
            }
        }else if (cell.type.localeCompare("road") == 0)
        {
            mapCanvasContext.drawImage(cellRoad, columns, rows, x_size, y_size);
        }else
        {
            mapCanvasContext.drawImage(cellBackground, columns, rows, x_size, y_size);
        }
    }
    return false;
}

function loadImages() {
    cellBackground = new Image();
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

}

function getWorld(){
    $.getJSON("/retrieveWorld", function(result){
        world = result;
    });
}
function getProgress(){
    $.getJSON("/world1/progress/0", function(result){
        progress = result;
        console.log("Result: "+progress[0]+" - " + progress[1]);
        if(progress[0] != -1)
        {
            mapCanvasContext.drawImage(vehicle, progress[0]*x_size, progress[1]*y_size, x_size, y_size);

        }

    });


}
function legitArea(x, y)
{
    var type;
    for(var i=0; i<world.dimensionY; i++){
        for(var j=0; j<world.dimensionX; j++){
            var column = x_size * j;
            var row = y_size * i;
            if (column <= x && x < column + x_size && row <= y && y < row + y_size) {

                /*
                 * Type defenition
                 * type: 1 = background
                 * type: 2 = spot_robot
                 * type: 3 = surrounding_point
                 * type: 4 = road_robot
                 */
                if (world.cells[i].cellList[j].type.localeCompare("background") == 0 || world.cells[i].cellList[j].type.localeCompare("road") == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }
}
function onClick(e) {
    console.log("MOUSE CLICK x "+ e.pageX + " en y " +e.pageY);
    var map = document.getElementById('myMapCanvas');
    console.log("Map info x "+ map.offsetLeft + " en y " +map.offsetTop);
    console.log("DELIVERY FROM THYMLEAF " +pointA);
    document.getElementById('inputA').value = "JellyFish";
    var x_click = e.pageX-map.offsetLeft;
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
                pointA_set = false;
                if (pointB_set == true) {
                    pointB_x = x_click;
                    pointB_y = y_click;
                    pointB_set = false;
                }
            } else {
                if (pointB_set == true) {
                    var midpointB_X = pointB_x+(x_size/2);
                    var midpointB_Y = pointB_y+(y_size/2);
                    if (midpointB_X - sideVariation_X < x_click && x_click < midpointB_X + sideVariation_X && midpointB_Y - sideVariation_Y < y_click && y_click < midpointB_Y + sideVariation_Y) {
                        pointB_x = x_click;
                        pointB_y = y_click;
                        pointB_set = false;
                    }
                } else {
                    pointB_x = x_click;
                    pointB_y = y_click;
                    pointB_set = true;
                }
            }
        } else {
            pointA_x = x_click;
            pointA_y = y_click;
            pointA_set = true;
        }
        drawMap();
    }
}

    loadImages();
    getWorld();
    start();
    // Execute getProgress every 250 milliseconds
    setInterval(getProgress, 100)
    setTimeout(start, 500);

