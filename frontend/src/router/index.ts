// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue')
    },
    {
      path: '/books',
      name: 'books',
      component: () => import('@/views/BooksView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { guest: true } // Accessible uniquement aux non-connectes
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('@/views/AdminView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/admin/books/new',
      name: 'book-create',
      component: () => import('@/views/BookFormView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/admin/books/:id/edit',
      name: 'book-edit',
      component: () => import('@/views/BookFormView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    }
  ]
})

// Guard global
router.beforeEach(async (to) => {
  const authStore = useAuthStore()

  // Verifier l'authentification au premier chargement
  if (authStore.token && !authStore.user) {
    await authStore.checkAuth()
  }

  // Route necessitant une authentification
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }

  // Route necessitant le role admin
  if (to.meta.requiresAdmin && !authStore.isAdmin) {
    return { name: 'home' }
  }

  // Route reservee aux invites (ex: login)
  if (to.meta.guest && authStore.isAuthenticated) {
    return { name: 'home' }
  }
})

export default router
