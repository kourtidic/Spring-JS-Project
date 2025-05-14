/**
 * Main Application Entry Point
 * Handles page navigation and initialization
 */
document.addEventListener('DOMContentLoaded', () => {
    // DOM elements for navigation
    const mainContent = document.getElementById('main-content');
    const welcomePage = document.getElementById('welcome-page');
    const booksPage = document.getElementById('books-page');
    const authorsPage = document.getElementById('authors-page');
    const navBooks = document.getElementById('nav-books');
    const navAuthors = document.getElementById('nav-authors');
    const welcomeBooksBtn = document.getElementById('welcome-books-btn');
    const welcomeAuthorsBtn = document.getElementById('welcome-authors-btn');

    // Initialize the application
    const init = () => {
        // Set up page navigation
        setupNavigation();
        
        // Set up notification system
        setupNotifications();
        
        // Initialize modules
        BookModule.init();
        AuthorModule.init();
    };

    // Set up page navigation
    const setupNavigation = () => {
        // Navigation from navbar
        navBooks.addEventListener('click', (e) => {
            e.preventDefault();
            showPage(booksPage);
            highlightNavItem(navBooks);
        });

        navAuthors.addEventListener('click', (e) => {
            e.preventDefault();
            showPage(authorsPage);
            highlightNavItem(navAuthors);
        });

        // Navigation from welcome page
        welcomeBooksBtn.addEventListener('click', () => {
            showPage(booksPage);
            highlightNavItem(navBooks);
        });

        welcomeAuthorsBtn.addEventListener('click', () => {
            showPage(authorsPage);
            highlightNavItem(navAuthors);
        });
    };

    // Show the selected page
    const showPage = (page) => {
        // Hide all pages
        welcomePage.style.display = 'none';
        booksPage.style.display = 'none';
        authorsPage.style.display = 'none';

        // Show the selected page
        page.style.display = 'block';

        // Refresh data when showing respective pages
        if (page === booksPage) {
            BookModule.loadBooks();
        } else if (page === authorsPage) {
            AuthorModule.loadAuthors();
        }
    };

    // Highlight the active nav item
    const highlightNavItem = (navItem) => {
        // Remove active class from all nav items
        document.querySelectorAll('.nav-link').forEach(item => {
            item.classList.remove('active');
        });

        // Add active class to the selected nav item
        navItem.classList.add('active');
    };

    // Set up notification system
    const setupNotifications = () => {
        // Create a notification container if it doesn't exist
        if (!document.getElementById('notification-container')) {
            const container = document.createElement('div');
            container.id = 'notification-container';
            container.className = 'position-fixed top-0 end-0 p-3';
            container.style.zIndex = '1050';
            document.body.appendChild(container);
        }

        // Global notification function
        window.showNotification = (message, type = 'info') => {
            const container = document.getElementById('notification-container');
            const notificationId = `notification-${Date.now()}`;
            
            const notification = document.createElement('div');
            notification.className = `toast align-items-center text-white bg-${type}`;
            notification.id = notificationId;
            notification.setAttribute('role', 'alert');
            notification.setAttribute('aria-live', 'assertive');
            notification.setAttribute('aria-atomic', 'true');
            
            notification.innerHTML = `
                <div class="d-flex">
                    <div class="toast-body">
                        ${message}
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            `;
            
            container.appendChild(notification);
            
            const toast = new bootstrap.Toast(notification, { autohide: true, delay: 5000 });
            toast.show();
            
            // Remove the notification from the DOM after it's hidden
            notification.addEventListener('hidden.bs.toast', () => {
                notification.remove();
            });
        };
    };

    // Initialize the application
    init();
});
