var mapDrawer;
window.addEventListener('load', (event) => {
    console.log('## SmartCity SVG Visualisation Core ##');
    var canvasWidth = 1000;
    var canvasHeight = 1000;
    mapDrawer = SVG().addTo('#map').size(canvasWidth, canvasHeight).id("mapcontainer");
    libraryDrawer = SVG().addTo('#library').size(180,1000).id("librarycontainer");
    //var rect = draw.rect(100, 100).attr({ fill: '#f06' });

    // Insert your testcode below
    
    //draw.svg(drone_icon);
    //draw.svg(car_gas);


  });
var visualisationCore = {
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
    drawLibrary : function(){
        // Plot a library for the mapbuilder
        // Car gas, drone helipad, robot tiles
        // Displayed in a grid of 2 by ..
        // Dimensions of each cell are 90px*90px
        var library = [car_gas, drone_h];
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
