/**
 * API Service for the Book and Author Management System
 * Handles all HTTP requests to the backend server
 */
const API_URL = 'http://localhost:8080';

const ApiService = {
    // Book API calls
    getAllBooks: async () => {
        try {
            const response = await axios.get(`${API_URL}/books`);
            return response.data;
        } catch (error) {
            console.error('Error fetching books:', error);
            throw error;
        }
    },

    getBookById: async (id) => {
        try {
            const response = await axios.get(`${API_URL}/books/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching book with id ${id}:`, error);
            throw error;
        }
    },

    createBook: async (bookData) => {
        try {
            const response = await axios.post(`${API_URL}/books`, bookData);
            return response.data;
        } catch (error) {
            console.error('Error creating book:', error);
            throw error;
        }
    },

    updateBook: async (id, bookData) => {
        try {
            const response = await axios.put(`${API_URL}/books/${id}`, bookData);
            return response.data;
        } catch (error) {
            console.error(`Error updating book with id ${id}:`, error);
            throw error;
        }
    },

    deleteBook: async (id) => {
        try {
            await axios.delete(`${API_URL}/books/${id}`);
            return true;
        } catch (error) {
            console.error(`Error deleting book with id ${id}:`, error);
            throw error;
        }
    },

    addAuthorsToBook: async (bookId, authorIds) => {
        try {
            const response = await axios.post(`${API_URL}/books/${bookId}/authors`, authorIds);
            return response.data;
        } catch (error) {
            console.error(`Error adding authors to book with id ${bookId}:`, error);
            throw error;
        }
    },

    removeAuthorsFromBook: async (bookId, authorIds) => {
        try {
            const response = await axios.delete(`${API_URL}/books/${bookId}/authors`, { data: authorIds });
            return response.data;
        } catch (error) {
            console.error(`Error removing authors from book with id ${bookId}:`, error);
            throw error;
        }
    },

    // Author API calls
    getAllAuthors: async () => {
        try {
            const response = await axios.get(`${API_URL}/authors`);
            return response.data;
        } catch (error) {
            console.error('Error fetching authors:', error);
            throw error;
        }
    },

    getAuthorById: async (id) => {
        try {
            const response = await axios.get(`${API_URL}/authors/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching author with id ${id}:`, error);
            throw error;
        }
    },

    createAuthor: async (authorData) => {
        try {
            const response = await axios.post(`${API_URL}/authors`, authorData);
            return response.data;
        } catch (error) {
            console.error('Error creating author:', error);
            throw error;
        }
    },

    updateAuthor: async (id, authorData) => {
        try {
            const response = await axios.put(`${API_URL}/authors/${id}`, authorData);
            return response.data;
        } catch (error) {
            console.error(`Error updating author with id ${id}:`, error);
            throw error;
        }
    },

    deleteAuthor: async (id) => {
        try {
            await axios.delete(`${API_URL}/authors/${id}`);
            return true;
        } catch (error) {
            console.error(`Error deleting author with id ${id}:`, error);
            throw error;
        }
    },

    addBooksToAuthor: async (authorId, bookIds) => {
        try {
            const response = await axios.post(`${API_URL}/authors/${authorId}/books`, bookIds);
            return response.data;
        } catch (error) {
            console.error(`Error adding books to author with id ${authorId}:`, error);
            throw error;
        }
    },

    removeBooksFromAuthor: async (authorId, bookIds) => {
        try {
            const response = await axios.delete(`${API_URL}/authors/${authorId}/books`, { data: bookIds });
            return response.data;
        } catch (error) {
            console.error(`Error removing books from author with id ${authorId}:`, error);
            throw error;
        }
    }
};
