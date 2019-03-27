var map;
function initMap() {
    var myLatLng = {lat: 51.296711, lng: 1.068117};

    map = new google.maps.Map(document.getElementById('map'), {
        center: myLatLng,
        zoom: 15
    });
}

function addMarker(name, latitude, longitude, from) {
    var image;
    if (from === "own") {
        image = "images/marker-icon-white-grey.png";
    } else {
        image = "images/marker-icon-grey.png";
    }
    var marker = new google.maps.Marker({
        position: {lat: latitude, lng: longitude},
        map: map,
        title: name,
        icon: {
            url: image
        }
    });
}