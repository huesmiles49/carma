/** This file will provide controls for the reserver home page. */

/** Display listed spots in the table */
function displayListOfSpots(spots) {
    var list = JSON.parse(spots);
    var spot, i, print = "";

    // console.log(list);
    for (i in list) {
        print += "<tr>";
        //adding avatar to represent the location could be done later
        // print += "<td>" + list[i].avatar + "</td>";
        print += "<td>" + list[i].location + "</td>";
        print += "<td>" + list[i].timeSwap + "</td>";
        print += "<td>" + list[i].comment + "</td>";
        print += " </tr> ";
    }
    document.getElementById("reserve-table-row").innerHTML = print;
}