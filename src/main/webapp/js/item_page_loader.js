function buildUI() {
  fetch("/item-data")
    .then(function(response) {
      return response.json();
    })
    .then(item => {
      const descriptionContainer = document.getElementById("description");
      descriptionContainer.innerHTML = item.description;
      const titleContainer = document.getElementById("item-title");
      titleContainer.innerHTML = item.title;
      });
}
