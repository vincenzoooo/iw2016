/* 
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */

var pageHeight = $(window).height();
var pageWidth = $(window).width();


function dimentionScreen() {
    if( pageWidth > 768 ) {	
        $('.pageContent').height(pageHeight-220);
    }
}

dimentionScreen();