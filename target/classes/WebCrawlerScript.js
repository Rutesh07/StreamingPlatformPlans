// Show the search bar when the button is clicked
document.getElementById("crawlButton").addEventListener("click", function () {
    document.getElementById("searchBar").style.display = "block";
  });
  
  // Function to call the backend API to crawl the website
  function crawlWebsite() {
    const url = document.getElementById("urlInput").value; // Get the URL input value (corrected ID)
    
    fetch(`http://localhost:8080/api/crawl?url=${encodeURIComponent(url)}`)
      .then((response) => response.json())
      .then((data) => {
        console.log(data); // Handle the response (the list of links)
  
        // Display the crawled links in the HTML
        const linksList = document.getElementById("linksList");
        linksList.innerHTML = ''; // Clear previous links
  
        data.forEach(link => {
          const listItem = document.createElement("li");
          listItem.textContent = link; // Add link to the list
          linksList.appendChild(listItem);
        });
      })
      .catch((error) => console.error("Error:", error));
  }
  