// Path to the CSV file (update the path if needed)
const csvFilePath = "Crawled_Website_Data.csv";

// AVL Tree Implementation for Word Frequency Count
class AVLNode {
  constructor(key) {
    this.key = key; // Word
    this.frequency = 1; // Count of occurrences
    this.height = 1; // Height of the node
    this.left = null; // Left child
    this.right = null; // Right child
  }
}

class AVLTree {
  constructor() {
    this.root = null;
  }

  getHeight(node) {
    return node ? node.height : 0;
  }

  getBalanceFactor(node) {
    return node ? this.getHeight(node.left) - this.getHeight(node.right) : 0;
  }

  rightRotate(y) {
    const x = y.left;
    const T2 = x.right;

    x.right = y;
    y.left = T2;

    y.height = Math.max(this.getHeight(y.left), this.getHeight(y.right)) + 1;
    x.height = Math.max(this.getHeight(x.left), this.getHeight(x.right)) + 1;

    return x;
  }

  leftRotate(x) {
    const y = x.right;
    const T2 = y.left;

    y.left = x;
    x.right = T2;

    x.height = Math.max(this.getHeight(x.left), this.getHeight(x.right)) + 1;
    y.height = Math.max(this.getHeight(y.left), this.getHeight(y.right)) + 1;

    return y;
  }

  insert(node, key) {
    if (node === null) {
      return new AVLNode(key);
    }

    if (key < node.key) {
      node.left = this.insert(node.left, key);
    } else if (key > node.key) {
      node.right = this.insert(node.right, key);
    } else {
      node.frequency++;
      return node;
    }

    node.height =
      1 + Math.max(this.getHeight(node.left), this.getHeight(node.right));

    const balance = this.getBalanceFactor(node);

    if (balance > 1 && key < node.left.key) {
      return this.rightRotate(node);
    }

    if (balance < -1 && key > node.right.key) {
      return this.leftRotate(node);
    }

    if (balance > 1 && key > node.left.key) {
      node.left = this.leftRotate(node.left);
      return this.rightRotate(node);
    }

    if (balance < -1 && key < node.right.key) {
      node.right = this.rightRotate(node.right);
      return this.leftRotate(node);
    }

    return node;
  }

  getFrequency(node, key) {
    if (node === null) {
      return 0;
    }

    if (key < node.key) {
      return this.getFrequency(node.left, key);
    } else if (key > node.key) {
      return this.getFrequency(node.right, key);
    } else {
      return node.frequency;
    }
  }

  insertWord(key) {
    this.root = this.insert(this.root, key);
  }

  findFrequency(key) {
    return this.getFrequency(this.root, key);
  }

  findAllWords(node, callback) {
    if (node === null) return;
    this.findAllWords(node.left, callback);
    callback(node.key); // Pass the word to the callback
    this.findAllWords(node.right, callback);
  }

  getAllWords(callback) {
    this.findAllWords(this.root, callback);
  }
}

// Initialize Data Structures
let searchFrequencyMap = new Map(); // Tracks frequency of user search queries
let rows = []; // Stores CSV data for KMP processing
const wordFrequencyAVL = new AVLTree(); // AVL Tree for Word Frequency Count

// Function to Load CSV and Process Data
function loadCSVFile() {
  fetch(csvFilePath)
    .then((response) => response.text())
    .then((csvData) => {
      searchFrequencyMap.clear(); // Reset Search Frequency Map
      parseCSV(csvData); // Parse CSV into rows
      alert("CSV file successfully loaded and processed.");
    })
    .catch((error) => {
      console.error("Error loading CSV file:", error);
      alert("Error loading CSV file.");
    });
}

function parseCSV(csvData) {
  rows = csvData.split("\n").map((row) => row.split(","));
  rows.forEach((row) => {
    const textContent = row[1]; // Assuming content is in the second column
    if (textContent) {
      const words = textContent.split(/\s+/);
      words.forEach((word) => {
        word = word.trim().toLowerCase();
        if (word) {
          wordFrequencyAVL.insertWord(word); // Insert into AVL Tree
        }
      });
    }
  });
}

