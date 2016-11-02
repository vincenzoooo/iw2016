/* 
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
$(document).ready(function() {
    
    var pageHeight = $(window).height();
    var pageWidth = $(window).width();

    function dimentionScreen() {
        if( pageWidth > 768 ) {	
            var valore= pageHeight-220;
            document.getElementById("pageContent").style.minHeight = valore+"px";
        }
    }

    dimentionScreen(); 

});


    

