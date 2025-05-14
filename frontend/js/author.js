/**
 * Author Management functionality
 */
const AuthorModule = (() => {
    // DOM elements
    const authorsTableBody = document.getElementById('authors-table-body');
    const addAuthorBtn = document.getElementById('add-author-btn');
    const authorForm = document.getElementById('author-form');
    const authorModalTitle = document.getElementById('authorModalTitle');
    const authorModal = new bootstrap.Modal(document.getElementById('authorModal'));
    const saveAuthorBtn = document.getElementById('save-author-btn');
    const authorIdInput = document.getElementById('author-id');
    const authorNameInput = document.getElementById('author-name');
    const authorNationalityInput = document.getElementById('author-nationality');
    const authorDobInput = document.getElementById('author-dob');
    const authorBooksSelect = document.getElementById('author-books');
    const confirmationModal = new bootstrap.Modal(document.getElementById('confirmationModal'));
    const confirmDeleteBtn = document.getElementById('confirm-delete-btn');
    const confirmationMessage = document.getElementById('confirmation-message');
    const searchAuthorInput = document.getElementById('search-author');
    const authorDetailsModal = new bootstrap.Modal(document.getElementById('authorDetailsModal'));
    const authorDetailsContent = document.getElementById('author-details-content');

    // State variables
    let authors = [];
    let books = [];
    let currentAuthorId = null;

    // Initialize the module
    const init = async () => {
        attachEventListeners();
        await loadAuthors();
        await loadBooksForSelect();
    };

    // Attach event listeners
    const attachEventListeners = () => {
        addAuthorBtn.addEventListener('click', showAddAuthorModal);
        saveAuthorBtn.addEventListener('click', saveAuthor);
        searchAuthorInput.addEventListener('input', searchAuthors);

        // Event delegation for table row actions
        authorsTableBody.addEventListener('click', (e) => {
            const authorId = e.target.closest('tr')?.dataset.id;
            if (!authorId) return;

            if (e.target.classList.contains('edit-author-btn')) {
                showEditAuthorModal(authorId);
            } else if (e.target.classList.contains('delete-author-btn')) {
                showDeleteAuthorConfirmation(authorId);
            } else if (e.target.classList.contains('view-author-btn')) {
                showAuthorDetails(authorId);
            }
        });
    };

    // Load all authors from the API
    const loadAuthors = async () => {
        try {
            authors = await ApiService.getAllAuthors();
            renderAuthors();
        } catch (error) {
            showAlert('Error loading authors', 'danger');
        }
    };

    // Load books for the select dropdown
    const loadBooksForSelect = async () => {
        try {
            books = await ApiService.getAllBooks();
            renderBooksSelect();
        } catch (error) {
            showAlert('Error loading books', 'danger');
        }
    };

    // Render authors table
    const renderAuthors = () => {
        authorsTableBody.innerHTML = '';
        authors.forEach(author => {
            const bookNames = author.books
                .map(book => book.title)
                .join(', ');

            const formattedDob = author.dateOfBirth ? 
                new Date(author.dateOfBirth).toLocaleDateString() : 'N/A';

            const tr = document.createElement('tr');
            tr.dataset.id = author.id;
            tr.innerHTML = `
                <td>${author.name}</td>
                <td>${author.nationality || 'N/A'}</td>
                <td>${formattedDob}</td>
                <td>${bookNames || 'No books'}</td>
                <td>
                    <button class="btn btn-sm btn-info view-author-btn">
                        <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-warning edit-author-btn">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger delete-author-btn">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            `;
            authorsTableBody.appendChild(tr);
        });
    };

    // Render books in the select dropdown
    const renderBooksSelect = () => {
        authorBooksSelect.innerHTML = '';
        books.forEach(book => {
            const option = document.createElement('option');
            option.value = book.id;
            option.textContent = book.title;
            authorBooksSelect.appendChild(option);
        });
    };

    // Show the add author modal
    const showAddAuthorModal = () => {
        authorModalTitle.textContent = 'Add Author';
        resetAuthorForm();
        currentAuthorId = null;
        authorModal.show();
    };

    // Show the edit author modal
    const showEditAuthorModal = async (authorId) => {
        try {
            const author = await ApiService.getAuthorById(authorId);
            authorModalTitle.textContent = 'Edit Author';
            
            authorIdInput.value = author.id;
            authorNameInput.value = author.name;
            authorNationalityInput.value = author.nationality || '';
            authorDobInput.value = author.dateOfBirth ? author.dateOfBirth.substring(0, 10) : '';
            
            // Select the author's books in the multi-select
            Array.from(authorBooksSelect.options).forEach(option => {
                option.selected = author.books.some(book => book.id == option.value);
            });
            
            currentAuthorId = author.id;
            authorModal.show();
        } catch (error) {
            showAlert('Error loading author details', 'danger');
        }
    };

    // Reset the author form
    const resetAuthorForm = () => {
        authorForm.reset();
        authorIdInput.value = '';
    };

    // Save author (create or update)
    const saveAuthor = async () => {
        if (!validateAuthorForm()) return;

        try {
            const authorData = {
                name: authorNameInput.value,
                nationality: authorNationalityInput.value,
                dateOfBirth: authorDobInput.value || null,
                bookIds: Array.from(authorBooksSelect.selectedOptions).map(option => parseInt(option.value))
            };

            let savedAuthor;
            if (currentAuthorId) {
                savedAuthor = await ApiService.updateAuthor(currentAuthorId, authorData);
                showAlert('Author updated successfully', 'success');
            } else {
                savedAuthor = await ApiService.createAuthor(authorData);
                showAlert('Author created successfully', 'success');
            }

            authorModal.hide();
            await loadAuthors();
        } catch (error) {
            showAlert(`Error: ${error.response?.data?.message || 'Failed to save author'}`, 'danger');
        }
    };

    // Validate the author form
    const validateAuthorForm = () => {
        if (!authorNameInput.value) {
            showAlert('Name is required', 'danger');
            return false;
        }
        return true;
    };

    // Show delete confirmation modal
    const showDeleteAuthorConfirmation = (authorId) => {
        const author = authors.find(a => a.id == authorId);
        if (!author) return;

        confirmationMessage.textContent = `Are you sure you want to delete the author "${author.name}"?`;
        currentAuthorId = authorId;
        
        // Set up the delete button
        const handleDelete = async () => {
            try {
                await ApiService.deleteAuthor(currentAuthorId);
                showAlert('Author deleted successfully', 'success');
                await loadAuthors();
            } catch (error) {
                showAlert('Error deleting author', 'danger');
            } finally {
                confirmationModal.hide();
                confirmDeleteBtn.removeEventListener('click', handleDelete);
            }
        };
        
        // Remove any existing event listeners and add the new one
        confirmDeleteBtn.replaceWith(confirmDeleteBtn.cloneNode(true));
        document.getElementById('confirm-delete-btn').addEventListener('click', handleDelete);
        
        confirmationModal.show();
    };

    // Show author details modal
    const showAuthorDetails = async (authorId) => {
        try {
            const author = await ApiService.getAuthorById(authorId);
            
            let booksList = '<p>No books associated with this author.</p>';
            if (author.books && author.books.length > 0) {
                booksList = '<ul class="author-books-list">' + 
                    author.books.map(book => `<li>${book.title} (${book.publicationYear})</li>`).join('') + 
                    '</ul>';
            }
            
            const formattedDob = author.dateOfBirth ? 
                new Date(author.dateOfBirth).toLocaleDateString() : 'N/A';
            
            authorDetailsContent.innerHTML = `
                <div class="author-details-name">${author.name}</div>
                <div class="author-details-info">
                    <p><strong>Nationality:</strong> ${author.nationality || 'N/A'}</p>
                    <p><strong>Date of Birth:</strong> ${formattedDob}</p>
                    <div class="mt-3">
                        <h5>Books:</h5>
                        ${booksList}
                    </div>
                </div>
            `;
            
            authorDetailsModal.show();
        } catch (error) {
            showAlert('Error loading author details', 'danger');
        }
    };

    // Search authors based on input
    const searchAuthors = () => {
        const searchText = searchAuthorInput.value.toLowerCase();
        const filteredAuthors = authors.filter(author => 
            author.name.toLowerCase().includes(searchText) ||
            (author.nationality && author.nationality.toLowerCase().includes(searchText))
        );
        
        authorsTableBody.innerHTML = '';
        if (filteredAuthors.length === 0) {
            const tr = document.createElement('tr');
            tr.innerHTML = '<td colspan="5" class="text-center">No authors found</td>';
            authorsTableBody.appendChild(tr);
            return;
        }
        
        filteredAuthors.forEach(author => {
            const bookNames = author.books
                .map(book => book.title)
                .join(', ');

            const formattedDob = author.dateOfBirth ? 
                new Date(author.dateOfBirth).toLocaleDateString() : 'N/A';

            const tr = document.createElement('tr');
            tr.dataset.id = author.id;
            tr.innerHTML = `
                <td>${author.name}</td>
                <td>${author.nationality || 'N/A'}</td>
                <td>${formattedDob}</td>
                <td>${bookNames || 'No books'}</td>
                <td>
                    <button class="btn btn-sm btn-info view-author-btn">
                        <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-warning edit-author-btn">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger delete-author-btn">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            `;
            authorsTableBody.appendChild(tr);
        });
    };

    // Show alert message
    const showAlert = (message, type) => {
        // Use this utility function to show alerts/notifications
        // Will be implemented in main.js
        if (window.showNotification) {
            window.showNotification(message, type);
        } else {
            console.log(`${type}: ${message}`);
        }
    };

    // Public API
    return {
        init,
        loadAuthors,
        loadBooksForSelect
    };
})();
