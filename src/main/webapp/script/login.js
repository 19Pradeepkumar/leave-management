document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
            const response = await fetch('http://localhost:8092/leave-management/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });
            if (!response.ok) {
                throw new Error('Login failed');
            }

            const data = await response.json(); // Parse the JSON response
            console.log(data);

            if (data.message) {
                localStorage.setItem("isManager", true);
            } else {
                localStorage.setItem("isManager", false);
            }
               window.location.href = "/leave-management/index.html"
        } catch (error) {
            console.error('Error:', error);
            alert('Login failed: ' + error.message);
        }
});
