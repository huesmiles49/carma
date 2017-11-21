//This file will provide controls for the reserver home page.

// Display listed spots in a table
function displayListOfSpots(spots) {
    // Parse JSON array
    var list = JSON.parse(spots);
    var spot, i, print = "";

    // iterate through list to display each element
    for (i in list) {
        console.log(list[i].id);

        if(list[i].reserved === "true") {
        	print += "<tr id=" + list[i].id + " class=\"reserved\">";
        	print += "<td>" + list[i].location + "</td>";
            print += "<td>" + list[i].timeSwap + "</td>";
            print += "<td>" + list[i].comment + "</td>";
            print += "</tr>";
        } else {
        	print += "<tr id=" + list[i].id + " onclick=sendReservation(" + list[i].id + ")>";
        	print += "<td>" + list[i].location + "</td>";
        	print += "<td>" + list[i].timeSwap + "</td>";
        	print += "<td>" + list[i].comment + "</td>";
        	print += "</tr>";
        }
    }
    // populate table with elements
    document.getElementById("reserve-table-row").innerHTML = print;
}