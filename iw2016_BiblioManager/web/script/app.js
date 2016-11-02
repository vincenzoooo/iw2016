/* 
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */

$(document).ready(function() {
    
    if(document.getElementById("editComponent")!=null) {
        $('html, body').animate({
        scrollTop: $(".scollTo").offset().top	
        },1000); 
    }
    //MENU SCROLL TO
    function scroll_to(div){
        $('html, body').animate({
            scrollTop: $(div).offset().top	
        },1000);
    }
    
});

