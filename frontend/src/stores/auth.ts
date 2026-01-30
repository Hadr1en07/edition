import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/services/api'
import type { User, LoginCredentials, AuthResponse } from '@/types'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const user = ref<User | null>(JSON.parse(localStorage.getItem('user') || 'null'))

  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ROLE_ADMIN')
  const username = computed(() => user.value?.username || '')

  async function login(credentials: LoginCredentials): Promise<void> {
    const response = await api.post<AuthResponse>('/auth/login', credentials)
    const data = response.data

    token.value = data.token
    user.value = { username: data.username, role: data.role }

    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(user.value))
  }

  function logout(): void {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  async function checkAuth(): Promise<boolean> {
    if (!token.value) return false
    try {
      const response = await api.get<User>('/auth/me')
      user.value = response.data
      localStorage.setItem('user', JSON.stringify(user.value))
      return true
    } catch {
      logout()
      return false
    }
  }

  return { token, user, isAuthenticated, isAdmin, username, login, logout, checkAuth }
})
