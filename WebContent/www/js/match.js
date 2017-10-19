function myMap() {
  
  navigator.geolocation.getCurrentPosition(function(position){ 
			var currentLatitude = position.coords.latitude;
			var currentLongitude = position.coords.longitude;

			var infoWindowHTML = "Latitude: " + currentLatitude + "<br>Longitude: " + currentLongitude;
      var infoWindow = new google.maps.InfoWindow({
        map: map, 
        content: infoWindowHTML, 
        zoom: 18, 
        position: currentLocation});

			var currentLocation = { lat: currentLatitude, lng: currentLongitude };
			infoWindow.setPosition(currentLocation);
			document.getElementById("btnAction").style.display = 'none';
		});

  var currentLocation = new google.maps.LatLng(0,0);
  var mapCanvas = document.getElementById("map");
  var mapOptions = {
    center: currentLocation,
    zoom: 18,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
  };

  var map = new google.maps.Map(mapCanvas, mapOptions);
  
  var marker = new google.maps.Marker({position:currentLocation});
  marker.setMap(map);

}






