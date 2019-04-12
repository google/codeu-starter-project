function fetchProfileURL() {
  fetch("/profile-pic-upload-url")
    .then(response => {
      return response.text();
    })
    .then(profileURL => {
      const profileForm = document.getElementById("profile-form");
      profileForm.action = profileURL;
    });
}

function buildUI() {
  console.log("BUILDING UI PEOPLE!");
  fetchProfileURL();
}
