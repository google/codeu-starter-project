function fetchLogoUploadUrlAndShowForm() {
  fetch('/logo-upload-url')
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
}
