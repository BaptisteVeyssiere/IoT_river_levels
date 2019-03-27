jQuery('#sensor_list a')
    .click(function() {
        changeSensor(this);
        return false;
    });

function changeSensor(elem)
{
    $('#sensor_list a.active').removeClass('active');
    $(elem).addClass('active');
    if (!$(".dropdown-content a.active").length) {
        $(".dropdown-content a:last").addClass("active");
    }
    var timestamp = $(".dropdown-content a.active").text();
    $.ajax({
        type: "POST",
        url: "station_info.jsp",
        data: "name=" + $(elem).text() + "&timestamp=" + timestamp,
        success: function(data) {
            $("#selected").html(data);
        }
    });
    alert($(elem).text());
}

jQuery('.dropdown-content a')
    .click(function() {
        changeRecord(this);
        return false;
    });

function changeRecord(elem)
{
    $(elem).addClass('active');
    var name = $('#sensor_list a.active').text();
    $.ajax({
        type: "POST",
        url: "station_info.jsp",
        data: "name=" + name + "&timestamp=" + $(elem).text(),
        success: function(data) {
            $("#selected").html(data);
        }
    });
    alert($(elem).text());
}