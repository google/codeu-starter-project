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
console.log(typeof(window.location.href));
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
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
        //this is going to change to make sure you are in that hall
        if (loginStatus.isLoggedIn) {
          fetchImageUploadUrlAndShowForm();
        }
      });
}

function fetchImageUploadUrlAndShowForm() {
  fetch('/image-upload-url?recipient=' + parameterUsername + "&redirect=" + window.location.href).then((response) => {
        return response.text();
      })
      .then((imageUploadUrl) => {
        const messageForm = document.getElementById('message-form');
        messageForm.action = imageUploadUrl;
      });
}

/** Fetches messages and add them to the page. */
function fetchMessages() {
  // const parameterLanguage = urlParams.get('language');
  // let url = '/messages?user=' + parameterUsername;
  // if(parameterLanguage) {
  //   url += '&language=' + parameterLanguage;
  // }
  url = '/messages?user=' + parameterUsername;
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

/** Fetches data and populates the UI of the page. */
function buildUI() {
  showMessageFormIfLoggedIn();
  fetchMessages();
}