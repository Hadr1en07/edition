export interface Book {
  id?: number
  title: string
  author: string
  isbn: string
  price: number
  description?: string
  category: BookCategory
  publicationYear: number
  coverUrl?: string
}

export enum BookCategory {
  ROMAN = 'ROMAN',
  POESIE = 'POESIE',
  THEATRE = 'THEATRE',
  ESSAI = 'ESSAI',
  BIOGRAPHIE = 'BIOGRAPHIE'
}

export const BookCategoryLabels: Record<BookCategory, string> = {
  [BookCategory.ROMAN]: 'Roman',
  [BookCategory.POESIE]: 'Poésie',
  [BookCategory.THEATRE]: 'Théâtre',
  [BookCategory.ESSAI]: 'Essai',
  [BookCategory.BIOGRAPHIE]: 'Biographie'
}

export interface User {
  username: string
  role: string
}

export interface LoginCredentials {
  username: string
  password: string
}

export interface AuthResponse {
  token: string
  username: string
  role: string
}
