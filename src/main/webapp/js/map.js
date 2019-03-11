/* exported createMap */
/* exported google */

function createMap(){

  const map = new window.google.maps.Map(document.getElementById('map'), {
    center: {lat: 37.422403, lng: -122.088073},
    zoom: 15
  });

  addLandmark(map, 37.423829, -122.092154, 'Google West Campus', 'Google West Campus is home to YouTube and Maps.')
  addLandmark(map, 37.421903, -122.084674, 'Stan the T-Rex', 'This is Stan, the T-Rex statue.')
  addLandmark(map, 37.420919, -122.086619, 'Permanente Creek Trail', 'Permanente Creek Trail connects Google to a system of bike trails.');
  initMap(map);
}

/** Adds a marker that shows an InfoWindow when clicked. */
function addLandmark(map, lat, lng, title, description){

  const marker = new window.google.maps.Marker({
    position: {lat: lat, lng: lng},
    map: map,
    title: title
  });

  var infoWindow = new window.google.maps.InfoWindow({
    content: description
  });
  marker.addListener('click', function() {
    infoWindow.open(map, marker);
  });
}

/** Adds a Text Block that shows User Location upon Opening the Map */
function initMap(map) {
  var infoWindow = new window.google.maps.InfoWindow;
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var pos = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
      infoWindow.setPosition(pos);
      infoWindow.setContent('Location found.');
      infoWindow.open(map);
      map.setCenter(pos);
    }, function() {
        handleLocationError(true, infoWindow, map.getCenter());
      });
    } else {
        handleLocationError(false, infoWindow, map.getCenter());
    }
}

/** Handles location not found errors is initMap */
function handleLocationError(browserHasGeolocation, infoWindow, pos) {
  infoWindow.setPosition(pos);
  infoWindow.setContent('Location found.');
  infoWindow.setContent(browserHasGeolocation ?
    'Error: The Geolocation service failed.' :
    'Error: Your browser doesn\'t support geolocation.');
  infoWindow.open(map);
}
