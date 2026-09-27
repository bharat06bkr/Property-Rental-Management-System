const API_BASE_URL = 'http://localhost:8080/api';

const App = {
    getToken() {
        return localStorage.getItem('jwt_token');
    },

    getUserRole() {
        return localStorage.getItem('user_role');
    },

    getUserName() {
        return localStorage.getItem('user_name');
    },

    setSession(data) {
        localStorage.setItem('jwt_token', data.token);
        localStorage.setItem('user_role', data.role);
        localStorage.setItem('user_name', data.fullName);
        localStorage.setItem('user_id', data.id);
    },

    clearSession() {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_role');
        localStorage.removeItem('user_name');
        localStorage.removeItem('user_id');
        window.location.href = 'login.html';
    },

    async fetch(url, options = {}) {
        const token = this.getToken();
        const headers = {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const response = await fetch(`${API_BASE_URL}${url}`, {
            ...options,
            headers
        });

        const result = await response.json();

        if (response.status === 401) {
            this.clearSession();
            throw new Error(result.message || 'Session expired. Please login again.');
        }

        if (!response.ok) {
            throw new Error(result.message || 'Request failed');
        }

        return result;
    },

    showAlert(message, type = 'danger') {
        const container = document.getElementById('alert-container');
        if (!container) return;
        container.innerHTML = `
            <div style="padding: 1rem; border-radius: 6px; margin-bottom: 1rem; font-weight: 500; 
                 background-color: ${type === 'success' ? '#dcfce7' : '#fee2e2'}; 
                 color: ${type === 'success' ? '#166534' : '#991b1b'}; 
                 border: 1px solid ${type === 'success' ? '#bbf7d0' : '#fecaca'};">
                ${message}
            </div>
        `;
    }
};
