window.searchInvertedIndex = function () {
  const word = document.getElementById("searchWord").value.trim();
  if (word) {
    fetch(
      "http://localhost:8080/api/inverted-index/search?word=" +
        encodeURIComponent(word)
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        console.log(data); // Debug log
        document.getElementById("output").innerText = JSON.stringify(
          data,
          null,
          2
        );
      })
      .catch((error) => {
        console.error("There was a problem with the fetch operation:", error);
        document.getElementById("output").innerText = "Error fetching data.";
      });
  } else {
    document.getElementById("output").innerText =
      "Please enter a word to search.";
  }
};
