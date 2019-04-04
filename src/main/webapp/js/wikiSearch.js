function searchKeyword(keyword){
  var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
  var xhr = new XMLHttpRequest();
  const url = 'https://en.wikipedia.org/w/api.php?action=opensearch&search=' + keyword + '&limit=1&namespace=0&format=json';

  xhr.open('GET', url);
  xhr.send();

  xhr.onreadystatechange = function () {
    if(xhr.readyState === 4 && xhr.status === 200) {
      return xhr.responseText;
    }
    else {return null;}
  };
}