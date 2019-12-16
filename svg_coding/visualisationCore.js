var draw;
window.addEventListener('load', (event) => {
    console.log('## SmartCity SVG Visualisation Core ##');
    var canvasWidth = 1000;
    var canvasHeight = 1000;
    draw = SVG().addTo('body').size(canvasWidth, canvasHeight).id("hellloooow");
    //var rect = draw.rect(100, 100).attr({ fill: '#f06' });

    // Insert your testcode below
    
    //draw.svg(drone_icon);
    //draw.svg(car_gas);


  });
var visualisationCore = {
    helloWorld: "Hi there",
    sayHelloWorld : function(a){
        draw.svg(drone_icon);
        console.log(a);
        return "You asked me to say hello world. Hello world.";
    },
    sayHelloWorldByConsultant : function(){
        return _sayHelloWorldByConsultant();
    },
    drawCarGas : function(x, y){
        return this._drawAndTranslate(x,y,car_gas);
    },
    drawCarGasStartPoint : function(x,y){
        return this._drawAndTranslate(x,y,car_gas_A);
    },
    drawCarGasEndPoint : function(x,y){
        return this._drawAndTranslate(x,y,car_gas_B);
    },
    drawDroneHelipad : function(x,y){
        return this._drawAndTranslate(x,y,drone_h);
    },
    drawDroneHelipadStartPoint : function(x,y){
        return this._drawAndTranslate(x,y,drone_h_pointA);
    },
    drawDroneHelipadEndPoint : function(x,y){
        return this._drawAndTranslate(x,y,drone_h_pointB);
    },
    drawDrone : function(x,y){
        return this._drawAndTranslate(x,y,drone_icon);
    },
    drawDroneTargetted : function(x,y){
        return this._drawAndTranslate(x,y,drone_icon_targetted);
    },
    drawRaceCar : function(x,y){
        var obj = this._drawAndTranslate(x,y,racecaricon);
        obj.scale(8);
        return obj;
    },
    drawRaceCarTargetted : function(x,y){
        var obj = this._drawAndTranslate(x,y,raceCarTargetted);
        //obj.transform({scale:10})
        obj.scale(8); // Racecar is drawed on a smaller size
        return obj;
    },
    drawRobotCharge : function(x,y){
        return this._drawAndTranslate(x,y,robotcharge);
    },
    drawRobotStartPoint : function(x,y){
        return this._drawAndTranslate(x,y,robotcharge_pointA);
    },
    drawRobotEndPoint : function(x,y){
        return this._drawAndTranslate(x,y,robotcharge_pointB);
    },
    drawRobotIcon : function(x,y){
        return this._drawAndTranslate(x,y,roboticon);
    },
    drawRobotIconTargetted : function(x,y){
        return this._drawAndTranslate(x,y,robotTargetted);
    },
    _drawAndTranslate : function(x,y, template){
        var obj = draw.svg(template);
        obj = obj.last();
        obj.transform({translateX:x,translateY:y});
        return obj;
    }




    


}

function _sayHelloWorldByConsultant(){
    return "ciao";
}
