// Fetches about me section for all users and adds them to page
function fetchBioForAllMatches(){
  const url = '/matches';
  fetch(url).then(response => {
    return response.json();
  }).then(allBios => {
    const bioContainer = document.getElementById('bio-container');
    if(allBios.length == 0){
     allBios.innerHTML = '<p>You have no matches</p>';
    }
    else{
     bioContainer.innerHTML = '';
    }
    allBios.forEach(bio => {
     const bioDiv = buildBioDiv(bio);
     bioContainer.appendChild(bioDiv);
    });
  });
}

/*
* Builds div for the given bio
* @param bio The bio which the div is being made for
*/
function buildBioDiv(bio){
 const usernameDiv = document.createElement('div');
 usernameDiv.classList.add("left-align");
 usernameDiv.appendChild(createLink('/user-page.html?user=' + bio.email, bio.email));

 const headerDiv = document.createElement('div');
 headerDiv.classList.add('message-header');
 headerDiv.appendChild(usernameDiv);

 const bodyDiv = document.createElement('div');
 bodyDiv.classList.add('bio-body');
 bodyDiv.appendChild(document.createTextNode(bio.aboutMe));

 const bioDiv = document.createElement('div');
 bioDiv.classList.add("bio-div");
 bioDiv.appendChild(headerDiv);
 bioDiv.appendChild(bodyDiv);

 return bioDiv;
}

// Fetch data and populate the UI of the page.
function buildUI(){
 fetchBioForAllMatches();
}

/**
* Creates an anchor element.
* @param {string} url
* @param {string} text
* @return {Element} Anchor element
*/
function createLink(url, text) {
const linkElement = document.createElement('a');
linkElement.appendChild(document.createTextNode(text));
linkElement.href = url;
return linkElement;
}
