$(document).ready(function() {
    $('#displayCalender').datepicker({
        beforeShow: function(i) { if ($(i).attr('readonly')) { return false; } },
        todayHighlight: true,
    });
});