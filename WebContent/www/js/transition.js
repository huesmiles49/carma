

//expected to be called from the callback function of the xmlhttprequest done on load to get the maxMatch
//@value the maxMatch returned from the /transition servlet
function setMaxMatchCookie(value) {
	document.cookie="MAXMATCH=" + value+ "; path=/";
}


//expected to be called by the callback function of the xmlhttprequest done every x secs to check if won
//will transition to the match.html if value (the return from doPost /transition) is > that MAXMATCH cookie value
function checkIfWinner(value) {
	maxMatch = parseInt(getCookie("MAXMATCH"));
	if(value > maxMatch){
		window.location.href = "match.html";
	}
}


//function from w3 school
function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}