// Frequency Count Function
function checkFrequency() {
  const word = document.getElementById("word-input").value.trim().toLowerCase();
  if (!word) {
    alert("Please enter a word.");
    return;
  }

  // Update the search frequency map
  if (searchFrequencyMap.has(word)) {
    searchFrequencyMap.set(word, searchFrequencyMap.get(word) + 1);
  } else {
    searchFrequencyMap.set(word, 1);
  }

  console.log("Searching for word:", word); // Debugging
  const frequency = wordFrequencyAVL.findFrequency(word); //check how many times word occure in CSV file

  const resultContainer = document.getElementById("frequency-result");
  if (frequency > 0) {
    resultContainer.innerHTML = `The word "${word}" was found ${frequency} time(s) in the CSV.`;
  } else {
    resultContainer.innerHTML = `"${word}" was not found in the CSV.`;
    console.log("Word not found, suggesting alternatives."); // Debugging

    const suggestions = suggestWord(word);
    console.log("Suggestions:", suggestions); // Debugging
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

// Display Search Frequencies
function showSearchFrequencies() {
  const resultContainer = document.getElementById("search-frequency-result");
  if (searchFrequencyMap.size > 0) {
    resultContainer.innerHTML = `<strong>Search Frequencies:</strong><br>`;
    searchFrequencyMap.forEach((frequency, word) => {
      resultContainer.innerHTML += `${word}: ${frequency}<br>`;
    });
  } else {
    resultContainer.innerHTML = "No searches have been performed yet.";
  }
}

function knuthMorrisPratt(text, pattern) {
  const lps = computeLPSArray(pattern);
  let i = 0,
    j = 0,
    occurrences = 0;

  while (i < text.length) {
    if (pattern[j] === text[i]) {
      i++;
      j++;
    }
    if (j === pattern.length) {
      occurrences++;
      j = lps[j - 1];
    } else if (i < text.length && pattern[j] !== text[i]) {
      j = j !== 0 ? lps[j - 1] : 0;
    }
  }

  return occurrences;
}

function computeLPSArray(pattern) {
  const lps = Array(pattern.length).fill(0);
  let length = 0,
    i = 1;

  while (i < pattern.length) {
    if (pattern[i] === pattern[length]) {
      lps[i++] = ++length;
    } else if (length) {
      length = lps[length - 1];
    } else {
      lps[i++] = 0;
    }
  }

  return lps;
}

// Spell Checker using Edit Distance
function levenshtein(a, b) {
  const dp = Array.from({ length: a.length + 1 }, () =>
    Array(b.length + 1).fill(0)
  );
  for (let i = 0; i <= a.length; i++) dp[i][0] = i;
  for (let j = 0; j <= b.length; j++) dp[0][j] = j;

  for (let i = 1; i <= a.length; i++) {
    for (let j = 1; j <= b.length; j++) {
      dp[i][j] =
        a[i - 1] === b[j - 1]
          ? dp[i - 1][j - 1]
          : 1 + Math.min(dp[i - 1][j - 1], dp[i - 1][j], dp[i][j - 1]);
    }
  }

  return dp[a.length][b.length];
}

// Suggest words based on Levenshtein distance
function suggestWord(inputWord) {
  const suggestions = [];
  wordFrequencyAVL.getAllWords((word) => {
    const distance = levenshtein(inputWord, word);
    if (distance <= 2) {
      // Suggest words with edit distance <= 2
      suggestions.push({ word, distance });
    }
  });

  suggestions.sort((a, b) => a.distance - b.distance); // Sort by closest match
  return suggestions;
}

// Event Listener for Word Frequency Check
document
  .getElementById("check-frequency-button")
  .addEventListener("click", checkFrequency);

// Event Listener for Show Search Frequency
document
  .getElementById("show-frequency-button")
  .addEventListener("click", showSearchFrequencies);

// Load CSV when the page loads
window.onload = loadCSVFile;
