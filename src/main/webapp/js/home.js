var loadFile = function(event) {
  var image = document.getElementById('output');
  image.src = URL.createObjectURL(event.target.files[0]);
};

function fetchLogoUploadUrlAndShowForm() {
  fetch('/logo-detect')
      .then((response) => {
        return response.text();
      })
      .then((LogoUploadUrlServlet) => {
        const logo = document.getElementById('logo-form');
        logo.action = LogoUploadUrlServlet;
      });
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
  fetchLogoUploadUrlAndShowForm();
  loadFile(event);
}
