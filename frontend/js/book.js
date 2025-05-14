/**
 * Book Management functionality
 */
const BookModule = (() => {
    // DOM elements
    const booksTableBody = document.getElementById('books-table-body');
    const addBookBtn = document.getElementById('add-book-btn');
    const bookForm = document.getElementById('book-form');
    const bookModalTitle = document.getElementById('bookModalTitle');
    const bookModal = new bootstrap.Modal(document.getElementById('bookModal'));
    const saveBookBtn = document.getElementById('save-book-btn');
    const bookIdInput = document.getElementById('book-id');
    const bookIsbnInput = document.getElementById('book-isbn');
    const bookTitleInput = document.getElementById('book-title');
    const bookCategoryInput = document.getElementById('book-category');
    const bookYearInput = document.getElementById('book-year');
    const bookAuthorsSelect = document.getElementById('book-authors');
    const confirmationModal = new bootstrap.Modal(document.getElementById('confirmationModal'));
    const confirmDeleteBtn = document.getElementById('confirm-delete-btn');
    const confirmationMessage = document.getElementById('confirmation-message');
    const searchBookInput = document.getElementById('search-book');
    const bookDetailsModal = new bootstrap.Modal(document.getElementById('bookDetailsModal'));
    const bookDetailsContent = document.getElementById('book-details-content');

    // State variables
    let books = [];
    let authors = [];
    let currentBookId = null;

    // Initialize the module
    const init = async () => {
        attachEventListeners();
        await loadBooks();
        await loadAuthorsForSelect();
    };

    // Attach event listeners
    const attachEventListeners = () => {
        addBookBtn.addEventListener('click', showAddBookModal);
        saveBookBtn.addEventListener('click', saveBook);
        searchBookInput.addEventListener('input', searchBooks);

        // Event delegation for table row actions
        booksTableBody.addEventListener('click', (e) => {
            const bookId = e.target.closest('tr')?.dataset.id;
            if (!bookId) return;

            if (e.target.classList.contains('edit-book-btn')) {
                showEditBookModal(bookId);
            } else if (e.target.classList.contains('delete-book-btn')) {
                showDeleteBookConfirmation(bookId);
            } else if (e.target.classList.contains('view-book-btn')) {
                showBookDetails(bookId);
            }
        });
    };

    // Load all books from the API
    const loadBooks = async () => {
        try {
            books = await ApiService.getAllBooks();
            renderBooks();
        } catch (error) {
            showAlert('Error loading books', 'danger');
        }
    };

    // Load authors for the select dropdown
    const loadAuthorsForSelect = async () => {
        try {
            authors = await ApiService.getAllAuthors();
            renderAuthorsSelect();
        } catch (error) {
            showAlert('Error loading authors', 'danger');
        }
    };

    // Render books table
    const renderBooks = () => {
        booksTableBody.innerHTML = '';
        books.forEach(book => {
            const authorNames = book.authors
                .map(author => author.name)
                .join(', ');

            const tr = document.createElement('tr');
            tr.dataset.id = book.id;
            tr.innerHTML = `
                <td>${book.isbn}</td>
                <td>${book.title}</td>
                <td>${book.category}</td>
                <td>${book.publicationYear}</td>
                <td>${authorNames || 'No authors'}</td>
                <td>
                    <button class="btn btn-sm btn-info view-book-btn">
                        <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-warning edit-book-btn">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger delete-book-btn">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            `;
            booksTableBody.appendChild(tr);
        });
    };

    // Render authors in the select dropdown
    const renderAuthorsSelect = () => {
        bookAuthorsSelect.innerHTML = '';
        authors.forEach(author => {
            const option = document.createElement('option');
            option.value = author.id;
            option.textContent = author.name;
            bookAuthorsSelect.appendChild(option);
        });
    };

    // Show the add book modal
    const showAddBookModal = () => {
        bookModalTitle.textContent = 'Add Book';
        resetBookForm();
        currentBookId = null;
        bookModal.show();
    };

    // Show the edit book modal
    const showEditBookModal = async (bookId) => {
        try {
            const book = await ApiService.getBookById(bookId);
            bookModalTitle.textContent = 'Edit Book';
            
            bookIdInput.value = book.id;
            bookIsbnInput.value = book.isbn;
            bookTitleInput.value = book.title;
            bookCategoryInput.value = book.category;
            bookYearInput.value = book.publicationYear;
            
            // Select the book's authors in the multi-select
            Array.from(bookAuthorsSelect.options).forEach(option => {
                option.selected = book.authors.some(author => author.id == option.value);
            });
            
            currentBookId = book.id;
            bookModal.show();
        } catch (error) {
            showAlert('Error loading book details', 'danger');
        }
    };

    // Reset the book form
    const resetBookForm = () => {
        bookForm.reset();
        bookIdInput.value = '';
    };

    // Save book (create or update)
    const saveBook = async () => {
        if (!validateBookForm()) return;

        try {
            const bookData = {
                isbn: bookIsbnInput.value,
                title: bookTitleInput.value,
                category: bookCategoryInput.value,
                publicationYear: parseInt(bookYearInput.value),
                authorIds: Array.from(bookAuthorsSelect.selectedOptions).map(option => parseInt(option.value))
            };

            let savedBook;
            if (currentBookId) {
                savedBook = await ApiService.updateBook(currentBookId, bookData);
                showAlert('Book updated successfully', 'success');
            } else {
                savedBook = await ApiService.createBook(bookData);
                showAlert('Book created successfully', 'success');
            }

            bookModal.hide();
            await loadBooks();
        } catch (error) {
            showAlert(`Error: ${error.response?.data?.message || 'Failed to save book'}`, 'danger');
        }
    };

    // Validate the book form
    const validateBookForm = () => {
        if (!bookIsbnInput.value) {
            showAlert('ISBN is required', 'danger');
            return false;
        }
        if (!bookTitleInput.value) {
            showAlert('Title is required', 'danger');
            return false;
        }
        if (!bookCategoryInput.value) {
            showAlert('Category is required', 'danger');
            return false;
        }
        if (!bookYearInput.value || isNaN(parseInt(bookYearInput.value))) {
            showAlert('Publication year must be a valid number', 'danger');
            return false;
        }
        return true;
    };

    // Show delete confirmation modal
    const showDeleteBookConfirmation = (bookId) => {
        const book = books.find(b => b.id == bookId);
        if (!book) return;

        confirmationMessage.textContent = `Are you sure you want to delete the book "${book.title}"?`;
        currentBookId = bookId;
        
        // Set up the delete button
        const handleDelete = async () => {
            try {
                await ApiService.deleteBook(currentBookId);
                showAlert('Book deleted successfully', 'success');
                await loadBooks();
            } catch (error) {
                showAlert('Error deleting book', 'danger');
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

    // Show book details modal
    const showBookDetails = async (bookId) => {
        try {
            const book = await ApiService.getBookById(bookId);
            
            let authorsList = '<p>No authors associated with this book.</p>';
            if (book.authors && book.authors.length > 0) {
                authorsList = '<ul class="book-authors-list">' + 
                    book.authors.map(author => `<li>${author.name}</li>`).join('') + 
                    '</ul>';
            }
            
            bookDetailsContent.innerHTML = `
                <div class="book-details-title">${book.title}</div>
                <div class="book-details-info">
                    <p><strong>ISBN:</strong> ${book.isbn}</p>
                    <p><strong>Category:</strong> ${book.category}</p>
                    <p><strong>Publication Year:</strong> ${book.publicationYear}</p>
                    <div class="mt-3">
                        <h5>Authors:</h5>
                        ${authorsList}
                    </div>
                </div>
            `;
            
            bookDetailsModal.show();
        } catch (error) {
            showAlert('Error loading book details', 'danger');
        }
    };

    // Search books based on input
    const searchBooks = () => {
        const searchText = searchBookInput.value.toLowerCase();
        const filteredBooks = books.filter(book => 
            book.title.toLowerCase().includes(searchText) ||
            book.isbn.toLowerCase().includes(searchText) ||
            book.category.toLowerCase().includes(searchText)
        );
        
        booksTableBody.innerHTML = '';
        if (filteredBooks.length === 0) {
            const tr = document.createElement('tr');
            tr.innerHTML = '<td colspan="6" class="text-center">No books found</td>';
            booksTableBody.appendChild(tr);
            return;
        }
        
        filteredBooks.forEach(book => {
            const authorNames = book.authors
                .map(author => author.name)
                .join(', ');

            const tr = document.createElement('tr');
            tr.dataset.id = book.id;
            tr.innerHTML = `
                <td>${book.isbn}</td>
                <td>${book.title}</td>
                <td>${book.category}</td>
                <td>${book.publicationYear}</td>
                <td>${authorNames || 'No authors'}</td>
                <td>
                    <button class="btn btn-sm btn-info view-book-btn">
                        <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-warning edit-book-btn">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger delete-book-btn">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            `;
            booksTableBody.appendChild(tr);
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
        loadBooks,
        loadAuthorsForSelect
    };
})();
