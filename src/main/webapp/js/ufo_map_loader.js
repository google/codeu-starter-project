function createUfoSightingsMap() {
  fetch("/ufo-data")
    .then(function(response) {
      return response.json();
    })
    .then(ufoSightings => {
      const map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 35.78613674, lng: -119.4491591 },
        zoom: 7
      });

      ufoSightings.forEach(ufoSighting => {
        new google.maps.Marker({
          position: { lat: ufoSighting.lat, lng: ufoSighting.lng },
          map: map
        });
      });
    });
}
