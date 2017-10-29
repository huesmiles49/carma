var map, otherUserMarker, currentUserMarker, parkingSpotMarker;

// Displays the user's location in the Google Map
function map() {
  var displayMap = {
    center: new google.maps.LatLng(34.0678976, -118.1675802),
    zoom: 17,
    fullscreenControl: false,
    mapTypeControl: false,
    streetViewControl: false,
    zoomControl: false,
    gestureHandling: "none",
    keyboardShortcuts: false,
    mapTypeControl: false,
    scaleControl: false,
    scrollWheel: false,
    panControl: false,
    disableDoubleClickZoom: true
  };
  map = new google.maps.Map(document.getElementById("map"), displayMap);
}

// Dynamically display the matched users locations
function displayMatchLocation(matchUser) {
  // Parse JSON array
  var match = JSON.parse(matchUser);

  // populate table with elements
  document.getElementById("name").innerHTML = match.otherUserName;
  document.getElementById("car").innerHTML = match.otherUserCar;
  document.getElementById("location").innerHTML = match.parkingSpotLocation;
  // document.getElementById("level").innerHTML = match.parkingSpotLevel;
  document.getElementById("comment").innerHTML = match.parkingSpotComment;

  // set marker title as otherUserName
  otherUserMarker.setTitle(match.otherUserName);

  // create pin for parking spot
  var parkingSpotLatLong = {
    lat: parseFloat(match.parkingSpotGPSLat),
    lng: parseFloat(match.parkingSpotGPSLong)
  };

  parkingSpotMarker = new google.maps.Marker({
    position: parkingSpotLatLong,
    map: map,
    icon: {
      url: "img/car.svg",
      scaledSize: {
        height: 60,
        width: 55
      },
      size: new google.maps.Size(60, 55)
       },
    title: "Car"
  });

  //add match cookie
  document.cookie = "MATCHID=" + match.matchID + "; path=/";
  console.log(this);
  var disable;
}
// checks browser if it supports GPS, then it registers geoFindMe()
// When browser detects GPS changes, it then calls geoFindMe()
function allowGPS() {
  currentUserMarker = new google.maps.Marker({
    position: {
      lat: 0,
      lng: 0
    },
    map: map,
    icon: {
      url: "img/user1.svg",
      scaledSize: {
        height: 60,
        width: 55
      },
      size: new google.maps.Size(60, 55),
      optimized: false
       },
    title: "Me"
  });

  // initial otherUserMarker
  otherUserMarker = new google.maps.Marker({
    position: {
      lat: 0,
      lng: 0
    },
    map: map,
    icon: {
      url: "img/user2.svg",
      scaledSize: {
        height: 60,
        width: 55
      },
      size: new google.maps.Size(60, 55),
      optimized: false
       },
    title: "Other User"
  });

  if (navigator.geolocation) {
    navigator.geolocation.watchPosition(geoFindMe);
  }
}

// current user's GPS location
function geoFindMe(position) {
  var lat = position.coords.latitude;
  var long = position.coords.longitude;

  var currentUserLatLong = {
    lat: lat,
    lng: long
  };
  currentUserMarker.setPosition(currentUserLatLong);

  // call the serverInterface:sendMatchGPS(converFormToJSON(currentUserLocation))
  sendMatchGPS(convertLatLongToJSON(lat, long));
}

// set pin for the current user
function otherUserPin(lat, long) {
  otherUserMarker.setPosition({
    lat: parseFloat(lat),
    lng: parseFloat(long)
  });

  //  make an array of the LatLng's of the markers you want to show
  var centerMap = new Array(otherUserMarker.getPosition(), currentUserMarker.getPosition(), parkingSpotMarker.getPosition());
  //  Create a new viewpoint bound
  var bounds = new google.maps.LatLngBounds();
  //  go through each...
  for (var i = 0; i < centerMap.length; i++) {
    //  And increase the bounds to take this point
    bounds.extend(centerMap[i]);
  }
}