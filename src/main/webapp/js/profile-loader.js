//Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get("user");

//URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace("/");
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById("page-title").innerText = parameterUsername;
  document.title = parameterUsername + " - User Page";
}

function fetchProfile() {
  const url = "/profile?user=" + parameterUsername;
  fetch(url)
    .then(response => {
      return response.json();
    })
    .then(profile => {
      const profileContainer = document.getElementById("profile-container");

      fetchAndShowProfilePic();

      profileContainer.innerHTML = `Name: ${profile.name ||
        ""} Latitude: ${profile.latitude ||
        ""} Longitude:  ${profile.longitude || ""}  Phone: ${profile.phone ||
        ""} Schedule: ${profile.schedule || ""}`;
    });
}

function fetchAndShowProfilePic() {
	  fetch('/profile-pic-upload-url')
	      .then((response) => {
	        return response.text();
	      })
	      .then((profileURL) => {
	        const profileForm = document.getElementById('profile-form');
	        console.log(profileURL);
	        profileForm.action = profileURL;
	      });
}

function buildUI() {
	fetchProfile();
}