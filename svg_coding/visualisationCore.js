var mapDrawer;
var _linksgroup;

window.addEventListener('load', (event) => {
    console.log('## SmartCity SVG Visualisation Core ##');
    mapDrawer = SVG().addTo('#map').size(visualisationCore.canvasWidth, visualisationCore.canvasHeight).id("mapcontainer");
    mapDrawer.attr("xmlns:ss", "http://smartcity.ddns.net");
    libraryDrawer = SVG().addTo('#library').size(180,1000).id("librarycontainer");
    _linksgroup = mapDrawer.group();
    _linksgroup.attr("id", "links");
    visualisationCore.robotgridgroup = mapDrawer.group();
    visualisationCore.robotgridgroup.attr("id", "robotgrid");

    // Insert your testcode below
    
    //draw.svg(drone_icon);
    //draw.svg(car_gas);


  });
var visualisationCore = {
    canvasWidth : 1000,
    canvasHeight : 1000,
    robotgridgroup: null,
    gridCellSize : 100,
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
        return this._drawAndTranslate(x,y,drone_icon,mapDrawer);
    },
    drawDroneTargetted : function(x,y){
        return this._drawAndTranslate(x,y,drone_icon_targetted,mapDrawer);
    },
    drawRaceCar : function(x,y){
        var obj = this._drawAndTranslate(x,y,racecaricon,mapDrawer);
        obj.scale(8);
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
    drawRobotStartPoint : function(x,y){
        return this._drawAndTranslate(x,y,robotcharge_pointA,mapDrawer);
    },
    drawRobotEndPoint : function(x,y){
        return this._drawAndTranslate(x,y,robotcharge_pointB,mapDrawer);
    },
    drawRobotIcon : function(x,y){
        return this._drawAndTranslate(x,y,roboticon,mapDrawer);
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
    drawRobotGrid : function(){
        // var cellWidth = 100;
        // var cellHeight = 100;
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
    }
}

function _sayHelloWorldByConsultant(){
    return "ciao";
}
