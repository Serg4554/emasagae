/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var imagen = document.createElement("img");

function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else { 
        x.value = "Este navegador no soporta geolocalizacion.";
    }
}

function showPosition(position) {
    document.getElementById("form:lat").value = position.coords.latitude;
    document.getElementById("form:lon").value = position.coords.longitude;
    imagen.src = "https://maps.googleapis.com/maps/api/streetview?size=600x300&location="+position.coords.latitude+","+position.coords.longitude+"&heading=151.78&pitch=-0.76&key=AIzaSyAgYEiQOMHSJh-oq5zPCsZYk2z6FagzSgA";
    document.getElementById("image").appendChild(imagen);
    
}
