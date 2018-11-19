
$(document).ready(function() {


    // Als er geklikt wordt op delen van het navigatie, wordt dit opgelsagen in cookies
    $("ul#side-nav li ul li a").click(function(e) {
        if( $(this).hasClass("delivery-link")) window.location.href='/deliveries/put';
        else if( $(this).hasClass("visualisation-link") ) window.location.href='/visualization';
        else if( $(this).hasClass("all-users-link") ) window.location.href='/users';
        else if( $(this).hasClass("create-user-link") ) window.location.href='/users/put';
        else if( $(this).hasClass("manage-delivery-link") ) window.location.href='/jobs';
        else if( $(this).hasClass("create-delivery-link") ) window.location.href='/jobs/put';
        else if( $(this).hasClass("delivery-history-link") ) window.location.href='/deliveries';
    });

    $("ul#side-nav li a.nav-ch").click(function(e) {
        if(!$(this).hasClass("active")) {
            var tabNum = $(this).index();
            //$("ul#side-nav li a.active").trigger("click").removeClass("active");
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
    $("#admin-register").click(function(e) {
        $("#top-title").html("AP - Gebruiker registreren");
    });

    $("#admin-gebruikers").click(function(e) {
        //Load the settings part
        $("#top-title").html("AP - Gebruikers beheren");
    });




    $("#dashboard-show-1").click(function(e) {
        $("#top-title").html("Dashboard");
    });



    /********** KECO *********/
    $("#new-deliv").click(function(e) {
        //Hier ajax request functie maken
        $("#top-title").html("RaKeCo weergeven");
    });


    /********* VISUALISATION *********/
    $("#vis-show").click(function(e) {
        $("#top-title").html("JAP");

    });

    /******* REDIRECT *****/
    $(document.body).on("click tab", "img.logo", function(e) {
        // Link naar gidazo.be
    });
});
