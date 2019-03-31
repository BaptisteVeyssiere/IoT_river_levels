var monthNames = [
    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
];
$(function() {
    $('#sensor_list a:first').addClass('active');
    $("#slider").slider({
        range: "max",
        min: 0,
        max: 167,
        value: 167,
        slide: function(event, ui) {
            var currentDate = new Date();
            currentDate.setHours(currentDate.getHours() - 167 + ui.value);

            var date = currentDate.getDate();
            var month = currentDate.getMonth();
            var year = currentDate.getFullYear();
            var hours = currentDate.getHours();

            var dateString = ("0" + hours).slice(-2) + ":" + ("0" + minutes).slice(-2) + ":" + ("0" + seconds).slice(-2) + " " + ("0" + date).slice(-2) + " " + monthNames[month] + " " + year;
            $("#date_view").html(dateString);
        },
        stop: function(event, ui) {
            var name = $('#sensor_list a.active').text();
            var timestamp = Date.parse($("#date_view").html());
            $.ajax({
                type: "POST",
                url: "station_info.jsp",
                data: "name=" + name + "&timestamp=" + timestamp,
                success: function(data) {
                    $("#selected").html(data);
                }
            });
            $.ajax({
                type: "POST",
                url: "flood_warning_list.jsp",
                data: "timestamp=" + timestamp,
                success: function(data) {
                    removePolygons();
                    if (data) {
                        var warnings = JSON.parse(data);
                        warnings.forEach(function (warning) {
                            drawPolygon(warning.flood_severity, JSON.parse(warning.flood_polygon));
                        });
                    }
                }
            });
        }
    });
    var currentDate = new Date();
    currentDate.setHours(currentDate.getHours() - 167 + $("#slider").slider("value"));

    var date = currentDate.getDate();
    var month = currentDate.getMonth();
    var year = currentDate.getFullYear();
    var hours = currentDate.getHours();
    var minutes = currentDate.getMinutes();
    var seconds = currentDate.getSeconds();

    var dateString = ("0" + hours).slice(-2) + ":" + ("0" + minutes).slice(-2) + ":" + ("0" + seconds).slice(-2) + " " + ("0" + date).slice(-2) + " " + monthNames[month] + " " + year;
    $("#date_view").html(dateString);
    $.ajax({
        type: "GET",
        url: "station_list.jsp",
        success: function(data) {
            var stations = JSON.parse(data);
            stations.forEach(function(station) {
                addMarker(station.name, parseFloat(station.latitude), parseFloat(station.longitude), "own");
            });
        }
    });
    var timestamp = Date.parse($("#date_view").html());
    $.ajax({
        type: "POST",
        url: "flood_warning_list.jsp",
        data: "timestamp=" + timestamp,
        success: function(data) {
            if (data) {
                var warnings = JSON.parse(data);
                warnings.forEach(function (warning) {
                    drawPolygon(warning.flood_severity, JSON.parse(warning.flood_polygon));
                });
            }
        }
    });
    var name = $('#sensor_list a.active').text();
    $.ajax({
        type: "POST",
        url: "station_info.jsp",
        data: "name=" + name + "&timestamp=" + timestamp,
        success: function(data) {
            $("#selected").html(data);
        }
    });
});

jQuery('#sensor_list a')
    .click(function() {
        changeSensor(this);
        return false;
    });

function changeSensor(elem)
{
    $('#sensor_list a.active').removeClass('active');
    centerOnMarker($(elem).text());
    $(elem).addClass('active');
    var timestamp = Date.parse($("#date_view").html());
    $.ajax({
        type: "POST",
        url: "station_info.jsp",
        data: "name=" + $(elem).text() + "&timestamp=" + timestamp,
        success: function(data) {
            $("#selected").html(data);
        }
    });
}