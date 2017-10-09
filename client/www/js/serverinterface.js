/**
 * This file will have all the connections needed to send/receive data from the server
 */

/*
 * The server address, change this to the webserver you want to use
 */
var server="http://localhost:8080";

/**
 * Send registration JSON to server with XMLHTTPRequest
 * @param JSON, the JSON to pass to the server
 * @returns
 * TODO: add a return function, maybe start tracking userID
 */
function sendRegistration(userData) {
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", server + "/cs3337group3/registration", true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.onreadystatechange = function () {
		if (this.readyState === 4 && this.status === 200) {
			var user = JSON.parse(this.responseText);
			document.cookie="ID=" + user.id;
			document.cookie="CARID=" + user.car;

			window.location.href = "home.html";
		}
	};
	xhttp.send(userData);
}