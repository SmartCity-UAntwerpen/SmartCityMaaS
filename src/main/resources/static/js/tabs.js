/**
 * Created by Vincent on 17/03/16.
 */
var notiCount = 0; // Totaal aantal notificaties
var contentDest = $(".inside-con"); // Plaats waar de nieuwe inhoud van de webapplicatie getoond wordt
var japGppStr = [];
var japGppVersion = (new Date).getFullYear(); // Versie voor het JAP en het GPP begint standaard bij het huidige jaartal
var prevTab = null;

var today = new Date();
var dd = today.getDate();
var mm = today.getMonth()+1; //January is 0!

var yyyy = today.getFullYear();
if(dd<10){
    dd='0'+dd
}
if(mm<10){
    mm='0'+mm
}
var todayString = yyyy + "-" + mm + "-" + dd;

$(document).ready(function() {


    /******* COOKIES *******/
    /******* SIDEMENU *****/
        // Als er geklikt wordt op delen van het navigatie, wordt dit opgelsagen in cookies
    $("ul#side-nav li ul li a").click(function(e) {
        if( $(this).hasClass("delivery-link")) window.location.href='/deliveries/put';
        else if( $(this).hasClass("visualisation-link") ) window.location.href='/visualization';
    });
    $("ul#side-nav li a.nav-ch").click(function(e) {
        if(!$(this).hasClass("active")) {
            var tabNum = $(this).index();
            $("ul#side-nav li a.active").trigger("click").removeClass("active");
            $(this).addClass("active");

        } else {

        }
    });



    /******* SIDE MENU ******/
        // Het menu bij smartphones zichtbaar of onzichtbaar maken
    $("[data-toggle='offcanvas']").click(function(e) {
        $("#side-menu").toggleClass("hidden-xs");
    });




    /*********** ADMIN PANEL ***********/
    $("#admin-settings").click(function(e) {
        // Load the settings part
        // Extra informatie over welk deel getoond wordt
        $("#top-title").html("AP - settings");

        // Inhoud van webapplicatie laden
    });

    $("#admin-vzw").click(function(e) {
        // Load the settings part
        // Extra informatie over huidig deel van webapplicatie
        $("#top-title").html("AP - VZW's beheren");

        // Inhoud van de webapplicatie laden
    });

    $("#admin-onderwijs").click(function(e) {
        //Load the settings part
        $("#top-title").html("AP - Onderwijsniveau's");
    });

    $("#admin-campus").click(function(e) {
        //Load the settings part
        $("#top-title").html("AP - Campus");
    });

    $("#admin-register").click(function(e) {
        $("#top-title").html("AP - Gebruiker registreren");
    });

    $("#admin-gebruikers").click(function(e) {
        //Load the settings part
        $("#top-title").html("AP - Gebruikers beheren");
    });


    /**** SNELTOETSEN ****/
    $(document).on("click tab", ".shortcut-btn", function(e) {
        var thisID = $(this).attr("id");
        var thisTabInfo = thisID.split("_:_");
        $("#"+thisTabInfo[0]).click();
        $("#"+thisTabInfo[1]).click();
    });


    $("#dashboard-show-1").click(function(e) {
        $("#top-title").html("Dashboard");
    });

    /********** VERSLAGEN **********/
    var path = "";
    $("#dashboard-show-5").click(function(e) {
        $("#top-title").html("Verlsagen");
    });


    /********** KECO *********/
    $("#keco-show").click(function(e) {
        //Hier ajax request functie maken
        $("#top-title").html("RaKeCo weergeven");
    });

    $("#keco-add").click(function(e) {
        $("#top-title").html("RaKeCo toeveogen");
    });

    $("#keco-noti").click(function(e) {
        $("#top-title").html("RaKeCo notificaties")
    });


    /********* JAP *********/
    $("#jap-show").click(function(e) {
        $("#top-title").html("JAP");

    });

    $("#jap-av").click(function(e) {
        $("#top-title").html("JAP - Aandacht vereist");
    });

    $("#jap-ow").click(function(e) {
        $("#top-title").html("JAP - Open wijzigingen");
    });


    /******* GPP *******/
    $("#gpp-av").click(function(e) {
        $("#top-title").html("GPP - Aandacht vereist");
    });

    $("#gpp-ow").click(function(e) {
        $("#top-title").html("GPP - Open wijzigingen");
    });

    $("#gpp-show").click(function(e) {
        $("#top-title").html("GPP");
    });





    /******* REDIRECT *****/
    $(document.body).on("click tab", "img.logo", function(e) {
        // Link naar gidazo.be
    });
});
