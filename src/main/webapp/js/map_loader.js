let map;
function createMap(){
	<!--create map object and place in map element-->
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: 37.422, lng: -122.084},
		zoom: 16
	});

	<!--add T-rex marker-->
	const trexMarker = new google.maps.Marker({
		position: {lat: 37.421903, lng: -122.084674},
		map: map,
		title: 'Stan the T-Rex'
	});
	var trexInfoWindow = new google.maps.InfoWindow({
		content: 'This is Stan, the T-Rex statue.'});
	trexInfoWindow.open(map, trexMarker);

	<!--add click event for marker-->
	trexMarker.addListener('click', function() {
		trexInfoWindow.open(map, trexMarker);
	}); 

}