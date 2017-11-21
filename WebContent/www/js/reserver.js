//This file will provide controls for the reserver home page.

// Display listed spots in a table
function displayListOfSpots(spots) {
    // Parse JSON array
    var list = JSON.parse(spots);
    var spot, i, print = "";

    // iterate through list to display each element
    for (i in list) {
        print += "<tr id=" + list[i].id + " class=\"inactive\">";
        console.log(list[i].id);
        // TODO: add avatar to represent the location later, if time permits
        // print += "<td>" + list[i].avatar + "</td>";
        print += "<td>" + list[i].location + "</td>";
        print += "<td>" + list[i].timeSwap + "</td>";
        print += "<td>" + list[i].comment + "</td>";
        print += "</tr>";
    }
    // populate table with elements
    document.getElementById("reserve-table-row").innerHTML = print;
    console.log(this);
    var disable;

    // Checks if document is ready
    $(document).ready(function () {
        // Pass the reserved id to the server when clicked
        $('tr').click(function () {
            console.log(this);
            // jQuery method will directly pass JSON to server
            $.post("http://localhost:8080/cs3337group3/listParkingSpots", {
                id: this.id,
                GPS_Lat: document.getElementById("latitude").value,
                GPS_Long: document.getElementById("longitude").value,
            });      
            console.log("Sending id#: " + this.id + " to server.");
            console.log("Sending gps#: " + document.getElementById("latitude").value + ", " + document.getElementById("longitude").value + " to server.");          
            // Change the color of the row clicked
            $(this).css("background-color", "rgb(187, 247, 174)");
            // Disable further clicks
            diable = $(this).removeAttr("onclick");
            console.log(disable);
        });
    });
}