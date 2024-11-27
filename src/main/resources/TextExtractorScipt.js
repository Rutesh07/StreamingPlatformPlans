const BACKEND_URL = "http://localhost:8080/TextExtractorController";

// Wait for the DOM to be fully loaded before running the script
document.addEventListener("DOMContentLoaded", () => {
  // Fix the form ID to match the HTML
  const form = document.getElementById("extractor-form");

  // Add event listener for form submission
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const domain = document.getElementById("domain").value; // Get the domain value
    const file = document.getElementById("file").files[0]; // Get the selected file

    const formData = new FormData();
    formData.append("domain", domain);
    formData.append("file", file);

    try {
      // Async function fetch
      const response = await fetch(`${BACKEND_URL}/extract`, {
        method: "POST",
        body: formData, // Send form data directly
      });

      const output = document.getElementById("output");

      if (response.ok) {
        const data = await response.text();
        // Render extracted text into a structured format
        output.innerHTML = data
          .split("\n")
          .map((line) => `<div>${line}</div>`)
          .join("");
      } else {
        output.innerText = "Failed to extract text.";
      }
    } catch (error) {
      document.getElementById("output").innerText =
        "Error in submitting the form.";
      console.error("Error submitting the form:", error);
    }
  });
});
