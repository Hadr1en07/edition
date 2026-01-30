// src/stores/books.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/services/api'
import type { Book, BookCategory } from '@/types'

export const useBooksStore = defineStore('books', () => {
  // === STATE ===
  const books = ref<Book[]>([])
  const currentBook = ref<Book | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  // Filtres
  const searchQuery = ref('')
  const selectedCategory = ref<BookCategory | null>(null)

  // === GETTERS ===
  // Livres filtres par recherche et categorie
  const filteredBooks = computed(() => {
    return books.value.filter(book => {
      // Filtre par recherche (titre ou auteur)
      const matchesSearch =
        !searchQuery.value ||
        book.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        book.author.toLowerCase().includes(searchQuery.value.toLowerCase())

      // Filtre par categorie
      const matchesCategory =
        !selectedCategory.value || book.category === selectedCategory.value

      return matchesSearch && matchesCategory
    })
  })

  // Statistiques par categorie
  const categoryStats = computed(() => {
    const stats: Record<string, number> = {}
    for (const book of books.value) {
      stats[book.category] = (stats[book.category] || 0) + 1
    }
    return stats
  })

  // === ACTIONS ===
  async function fetchBooks(): Promise<void> {
    loading.value = true
    error.value = null
    try {
      const response = await api.get<Book[]>('/books')
      books.value = response.data
    } catch (e) {
      error.value = 'Erreur lors du chargement des livres'
      throw e
    } finally {
      loading.value = false
    }
  }

  async function fetchBook(id: number): Promise<void> {
    loading.value = true
    error.value = null
    try {
      const response = await api.get<Book>(`/books/${id}`)
      currentBook.value = response.data
    } catch (e) {
      error.value = 'Livre non trouve'
      throw e
    } finally {
      loading.value = false
    }
  }

  async function createBook(book: Omit<Book, 'id'>): Promise<Book> {
    const response = await api.post<Book>('/books', book)
    books.value.push(response.data)
    return response.data
  }

  async function updateBook(id: number, book: Partial<Book>): Promise<Book> {
    const response = await api.put<Book>(`/books/${id}`, book)
    const index = books.value.findIndex(b => b.id === id)
    if (index !== -1) {
      books.value[index] = response.data
    }
    return response.data
  }

  async function deleteBook(id: number): Promise<void> {
    await api.delete(`/books/${id}`)
    books.value = books.value.filter(b => b.id !== id)
  }

  // Methodes de filtrage
  function setSearchQuery(query: string): void {
    searchQuery.value = query
  }

  function setCategory(category: BookCategory | null): void {
    selectedCategory.value = category
  }

  return {
    // State
    books,
    currentBook,
    loading,
    error,
    searchQuery,
    selectedCategory,
    // Getters
    filteredBooks,
    categoryStats,
    // Actions
    fetchBooks,
    fetchBook,
    createBook,
    updateBook,
    deleteBook,
    setSearchQuery,
    setCategory
  }
})
