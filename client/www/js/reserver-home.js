//This file will provide controls for the reserver home page.

// Display listed spots in a table
function displayListOfSpots(spots) {
    // Parse JSON array
    var list = JSON.parse(spots);
    var spot, i, print = "",
        currentIDReserved;
    // console.log(list);

    // iterate through list to display each element
    for (i in list) {
        print += "<tr id=" + list[i].id + ">";
        console.log(list[i].id);
        // TODO: can add avatar to represent the location later, if time permits
        // print += "<td>" + list[i].avatar + "</td>";
        print += "<td>" + list[i].location + "</td>";
        print += "<td>" + list[i].timeSwap + "</td>";
        print += "<td>" + list[i].comment + "</td>";
        print += "</tr>";
    }
    // populate table with elements
    document.getElementById("reserve-table-row").innerHTML = print;

    // jQuery method will directly pass JSON to server
    $(document).ready(function () {
        $('tr').click(function () {
            console.log(".click");
            $.post("http://localhost:8080/cs3337group3/listParkingSpots", {
                id: this.id,
            });
            console.log("Sending id#: " + this.id + " to server.");
        });
    });
}