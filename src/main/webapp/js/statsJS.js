// Fetch stats and display them in the page.
function fetchStats() {
  const url = "/stats";
  fetch(url)
    .then(response => {
      return response.json();
    })
    .then(stats => {
      const statsContainer = document.getElementById("stats-container");
      statsContainer.innerHTML = "";

      const messageCountElement = buildStatElement(
        "Message Count: " + stats.messageCount
      );
      statsContainer.appendChild(messageCountElement);

      const longestMessageElement = buildStatElement(
        "Longest Message Length (Characters): " + stats.longestMessageLength
      );
      statsContainer.appendChild(longestMessageElement);

      const usersCountElement = buildStatElement(
        "Total Number of Users: " + stats.userCount
      );
      statsContainer.appendChild(usersCountElement);

      const topUsers = buildStatElement(
        "Top Users: " +
          (stats.topUser1 || "") +
          ", " +
          (stats.topUser2 || "") +
          ", " +
          (stats.topUser3 || "")
      );
      statsContainer.appendChild(topUsers);

      const messageCategories = buildStatElement(
        "Message Topics: " + stats.messageCategories
      );
      statsContainer.appendChild(messageCategories);
    });
}

function buildStatElement(statString) {
  const statElement = document.createElement("p");
  statElement.appendChild(document.createTextNode(statString));
  return statElement;
}

// Fetch data and populate the UI of the page.
function buildUI() {
  fetchStats();
}
