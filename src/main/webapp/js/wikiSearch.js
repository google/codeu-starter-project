function searchOnWiki(){
  fetch('/logo-detect').then(function(response) {
    return response.json();
  }).then((logos) => {
    const query = logos[0];
    const url = 'https://en.wikipedia.org/w/api.php?action=opensearch&search=' +
        query + '&limit=1&namespace=0&format=json&callback=handleSummary';

    var scpt = document.createElement("script");
    scpt.src = url;
    document.body.appendChild(scpt);
  })
}

function handleSummary(summary){
  var name = summary[1][0];
  var description = summary[2][0];
  var link = summary[3][0];
  displayLogoInfo(name, description, link);
}

function displayLogoInfo(name, description, link){
  const nameDiv = document.getElementById('logo-name');
  nameDiv.appendChild(document.createTextNode(name));

  const descriptionDiv = document.getElementById('logo-description');
  descriptionDiv.appendChild(document.createTextNode(description));

  const linkDiv = document.getElementById('logo-link');
  linkDiv.appendChild(document.createTextNode(link));
}

function infoNotFound(){
  const nameDiv = document.getElementById('logo-name');
  nameDiv.appendChild(document.createTextNode('Logo information not found'));
}

function buildInfoUI(){
  searchOnWiki();
}