function buildUI() {
  fetch("/item-data")
    .then(function(response) {
      return response.json();
    })
    .then(item => {
      const descriptionContainer = document.getElementById("description");
      descriptionContainer.innerHTML = item.description;
      const headerContainer = document.getElementById("item-header");
      headerContainer.innerHTML = item.title;
      const titleContainer = document.getElementById("title");
      titleContainer.innerHTML = item.title + item.price;
    });
}
