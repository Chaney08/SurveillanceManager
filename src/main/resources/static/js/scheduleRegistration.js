//Using var variables here for the datepickers for use in adding excemptions
var checkin;
var checkout

$(document).ready(function() {

    let nowTemp = new Date();
    let now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);


    //Using var for the datepickers for use in adding excemptions
    checkin = $('#dp1').datepicker({

        dateFormat: 'yyyy-mm-dd',
        beforeShowDay: function(date) {
            return date.valueOf() >= now.valueOf();
        },
        autoclose: true

    }).on('changeDate', function(ev) {
        if (ev.date.valueOf() > checkout.data('date') || !checkout.val()) {
            var newDate = new Date(ev.date);
            newDate.setDate(newDate.getDate() + 1);
            checkout.datepicker("update", newDate);

        }
        $('#dp2')[0].focus();
    });


    checkout = $('#dp2').datepicker({
        dateFormat: 'yyyy-mm-dd',
        beforeShowDay: function(date) {
            if (!checkin.val()) {
                return date.valueOf() >= new Date().valueOf();
            } else {
                $('#addExcemptionButton').prop('disabled', false);
                return date.valueOf() > checkin.datepicker("getDate").valueOf();
            }
        },
        autoclose: true

    }).on('changeDate', function(ev) {});


    //We want to disable adding excemptions until the original shcedule is filled as we only want the relevant days available for excemptions
    if (!checkin.val() && !checkout.val()) {
        $('#addExcemptionButton').prop('disabled', true);
    }

});

//Dynamically add Excemption rows
let addExcemption = function () {
    let listName = 'scheduleExcemptions'; //Name of the exemption list in Schedule.class
    let fieldsNames = ['excemptionDate', 'id']; //field names required by Excemption
    let rowIndex = document.querySelectorAll('.item').length; //we can add mock class to each movie-row

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

    //we need to add delete button to end
    let col = document.createElement('span')
    let button = document.createElement('button');
    button.classList.add("btn", "btn-primary","a-btn-slide-text");
    button.innerText = "Remove Excemption";
    button.onclick = deleteExcemption(this);
    button.type = "Button";
    col.appendChild(button);
    row.appendChild(col);

    
    document.getElementById('excemptionTable').appendChild(row);
    //We create the datepicker on newly generated elements and ensure that the only avalable dates are between the start and end date selected already
    $(".datepick").datepicker({
        dateFormat: 'yyyy-mm-dd',
        beforeShowDay: function(date) {
            return date.valueOf() > checkin.datepicker("getDate").valueOf() && date.valueOf() < checkout.datepicker("getDate").valueOf();;
        },
        autoclose: true
    });
};

function deleteExcemption(element){

}
