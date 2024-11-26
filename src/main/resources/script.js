// Function to toggle the explore popup visibility
function toggleExplorePopup() {
  const popup = document.getElementById("explorePopup");

  // Toggle display style
  if (popup.style.display === "none" || popup.style.display === "") {
    popup.style.display = "block";
  } else {
    popup.style.display = "none";
  }
}

// Function to fetch and render plans based on the selected platform
function renderPlans(platform) {
  const container = document.getElementById("content");
  container.innerHTML = "<p>Loading plans...</p>"; // Clear current content and show loading

  // Highlight active tab
  const tabs = document.querySelectorAll(".tab");
  tabs.forEach((tab) => tab.classList.remove("active"));
  const activeTab = document.querySelector(`.tab[onclick*="${platform}"]`);
  if (activeTab) {
    activeTab.classList.add("active");
  }

  // Log the platform being requested
  console.log(`Fetching plans for platform: ${platform}`);

  // Fetch the plans from the backend API filtered by the selected platform
  fetch(`http://localhost:8080/api/plans?platform=${platform}`)
    .then((response) => {
      // Check if the response is successful
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
    })
    .then((data) => {
      if (data.length === 0) {
        container.innerHTML = `<p>No plans available for ${platform}. Please try another service.</p>`;
      } else {
        // Clear loading message and render plans
        container.innerHTML = data
          .slice(0, 6) // Display up to 6 plans
          .map(
            (plan) => `
                        <div class="plan-box">
                            <h2>${plan.planName}</h2>
                            <p><strong>Service:</strong> ${plan.serviceName}</p>
                            <p><strong>Price:</strong> $${plan.price}</p>
                            <p><strong>Annual Price:</strong> ${plan.annualPrice}</p>
                            <p><strong>Features:</strong> ${plan.features}</p>
                            <p><strong>Simultaneous Streams:</strong> ${plan.simultaneousStream}</p>
                            <p><strong>Download:</strong> ${plan.download}</p>
                            <p><strong>Ad-Free Streaming:</strong> ${plan.adFreeStreaming}</p>
                        </div>
                    `
          )
          .join("");
      }
    })
    .catch((error) => {
      console.error("Error fetching data:", error);
      container.innerHTML =
        "<p>Error fetching data. Please try again later.</p>";
    });
}

// Function to fetch the best (lowest-priced) plans
function renderBestPlans() {
  const container = document.getElementById("content");
  container.innerHTML = "<p>Loading best plans...</p>"; // Show loading message

  // Fetch the best plans from the backend
  fetch("http://localhost:8080/api/best-plans")
    .then((response) => {
      // Check if the response is successful
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
    })
    .then((data) => {
      if (data.length === 0) {
        container.innerHTML = `<p>No best plans available.</p>`;
      } else {
        // Clear loading message and render the best plans
        container.innerHTML = data
          .map(
            (plan) => `
                    <div class="plan-box">
                        <h2>${plan.planName}</h2>
                        <p><strong>Service:</strong> ${plan.serviceName}</p>
                        <p><strong>Price:</strong> $${plan.price}</p>
                        <p><strong>Annual Price:</strong> ${plan.annualPrice}</p>
                        <p><strong>Features:</strong> ${plan.features}</p>
                        <p><strong>Simultaneous Streams:</strong> ${plan.simultaneousStream}</p>
                        <p><strong>Download:</strong> ${plan.download}</p>
                        <p><strong>Ad-Free Streaming:</strong> ${plan.adFreeStreaming}</p>
                    </div>
                `
          )
          .join("");
      }
    })
    .catch((error) => {
      console.error("Error fetching data:", error);
      container.innerHTML =
        "<p>Error fetching data. Please try again later.</p>";
    });
}

// Add event listener for "Best Plan" click in explore popup
document
  .querySelector(".explore-popup ul")
  .addEventListener("click", function (event) {
    if (
      event.target &&
      event.target.tagName === "LI" &&
      event.target.textContent === "Best Plan"
    ) {
      renderBestPlans(); // Call function to render the best plans
    }
  });

