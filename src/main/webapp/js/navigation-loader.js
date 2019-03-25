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

/**
 * Adds a login or logout link to the page, depending on whether the user is
 * already logged in.
 */
function addLoginOrLogoutLinkToNavigation() {
  const navigationElement = document.getElementById('navigation');
  if (!navigationElementExists) {
    return;
  }

  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        if (loginStatus.isLoggedIn) {
          navigationElement.appendChild(createListItem(createLink(
              '/user-page.html?user=' + loginStatus.username, 'Your Page')));

          navigationElement.appendChild(
              createListItem(createLink('/logout', 'Logout')));
        } else {
          navigationElement.appendChild(
              createListItem(createLink('/login', 'Login')));
        }
      });
}

/**
 * Adds a Public Feed link to the page
 */
function addPublicFeed() {
  const navigationElement = document.getElementById('navigation');
  if (!navigationElementExists) {
    return;
  }
  navigationElement.appendChild(createListItem(createLink('/feed.html', 'Public Feed')));
}

/**
* Adds link the matches feed for the current user
*/
function addMatchesFeed() {
  const navigationElement = document.getElementById('navigation');
  if (!navigationElementExists) {
    return;
  }
  navigationElement.appendChild(createListItem(createLink('/matches-feed.html', 'Matches Feed')));
}

/**
* Checks the given navigation element is not null
* Returns true if not null, otherwise outputs warning
* to console and returns false
* @param navigationElement What's being checked for nullity
* @return true if {Element} navigationElement,is not null
*         otherwise false and outputs warning to console
*/
function navigationElementExists(navigationElement) {
  if (!navigationElement) {
    console.warn('Navigation element not found!');
    return false;
  }
  return true;
}


function buildNavBar() {
	// Add login/logout link
	addLoginOrLogoutLinkToNavigation();
	// Add Public Feed link
	addPublicFeed();
  // Add matches feed link
  addMatchesFeed();
}

/**
 * Creates an li element.
 * @param {Element} childElement
 * @return {Element} li element
 */
function createListItem(childElement) {
  const listItemElement = document.createElement('li');
  listItemElement.appendChild(childElement);
  return listItemElement;
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
