// Path to the CSV file (update the path if needed)
const csvFilePath = "merged_final_files.csv"; // Ensure this file is in the same directory or provide the correct path

let frequencyMap = {};
let stringFrequencyMap = {}; // Added to track frequency of full text strings

// Function to load the CSV file and process it
function loadCSVFile() {
  fetch(csvFilePath) // Load the CSV file from the given path
    .then((response) => response.text()) // Fetch the CSV data as text
    .then((csvData) => {
      frequencyMap = {}; // Reset the word frequency map
      stringFrequencyMap = {}; // Reset the string frequency map
      parseCSV(csvData); // Parse the CSV data and build the frequency maps
      alert("CSV file successfully loaded and parsed.");
    })
    .catch((error) => {
      console.error("Error loading CSV file:", error);
      alert("Error loading CSV file.");
    });
}

// Parse the CSV data and build the frequency maps
function parseCSV(csvData) {
  console.log("Parsing CSV data..."); // Debugging log
  const rows = csvData.split("\n"); // Split the CSV data by rows

  rows.forEach((row) => {
    const columns = row.split(","); // Split each row by commas
    const textContent = columns[1]; // Assuming that the content is in the second column (index 1)

    if (textContent) {
      // Count the frequency of the full string (textContent)
      stringFrequencyMap[textContent.trim().toLowerCase()] =
        (stringFrequencyMap[textContent.trim().toLowerCase()] || 0) + 1;

      // Split the text into words based on spaces for word frequency
      const words = textContent.split(/\s+/);
      words.forEach((word) => {
        word = word.trim().toLowerCase(); // Trim whitespace and convert to lowercase
        if (word) {
          frequencyMap[word] = (frequencyMap[word] || 0) + 1; // Increment the word count
        }
      });
    }
  });
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

  const wordFrequency = frequencyMap[wordOrText] || 0; // Word frequency lookup
  const textFrequency = stringFrequencyMap[wordOrText] || 0; // Full text string frequency lookup

  const resultContainer = document.getElementById("frequency-result");

  if (wordFrequency > 0) {
    resultContainer.innerHTML = `Word "${wordOrText}" found ${wordFrequency} time(s) in the CSV.`;
  } else if (textFrequency > 0) {
    resultContainer.innerHTML = `Full text string "${wordOrText}" found ${textFrequency} time(s) in the CSV.`;
  } else {
    resultContainer.innerHTML = `"${wordOrText}" not found in the CSV file.`;
  }
}

// Load the CSV file on page load
window.onload = () => {
  loadCSVFile(); // Load the CSV file when the page loads
};