// Function to handle search and fetch results based on the search term
function handleSearch(event) {
  // Check if Enter key was pressed or if the search button was clicked
  if (event.key === "Enter" || event.type === "click") {
    const searchInput = document.querySelector(".search-bar");
    const searchTerm = searchInput.value.trim();

    if (searchTerm === "") {
      alert("Please enter a search term!");
      return;
    }

    const container = document.getElementById("content");
    container.innerHTML = "<p>Searching...</p>"; // Show loading message

    // Fetch search results from the backend
    fetch(
      `http://localhost:8080/api/search?query=${encodeURIComponent(searchTerm)}`
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        if (data.length === 0) {
          container.innerHTML = `<p>No results found for "${searchTerm}". Please try again.</p>`;
        } else {
          container.innerHTML = data
            .map(
              (plan) => `
                      <div class="plan-box">
                          <h2>${plan.planName}</h2>
                          <p><strong>Service:</strong> ${plan.serviceName}</p>
                          <p><strong>Price:</strong> $${plan.price}</p>
                          <p><strong>Annual Price:</strong> ${plan.annualPrice}</p>
                          <p><strong>Features:</strong> ${plan.features}</p>
                          <p><strong>Simultaneous Streams:</strong> ${plan.simultaneousStream}</p>
                          <p><strong>Download:</strong> ${plan.download}</p>
                          <p><strong>Ad-Free Streaming:</strong> ${plan.adFreeStreaming}</p>
                      </div>
                  `
            )
            .join("");
        }
      })
      .catch((error) => {
        console.error("Error fetching search results:", error);
        container.innerHTML =
          "<p>Error fetching search results. Please try again later.</p>";
      });
  }
}

// Attach event listeners to search bar and search button
const searchBar = document.querySelector(".search-bar");
const searchButton = document.querySelector(".search-button");

if (searchBar) {
  searchBar.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
      const query = event.target.value.trim();
      if (query) fetchSearchResults(query);
    }
  });
}

if (searchButton) {
  searchButton.addEventListener("click", () => {
    const query = searchBar.value.trim();
    if (query) fetchSearchResults(query);
  });
}

// Function to fetch search results based on user input
function fetchSearchResults(query) {
  const container = document.getElementById("content");
  container.innerHTML = "<p>Loading search results...</p>"; // Clear current content and show loading message

  fetch(`http://localhost:8080/api/search?query=${query}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
    })
    .then((data) => {
      if (data.length === 0) {
        container.innerHTML = `<p>No plans found for "${query}".</p>`;
      } else {
        container.innerHTML = data
          .map(
            (plan) => `
              <div class="plan-box">
                  <h2>${plan.planName}</h2>
                  <p><strong>Service:</strong> ${plan.serviceName}</p>
                  <p><strong>Price:</strong> $${plan.price}</p>
                  <p><strong>Annual Price:</strong> ${plan.annualPrice}</p>
                  <p><strong>Features:</strong> ${plan.features}</p>
                  <p><strong>Simultaneous Streams:</strong> ${plan.simultaneousStream}</p>
                  <p><strong>Download:</strong> ${plan.download}</p>
                  <p><strong>Ad-Free Streaming:</strong> ${plan.adFreeStreaming}</p>
              </div>
          `
          )
          .join("");
      }
    })
    .catch((error) => {
      console.error("Error fetching search results:", error);
      container.innerHTML =
        "<p>Error fetching search results. Please try again later.</p>";
    });

  function handleAutocomplete(event) {
    const query = event.target.value;
    const suggestionsDiv = document.getElementById("suggestions");

    if (query.length < 2) {
      suggestionsDiv.style.display = "none";
      return;
    }

    fetch(`http://localhost:8080/api/autocomplete?query=${query}`)
      .then((response) => response.json())
      .then((data) => {
        if (data.length > 0) {
          suggestionsDiv.innerHTML = data
            .map(
              (item) =>
                `<div onclick="selectSuggestion('${item}')">${item}</div>`
            )
            .join("");
          suggestionsDiv.style.display = "block";
        } else {
          suggestionsDiv.innerHTML = "<div>No suggestions found</div>";
        }
      })
      .catch(() => {
        suggestionsDiv.innerHTML = "<div>Error fetching suggestions</div>";
      });
  }

  function selectSuggestion(suggestion) {
    document.getElementById("searchBar").value = suggestion;
    document.getElementById("suggestions").style.display = "none";

    // Optionally, trigger a search
    renderPlans(suggestion);
  }

  function redirectToPageRanking() {
    // Redirect to PageRanking.html
    window.location.href = "PageRank.html";
  }

  function toggleExplorePopup() {
    const popup = document.getElementById("explorePopup");
    popup.style.display = popup.style.display === "block" ? "none" : "block";
  }

  // Function to navigate to the Web Crawler page
  function navigateToWebCrawler() {
    // Redirect to the webcrawler.html page
    window.location.href = "WebCrawler.html";
  }

  function navigateToInvertedIndex() {
    window.location.href = "invertedIndex.html";
  }

  function redirectToFrequencyCount() {
    window.location.href = "FrequencyCount.html";
  }

  function redirectToTextExtractor(){
    window.location.href = "TextExtractor.html"
}
}
