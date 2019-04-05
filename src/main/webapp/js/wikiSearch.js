function searchOnWiki(){
  fetch('/logo-detect').then(function(response) {
    return response.json();
  }).then((logos) => {
    const query = logos[0];
    var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
    var xhr = new XMLHttpRequest();
    const url = 'https://en.wikipedia.org/w/api.php?action=opensearch&search=' +
        query + '&limit=1&namespace=0&format=json';

    xhr.open('GET', url);
    xhr.send();

    xhr.onreadystatechange = function () {
      if(xhr.readyState === 4 && xhr.status === 200) {
        var arr = JSON.parse(xhr.responseText);
        displayLogoInfo(
            arr[1][0],
            arr[2][0],
            arr[3][0],
            )
      }
      else { infoNotFound(); }
    };
  })
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