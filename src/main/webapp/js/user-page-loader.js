/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById('page-title').innerText = parameterUsername;
  document.title = parameterUsername + ' - User Page';
}

/*
This function is called everytime the user types something in the recipient-form 
text area. It retrieves a list from dataServlet that is all users similar to the one 
being typed. Makes up to 5 divs with the possible emails inside them to aid users
into seeing potential recipients.
*/
function showUsers(){
  children = document.getElementById("message-form").children;
  if (children.length == 7){
    document.getElementById("message-form").removeChild(children[6]);    
  }

  const contents = document.getElementById("recipient-input");
  email = contents.value;
  const users = document.getElementById("possible-users");
  //this makes it so that if there is an empty textarea there arent 
  //boxes everywhere
  if (email != ''){
    users.classList.remove("hidden");
  } else {
    users.classList.add("hidden");
  }
  const url = '/users?email=' + email;
  fetch(url)
    .then((response) => {
      return response.json();
    })
    .then((users) => {
      i = 0;
      check = false;
      users.forEach((user) => {
        //limits the maximum number of users show to three
        if (i == 5){
          return;
        }
        const emailContainer = document.getElementById("possible-users");
        divList = emailContainer.children;
        check = cleanEmailContainer(divList, user, emailContainer);
        //if there isnt already a div created with this user then make one
        if (!check){
          createUserDiv(emailContainer, user);
          i += 1;
        }
      });
    });
}

/*
cleans up the possible users so that if the user types something that makes one of
the options irellevant, it will get rid of it. Returns true if user is already in
one of the newly created divs and false otherwise. 
*/
function cleanEmailContainer(divList, user, emailContainer){
  check = false;
  j = 0
  //iterates over all divs in "possible-users"
  while (j < divList.length){
    //if user is in possible users divs alreedy
    if(user.email == divList[j].innerText){
      check = true;
    }
    userIn = divList[j].innerText;
    //if the email in one of the divs is not relevant anymore
    if (!userIn.includes(email)){
      emailContainer.removeChild(divList[j]);
      //dont want to increment so effectively makes it j+=0;
      j--;
    }
    j++;
  }
  return check;
}
/*
creates a new div that will contain one possible full email similar to one that the user is typing 
*/
function createUserDiv(emailContainer, user){
  const innerDiv = document.createElement("div");
  innerDiv.classList.add('users');
  innerDiv.innerHTML = "<p>" + user.email  + "</p>";
  //if the user clicks one of the email auto fills it and hides the possible-users divs.
  innerDiv.onclick = function(){
    contents = document.getElementById("recipient-input");
    contents.value = user.email;
    document.getElementById("possible-users").classList.add("hidden");
  }
  emailContainer.appendChild(innerDiv);
}

/*
generates an error if the user tries to type in a message before specifying a user.
*/
function generateError(){
  const messageForm = document.getElementById("message-form");
  if (messageForm.children.length != 7){
    const innerDiv = document.createElement("div");
    innerDiv.classList.add("error");
    innerDiv.innerHTML = "<p> Enter an email address to send the message to </p>";
    messageForm.appendChild(innerDiv);
  }
}

/**
 * Shows the message form if the user is logged in and viewing their own page.
 */
function showMessageFormIfLoggedIn() {
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        if (loginStatus.isLoggedIn &&
          loginStatus.username == parameterUsername) {
          document.getElementById('about-me-form').classList.remove('hidden');
        }
      });
}

function fetchImageUploadUrlAndShowForm() {
  const contents = document.getElementById("recipient-input");
  email = contents.value;
  if (email == ""){
    generateError();
    return;
  } 
  fetch('/image-upload-url?recipient=' + email).then((response) => {
        return response.text();
      })
      .then((imageUploadUrl) => {
        const messageForm = document.getElementById('message-form');
        messageForm.action = imageUploadUrl;
      });
}

/** Fetches messages and add them to the page. */
function fetchMessages() {
  const parameterLanguage = urlParams.get('language');
  let url = '/messages?user=' + parameterUsername;
  if(parameterLanguage) {
    url += '&language=' + parameterLanguage;
  }
  //const url = '/messages?user=' + parameterUsername;
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((messages) => {
        const messagesContainer = document.getElementById('message-container');
        if (messages.length == 0) {
          messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
        } else {
          messagesContainer.innerHTML = '';
        }
        messages.forEach((message) => {
          const messageDiv = buildMessageDiv(message);
          messagesContainer.appendChild(messageDiv);
        });
      });
  
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(document.createTextNode(
    message.user + ' - ' +
    new Date(message.timestamp) + 
    ' [' + message.sentimentScore + ']'));
  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;
  if(message.imageUrl != ""){
    bodyDiv.innerHTML += '<br/>';
    bodyDiv.innerHTML += '<img src="' + message.imageUrl + '" />';
  }
  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}
function buildLanguageLinks(){
  const userPageUrl = '/user-page.html?user=' + parameterUsername;
  const languagesListElement  = document.getElementById('languages');
  languagesListElement.appendChild(createListItem(createLink(
       userPageUrl + '&language=en', 'English')));
  languagesListElement.appendChild(createListItem(createLink(
      userPageUrl + '&language=zh', 'Chinese')));
  languagesListElement.appendChild(createListItem(createLink(
      userPageUrl + '&language=hi', 'Hindi')));
  languagesListElement.appendChild(createListItem(createLink(
      userPageUrl + '&language=es', 'Spanish')));
  languagesListElement.appendChild(createListItem(createLink(
      userPageUrl + '&language=ar', 'Arabic')));
}

function fetchAboutMe(){
  const url = '/about?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
  }).then((aboutMe) => {
    const aboutMeContainer = document.getElementById('about-me-container');
    if(aboutMe == ''){
      aboutMe = 'This user has not entered any information yet.';
    }
    
    aboutMeContainer.innerHTML = aboutMe;

  });
}
/** Fetches data and populates the UI of the page. */
function buildUI() {
  setPageTitle();
  showMessageFormIfLoggedIn();
  fetchMessages();
  fetchAboutMe();
}