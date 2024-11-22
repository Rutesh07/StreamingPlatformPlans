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
                container.innerHTML = data.slice(0, 6) // Display up to 6 plans
                    .map(plan => `
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
                    `).join('');
            }
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            container.innerHTML = "<p>Error fetching data. Please try again later.</p>";
        });
}

// Function to fetch the best (lowest-priced) plans
function renderBestPlans() {
    const container = document.getElementById("content");
    container.innerHTML = "<p>Loading best plans...</p>"; // Show loading message

    // Fetch the best plans from the backend
    fetch('http://localhost:8080/api/best-plans')
        .then(response => {
            // Check if the response is successful
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.length === 0) {
                container.innerHTML = `<p>No best plans available.</p>`;
            } else {
                // Clear loading message and render the best plans
                container.innerHTML = data.map(plan => `
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
                `).join('');
            }
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            container.innerHTML = "<p>Error fetching data. Please try again later.</p>";
        });
}

// Add event listener for "Best Plan" click in explore popup
document.querySelector('.explore-popup ul').addEventListener('click', function(event) {
    if (event.target && event.target.tagName === "LI" && event.target.textContent === "Best Plan") {
        renderBestPlans();  // Call function to render the best plans
    }
});
