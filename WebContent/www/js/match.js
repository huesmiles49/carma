// Displays the user's location in the Google Map
function myMap() {

  navigator.geolocation.getCurrentPosition(function (position) {
    var currentLatitude = position.coords.latitude;
    var currentLongitude = position.coords.longitude;

    var infoWindowHTML = "Latitude: " + currentLatitude + "<br>Longitude: " + currentLongitude;
    var infoWindow = new google.maps.InfoWindow({
      map: map,
      content: infoWindowHTML,
      zoom: 18,
      position: currentLocation
    });

    var currentLocation = {
      lat: currentLatitude,
      lng: currentLongitude
    };
    infoWindow.setPosition(currentLocation);
    // document.getElementById("btnAction").style.display = 'none';
  });

  var currentLocation = new google.maps.LatLng(0, 0);
  var mapCanvas = document.getElementById("map");
  var mapOptions = {
    center: currentLocation,
    zoom: 18,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
  };

  var map = new google.maps.Map(mapCanvas, mapOptions);
  var marker = new google.maps.Marker({
    position: currentLocation
  });
  marker.setMap(map);
}

// Dynamically display the matched users locations
function displayMatchLocation(matchUser) {
  // Parse JSON array
  var match = JSON.parse(matchUser);
  // var location, i, print = "";

  // // iterate through list to display each element
  // for (i in list) {
  //     print += "<tr id=" + list[i].id + " class=\"inactive\">";
  //     console.log(list[i].id);
  //     // TODO: add avatar to represent the location later, if time permits
  //     // print += "<td>" + list[i].avatar + "</td>";
  //     print += "<td>" + list[i].location + "</td>";
  //     print += "<td>" + list[i].timeSwap + "</td>";
  //     print += "<td>" + list[i].comment + "</td>";
  //     print += "</tr>";
  // }
  // populate table with elements
  document.getElementById("location").innerHTML = match.parkingSpotLocation;
  console.log(this);
  var disable;

  // Checks if document is ready
  // $(document).ready(function () {
  //     // Pass the reserved id to the server when clicked
  //     $('tr').click(function () {
  //         console.log(this);
  //         // jQuery method will directly pass JSON to server
  //         $.post("http://localhost:8080/cs3337group3/match", {
  //             id: this.id,
  //         });
  //         console.log("Sending id#: " + this.id + " to server.");
  //         // Change the color of the row clicked
  //         $(this).css("background-color", "rgb(187, 247, 174)");
  //         // Disable further clicks
  //         diable = $(this).removeAttr("onclick");
  //         console.log(disable);
  //     });
  // });
}