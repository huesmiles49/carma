// This file will have all the connections needed to send/receive data from the server.

// The server address, change this to the webserver you want to use
var server = "http://localhost:8080/";

// TODO: add a return function to track userID

/** Send registration JSON to server with XMLHTTPRequest
 * @param JSON, the JSON to pass to the server
 */
function sendRegistration(newUser) {
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", server + "/cs3337group3/registration", true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(newUser);
}

// Checks if server is OK, then request server for the JSON array of all parking spots
function getList() {
	var xhttp;
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState === 4 && this.status === 200) {
			console.log(this.responseText)
			displayListOfSpots(this.responseText);
		}
	};
	xhttp.open("GET", server + "/cs3337group3/listParkingSpots", true);
	xhttp.send();
}