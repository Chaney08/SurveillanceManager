//Using var variables here for the datepickers for use in adding excemptions
var checkin;
var checkout

$(document).ready(function() {

    //Need to load dates into datepicker field on startup since the fields linked to Java are now hidden
    let startDate = $('#startDate').val();
    let endDate = $('#endDate').val();
    $('#dp1').val(startDate);
    $('#endDateTimePicker').val(endDate);

    //Initialising any datepickers that will already be on excemptions list in screen
    $(".datepick").datepicker({});

    let nowTemp = new Date();
    let now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);


    //Using var for the datepickers for use in adding excemptions
    checkin = $('#dp1').datetimepicker({
        beforeShowDay: function(date) {
            return date.valueOf() >= now.valueOf();
        },
        autoclose: true

    }).on('changeDate', function(ev) {
        if (ev.date.valueOf() > checkout.data('date') || !checkout.val()) {

            let newDate = new Date(ev.date);
            newDate.setDate(newDate.getDate() + 1);
            //Update the EndDate to auto fill the day after start date
            checkout.datetimepicker("update", newDate);

            //Cannot get DateTimePicker into the correct format so I use a hidden field and convert the date value - Also need to increment by 2 to get accurate time, not enough time to be messing with timezones
            let isoDate = new Date(ev.date);
            isoDate.setHours(isoDate.getHours() + 2);
            isoDate = isoDate.toISOString();
            $('#startDate').val(isoDate);

            if (checkin.val() && checkout.val()) {
                $('#addExcemptionButton').prop('disabled', false);
            }

        }
         $('#endDateTimePicker')[0].focus();
    });


    checkout = $('#endDateTimePicker').datetimepicker({
        beforeShowDay: function(date) {
            if (!checkin.val()) {
                return date.valueOf() >= new Date().valueOf();
            } else {
                return date.valueOf() > checkin.datepicker("getDate").valueOf();
            }
        },
        autoclose: true

    }).on('changeDate', function(ev) {
        //Cannot get DateTimePicker into the correct format so I use a hidden field and convert the date value - Also need to increment by 2 to get accurate time, not enough time to be messing with timezones
        let isoDate = new Date(ev.date);
        isoDate.setHours(isoDate.getHours() + 2);
        isoDate = isoDate.toISOString();
        $('#endDate').val(isoDate);
        if (checkin.val() && checkout.val()) {
            $('#addExcemptionButton').prop('disabled', false);
        }
    });


    //We want to disable adding excemptions until the original shcedule is filled as we only want the relevant days available for excemptions
    if (!checkin.val() && !checkout.val()) {
        $('#addExcemptionButton').prop('disabled', true);
    }

});

//Dynamically add Excemption rows
let addExcemption = function () {
    let listName = 'scheduleExcemptions'; //Name of the exemption list in Schedule.class
    let fieldsNames = ['excemptionDate', 'id']; //field names required by Excemption
    let rowIndex = document.querySelectorAll('.datepick').length;

    let row = document.createElement('div');
    row.classList.add('row', 'item');

    //Destroy datepickers so that we can recreate - This is for dynamic datepicker additions
    $(".datepick").datepicker("destroy");

    //Loop here is not really required - I just put it in to make it very easily expandable - If we want to add another field, we just add a fieldname to array above
    fieldsNames.forEach((fieldName) => {
        let col = document.createElement('div');
        let input = document.createElement('input');

        col.classList.add('col', 'form-group');
        if (fieldName === 'id') {
            col.classList.add('d-none'); //field with id - hidden
        }
        if(fieldName === 'excemptionDate'){
            input.classList.add('datepick');
        }
        input.type = 'text';
        input.classList.add('form-control');
        input.id = listName + rowIndex + '.' + fieldName;
        input.setAttribute('name', listName + '[' + rowIndex + '].' + fieldName);

        col.appendChild(input);
        row.appendChild(col);
    });

    //we need to add delete button to end - TODO : running out of time so removing as not implemented yet, implement if time left over
    /*let col = document.createElement('span')
    let button = document.createElement('button');
    button.classList.add("btn", "btn-primary","a-btn-slide-text");
    button.innerText = "Remove Excemption";
    button.onclick = deleteExcemption(this);
    button.type = "Button";
    col.appendChild(button);
    row.appendChild(col);*/

    
    document.getElementById('excemptionTable').appendChild(row);
    //We create the datepicker on newly generated elements and ensure that the only avalable dates are between the start and end date selected already
    $(".datepick").datepicker({
        beforeShowDay: function(date) {
            return date.valueOf() > checkin.data("datetimepicker").getDate().valueOf() && date.valueOf() < checkout.data("datetimepicker").getDate().valueOf();
        },
    }).on('changeDate', function(ev) {
        //Cannot get DateTimePicker into the correct format so I use a hidden field and convert the date value - Also need to increment by 2 to get accurate time, not enough time to be messing with timezones
        let isoDate = new Date(ev.date);
        isoDate.setHours(isoDate.getHours() + 2);
        isoDate = isoDate.toISOString();
        $('#endDate').val(isoDate);
    });;
};

function deleteExcemption(element){

}
