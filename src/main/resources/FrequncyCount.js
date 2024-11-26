// Path to the CSV file (update the path if needed)
const csvFilePath = "Crawled_Website_Data.csv"; // Ensure this file is in the same directory or provide the correct path

let searchFrequencyMap = {}; // Tracks frequency of user search queries
let frequencyMap = {}; // Tracks word frequency from CSV
let rows = []; // Stores CSV data for KMP processing

// Function to load the CSV file and process it
function loadCSVFile() {
  fetch(csvFilePath) // Load the CSV file from the given path
    .then((response) => response.text()) // Fetch the CSV data as text
    .then((csvData) => {
      searchFrequencyMap = {}; // Reset the search frequency map
      frequencyMap = {}; // Reset frequency map
      parseCSV(csvData); // Parse the CSV data into rows
      alert("CSV file successfully loaded and parsed.");
    })
    .catch((error) => {
      console.error("Error loading CSV file:", error);
      alert("Error loading CSV file.");
    });
}

// Parse the CSV data and store rows for processing
function parseCSV(csvData) {
  console.log("Parsing CSV data..."); // Debugging log
  rows = csvData.split("\n").map((row) => row.split(",")); // Split CSV data into rows and columns

  // Process the CSV to count word frequency
  rows.forEach((row) => {
    const textContent = row[1]; // Assuming content is in the second column (index 1)
    if (textContent) {
      const words = textContent.split(/\s+/);
      words.forEach((word) => {
        word = word.trim().toLowerCase(); // Normalize the word
        if (word) {
          frequencyMap[word] = (frequencyMap[word] || 0) + 1; // Increment word count
        }
      });
    }
  });
}

// Levenshtein Distance (Edit Distance) to calculate similarity
function levenshtein(a, b) {
  const tmp = [];
  for (let i = 0; i <= a.length; i++) {
    tmp[i] = [i];
  }
  for (let j = 0; j <= b.length; j++) {
    tmp[0][j] = j;
  }
  for (let i = 1; i <= a.length; i++) {
    for (let j = 1; j <= b.length; j++) {
      tmp[i][j] = Math.min(
        tmp[i - 1][j - 1] + (a[i - 1] !== b[j - 1]),
        tmp[i][j - 1] + 1,
        tmp[i - 1][j] + 1
      );
    }
  }
  return tmp[a.length][b.length];
}

// Suggest a word based on the closest match using Levenshtein distance
function suggestWord(inputWord) {
  let suggestions = [];
  const maxDistance = 2; // Maximum distance for suggestion
  Object.keys(frequencyMap).forEach((word) => {
    const distance = levenshtein(inputWord, word);
    if (distance <= maxDistance) {
      suggestions.push({ word: word, distance: distance });
    }
  });

  // Sort suggestions by distance (closest first)
  suggestions.sort((a, b) => a.distance - b.distance);
  return suggestions;
}

// Check the frequency of a word or full text string entered by the user
function checkFrequency() {
  const wordOrText = document
    .getElementById("word-input")
    .value.trim()
    .toLowerCase();

  if (!wordOrText) {
    alert("Please enter a word or text.");
    return;
  }

  // Increment the search frequency for the queried term
  searchFrequencyMap[wordOrText] = (searchFrequencyMap[wordOrText] || 0) + 1;

  let totalOccurrences = 0;

  // Iterate over rows of CSV data and use KMP to search each row
  rows.forEach((row) => {
    const textContent = row[1]; // Assuming content is in the second column (index 1)
    if (textContent) {
      const words = textContent.split(/\s+/);
      words.forEach((word) => {
        if (word.toLowerCase() === wordOrText) {
          totalOccurrences++;
        }
      });
    }
  });

  const resultContainer = document.getElementById("frequency-result");

  if (totalOccurrences > 0) {
    resultContainer.innerHTML = `The phrase "${wordOrText}" was found ${totalOccurrences} time(s) in the CSV.`;
  } else {
    resultContainer.innerHTML = `"${wordOrText}" was not found in the CSV file.`;
    // Check if it's a misspelling and suggest corrections
    const suggestions = suggestWord(wordOrText);
    if (suggestions.length > 0) {
      resultContainer.innerHTML += `<br><strong>Did you mean:</strong><br>`;
      suggestions.forEach((suggestion) => {
        resultContainer.innerHTML += `${suggestion.word} (Distance: ${suggestion.distance})<br>`;
      });
    } else {
      resultContainer.innerHTML += "<br>No suggestions available.";
    }
  }
}

// Display the search frequency data
function showSearchFrequencies() {
  const resultContainer = document.getElementById("frequency-result");
  if (Object.keys(searchFrequencyMap).length === 0) {
    resultContainer.innerHTML = "<p>No searches have been performed yet.</p>";
    return;
  }

  let frequencyHTML = "<h3>Search Frequencies:</h3><ul>";
  for (const [term, count] of Object.entries(searchFrequencyMap)) {
    frequencyHTML += `<li>"${term}" searched ${count} time(s)</li>`;
  }
  frequencyHTML += "</ul>";
  resultContainer.innerHTML = frequencyHTML;
}

// Load the CSV file on page load
window.onload = () => {
  loadCSVFile(); // Load the CSV file when the page loads
};
