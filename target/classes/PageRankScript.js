// Wait for the DOM to load
document.addEventListener("DOMContentLoaded", () => {
  // Handle search button click
  document.getElementById("search-btn").addEventListener("click", function () {
    const query = document.getElementById("search-input").value.trim(); // Get the input query
    if (!query) {
      alert("Please enter a search query!");
      return;
    }

    // Fetch results from the backend
    fetch(`http://localhost:8080/api/rank?query=${encodeURIComponent(query)}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Server error: ${response.status}`);
        }
        return response.json(); // Parse JSON response
      })
      .then((data) => {
        displayResults(data); // Call function to display the results
      })
      .catch((error) => {
        console.error("Error:", error);
        alert("Something went wrong. Please try again later.");
      });
  });
});

// Function to display the results in the DOM
function displayResults(data) {
  const resultsContainer = document.getElementById("results-container"); // Container to hold results
  resultsContainer.innerHTML = ""; // Clear previous results

  if (data.length === 0) {
    resultsContainer.innerHTML = "<p>No results found.</p>";
    return;
  }

  // Create a list of ranked pages
  const list = document.createElement("ul");
  list.className = "results-list"; // Add a class for styling

  data.forEach((page, index) => {
    const listItem = document.createElement("li");
    listItem.innerHTML = `
            <strong>Rank #${index + 1}</strong>: 
            <a href="${page.title}" target="_blank">${page.title}</a> 
            (Keyword Frequency: ${page.frequency})
        `;
    list.appendChild(listItem);
  });

  resultsContainer.appendChild(list);
}
