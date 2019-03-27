jQuery('#sensor_list a')
    .click(function() {
        changeSensor(this);
        return false;
    });

function changeSensor(elem)
{
    jQuery('#sensor_list a.active').removeClass('active');
    $(elem).addClass('active');
    alert($(elem).text());
}

jQuery('.dropdown-content a')
    .click(function() {
        changeRecord(this);
        return false;
    });

function changeRecord(elem)
{
    alert($(elem).text());
}