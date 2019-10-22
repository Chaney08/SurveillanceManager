$(document).ready(function() {


    var inRange = false;
    $('#dp1').datepicker({
        multidate: true,
        todayHighlight: true,
        minDate: 0,
    });

    $('#dp1').datepicker('setDates', [new Date(2019, 10, 10), new Date(2019, 10, 11), new Date(2019, 10, 20)])
});