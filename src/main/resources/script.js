// Function to fetch and render plans based on the selected platform
function renderPlans(platform) {
    const container = document.getElementById("content");
    container.innerHTML = "<p>Loading plans...</p>"; // Clear current content and show loading

    // Highlight active tab
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => tab.classList.remove('active'));
    const activeTab = document.querySelector(`.tab[onclick*="${platform}"]`);
    if (activeTab) {
        activeTab.classList.add('active');
    }

    // Log the platform being requested
    console.log(`Fetching plans for platform: ${platform}`);

    // Fetch the plans from the backend API filtered by the selected platform
    fetch(`http://localhost:8080/api/plans?platform=${platform}`)
        .then(response => {
            // Check if the response is successful
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.length === 0) {
                container.innerHTML = `<p>No plans available for ${platform}. Please try another service.</p>`;
            } else {
                // Clear loading message and render plans
                container.innerHTML = data
                    .map(plan => `
                        <div class="plan-box">
                            <h2>${plan.planName}</h2>
                            <p><strong>Service:</strong> ${plan.serviceName}</p>
                            <p><strong>Price:</strong> $${plan.price}</p>
                            <p><strong>Features:</strong> ${plan.features}</p>
                        </div>
                    `)
                    .join("");
            }
        })
        .catch(error => {
            // Handle any errors that occur during the fetch
            console.error("Error fetching plans:", error);
            container.innerHTML = "<p>Failed to load plans. Please try again later.</p>";
        });
}

// Initialize with the plans for Disney+ Hotstar by default
document.addEventListener("DOMContentLoaded", () => {
    renderPlans("Disney");
});
