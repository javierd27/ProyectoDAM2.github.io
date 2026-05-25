/*
    @author Javier
    Proyecto: Hotelia
    Fecha: 2026
*/

const contenedores = document.querySelectorAll(".imagen-container");

contenedores.forEach(function(contenedor){

    const imagen = contenedor.querySelector("img");
    const overlay = contenedor.querySelector(".overlay");

    contenedor.addEventListener("click", function(){

        imagen.style.filter = "blur(0px)";
        imagen.style.transform = "scale(1.03)";

        overlay.style.opacity = "0";
        overlay.style.pointerEvents = "none";

    });

});