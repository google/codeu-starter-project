function getKeyword(url_string){
  var url = new URL(url_string);
  return url.searchParams.get("kw");
}

function searchOnWiki(keyword){
  if(keyword == 'error'){
    displayLogoInfo('', 'Error: Logo not found', '');
  }
  else{
    const url = 'https://en.wikipedia.org/w/api.php?action=opensearch&search=' +
        keyword + '&limit=1&namespace=0&format=json&callback=handleSummary';
    var scpt = document.createElement("script");
    scpt.src = url;
    document.body.appendChild(scpt);
  }
}

/**
 * The function that is called back by wikipedia api, with result stored as Json array.
 */
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

function buildInfoUI(){
  var keyword = getKeyword(window.location.href);
  searchOnWiki(keyword);
}