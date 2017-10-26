// Displays the user's location in the Google Map
function map() {
  var displayMap = {
    center: new google.maps.LatLng(34.0678976, -118.1675802),
    zoom: 15,
    fullscreenControl: false,
    mapTypeControl: false,
    streetViewControl: false,
    zoomControl: false,
  };
  var map = new google.maps.Map(document.getElementById("map"), displayMap);
}

// Dynamically display the matched users locations
function displayMatchLocation(matchUser) {
  // Parse JSON array
  var match = JSON.parse(matchUser);
  // var location, i, print = "";

  // populate table with elements
  document.getElementById("name").innerHTML = match.otherUserName;
  document.getElementById("location").innerHTML = match.parkingSpotLocation;
  console.log(this);
  var disable;
}
// checks browser if it suports GPS, then it registers geoFindMe()
// When browser detects GPS changes, it then calls geoFindMe()
function allowGPS() {
  if (navigator.geolocation) {
    navigator.geolocation.watchPosition(geoFindMe);
  }
}

// current user's GPS location
function geoFindMe(position) {
  var lat = position.coords.latitude;
  document.getElementById("latitude").value = lat;

  var long = position.coords.longitude;
  document.getElementById("longitude").value = long;

  // set pin for the current user

  // call the serverInterface:sendMatchGPS(converFormToJSON(currentUserLocation))
  sendMatchGPS(converFormToJSON(currentUserLocation));
}