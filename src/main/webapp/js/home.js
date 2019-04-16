function fetchLogoUploadUrlAndShowForm() {
  fetch('/logo-upload-url')
      .then((response) => {
        return response.text();
      })
      .then((LogoUploadUrl) => {
        const logo = document.getElementById('logo-form');
        logo.action = LogoUploadUrl;
      });
}

var loadFile = function(event) {
	var image = document.getElementById('output');
	image.src = URL.createObjectURL(event.target.files[0]);
};

function displayImage() {
  document.querySelector('input[type="file"]').addEventListener('change', function() {
    if (this.files && this.files[0]) {
      var img = document.querySelector('img');
      img.src = URL.createObjectURL(this.files[0]);
      //img.onload = imageIsLoaded;
    }
  });
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
  fetchLogoUploadUrlAndShowForm();
  displayImage();
}