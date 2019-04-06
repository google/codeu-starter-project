let map;
function createMap(user_lat, user_lng, user_title) {
  // create map object and place in map element
  map = new google.maps.Map(document.getElementById("map"), {
    center: { lat: user_lat, lng: user_lng },
    zoom: 16
  });

  // add marker
  const item_marker = new google.maps.Marker({
    position: { lat: user_lat, lng: user_lng },
    map: map,
    title: user_title
  });
}

function buildUI() {
  // placeholder for marker title
  var title = "";
  // fill form
  fetch("/item-data")
    .then(function(response) {
      return response.json();
    })
    .then(item => {
      const descriptionContainer = document.getElementById("description");
      descriptionContainer.innerHTML = item.description;
      const headerContainer = document.getElementById("item-header");
      headerContainer.innerHTML = item.title;
      title = item.title;
      document.title = item.title + " - $" + item.price;
      const profile_url = "/profile" + "?user=" + item.email;
      return fetch(profile_url);
    })
    .then(function(response) {
      return response.json();
    })
    .then(item => {
      createMap(item.latitude, item.longitude, title);
    });
}
