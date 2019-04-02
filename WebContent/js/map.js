var map;
var polygon_list = [];
var marker_list = [];
function initMap() {
    var myLatLng = {lat: 51.296711, lng: 1.068117};

    map = new google.maps.Map(document.getElementById('map'), {
        center: myLatLng,
        zoom: 11
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
    marker.addListener('click', function() {
        changeSensor($('#sensor_list a:contains("' + marker.getTitle() + '")'));
    });
    marker_list.push(marker);
}

function centerOnMarker(name) {
    marker_list.forEach(function(elem) {
        if (elem.getTitle() == name) {
            map.setCenter(elem.getPosition());
            return(true);
        }
    });
    return(false);
}

function drawPolygon(severity, flood_area) {
    var colors = ['#E5FFCC', '#96FF32', '#FF9B32', '#FF3532'];
    var polygon = new google.maps.Polygon({
        paths: flood_area,
        strokeColor: colors[severity],
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: colors[severity],
        fillOpacity: 0.35
    });
    polygon.setMap(map);
    polygon_list.push(polygon);
}

function removePolygons() {
    polygon_list.forEach(function(elem) {
        elem.setMap(null);
    });
    polygon_list.length = 0;
}