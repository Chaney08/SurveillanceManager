$(document).ready(function() {

    console.log("Testing");
    var nowTemp = new Date();
    var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);


    var checkin = $('#dp1').datepicker({

        dateFormat: 'yyyy-mm-dd',
        beforeShowDay: function(date) {
            return date.valueOf() >= now.valueOf();
        },
        autoclose: true

    }).on('changeDate', function(ev) {
        console.log(ev.date.valueOf());
        console.log(checkout.val());
        console.log(ev.date.valueOf() > checkout.val());
        if (ev.date.valueOf() > checkout.data('date') || !checkout.val()) {

            console.log("Testing");
            var newDate = new Date(ev.date);
            newDate.setDate(newDate.getDate() + 1);
            checkout.datepicker("update", newDate);

        }
        $('#dp2')[0].focus();
    });


    var checkout = $('#dp2').datepicker({
        dateFormat: 'yyyy-mm-dd',
        beforeShowDay: function(date) {
            if (!checkin.val()) {
                return date.valueOf() >= new Date().valueOf();
            } else {
                return date.valueOf() > checkin.datepicker("getDate").valueOf();
            }
        },
        autoclose: true

    }).on('changeDate', function(ev) {});

});