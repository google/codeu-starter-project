function fetchProfileURL() {
	console.log("fetching url");
	  fetch('/profile-pic-upload-url')
	      .then((response) => {
	        return response.text();
	      })
	      .then((profileURL) => {
	        const profileForm = document.getElementById('profile-form');
	        profileForm.action = profileURL;
	      });
	console.log("fetched url")
}

function buildUI() {
	console.log("BUILDING UI PEOPLE!");
	fetchProfileURL(); 
}