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

function start() {

    mapCanvas = document.getElementById("mapCanvas");
    mapCanvasContext = mapCanvas.getContext("2d");
    drawMap();
    mapCanvas.addEventListener("click", onClick, false);
    //setInterval(loop, 15);
}
function drawMap() {
    x_size = mapCanvas.width/world.dimensionY;
    y_size = mapCanvas.height/world.dimensionX;
    console.log("--- Information ---");
    console.log("ROWS "+world.dimensionY);
    console.log("COLUMNS "+world.dimensionX);
    console.log("x_size "+x_size);
    console.log("y_size "+y_size);
    var cell;
    for(var i=0; i<world.dimensionY; i++){
        for(var j=0; j<world.dimensionX; j++){
            cell = world.cells[i].cellList[j];
            drawCell( j, i,cell);
        }
    }
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
    if(cell.type != 1) {
        if (pointA_set == true) {
            if (columns <= pointA_x && pointA_x < columns + x_size && rows <= pointA_y && pointA_y < rows + y_size) {
                pointA_x_cell = columns;
                pointA_y_cell = rows;
                mapCanvasContext.drawImage(cellPointA, pointA_x_cell, pointA_y_cell, x_size, y_size);
            } else {

                if (pointB_set == true) {
                    if (columns <= pointB_x && pointB_x < columns + x_size && rows <= pointB_y && pointB_y < rows + y_size) {
                        pointB_x_cell = columns;
                        pointB_y_cell = rows;
                        mapCanvasContext.drawImage(cellPointB, pointB_x_cell, pointB_y_cell, x_size, y_size);
                    } else {
                        mapCanvasContext.drawImage(cellRoad, columns, rows, x_size, y_size);

                    }
                } else {
                    mapCanvasContext.drawImage(cellRoad, columns, rows, x_size, y_size);
                }
            }
        } else {
            mapCanvasContext.drawImage(cellRoad, columns, rows, x_size, y_size);
        }
    }else
    {

        mapCanvasContext.drawImage(cellBackground, columns, rows, x_size, y_size);
    }
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
}

function getWorld(){
    $.getJSON("/retrieveWorld", function(result){
        world = result;
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
                if (world.cells[i].cellList[j].type == 1) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }
}
function onClick(e) {
    if(legitArea(e.pageX, e.pageY) == true) {
        if (pointA_set == true) {
            if (pointA_x_cell < e.pageX && e.pageX < pointA_x_cell + x_size && pointA_y_cell < e.pageY && e.pageY < pointA_y_cell + y_size) {
                pointA_x = e.pageX;
                pointA_y = e.pageY;
                pointA_set = false;
                if (pointB_set == true) {
                    pointB_x = e.pageX;
                    pointB_y = e.pageY;
                    pointB_set = false;
                }
            } else {
                if (pointB_set == true) {
                    if (pointB_x_cell < e.pageX && e.pageX < pointB_x_cell + x_size && pointB_y_cell < e.pageY && e.pageY < pointB_y_cell + y_size) {
                        pointB_x = e.pageX;
                        pointB_y = e.pageY;
                        pointB_set = false;
                    }
                } else {
                    pointB_x = e.pageX;
                    pointB_y = e.pageY;
                    pointB_set = true;
                }
            }

        } else {
            pointA_x = e.pageX;
            pointA_y = e.pageY;
            pointA_set = true;
        }
        drawMap();
    }
}

loadImages();
getWorld();
start();
setTimeout(start, 500);