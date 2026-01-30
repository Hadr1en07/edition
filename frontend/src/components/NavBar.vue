<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()

const logged = computed(() => auth.isAuthenticated)
const admin = computed(() => auth.isAdmin)

function logout() {
  auth.logout()
  router.push({ name: 'home' })
}
</script>

<template>
  <header class="nav">
    <div class="container nav-inner">
      <RouterLink to="/" class="brand">
        <span class="dot"></span>
        <span>Les Belles Lettres</span>
      </RouterLink>

      <nav class="links">
        <RouterLink to="/" class="link">Accueil</RouterLink>
        <RouterLink to="/books" class="link">Catalogue</RouterLink>
        <RouterLink v-if="admin" to="/admin" class="link">Admin</RouterLink>
      </nav>

      <div class="right">
        <span v-if="logged" class="badge">
          👤 <strong>{{ auth.username }}</strong>
          <span style="opacity:.8">({{ auth.user?.role }})</span>
        </span>

        <RouterLink v-if="!logged" to="/login" class="btn">Connexion</RouterLink>
        <button v-else class="btn danger" @click="logout">Déconnexion</button>
      </div>
    </div>
  </header>
</template>

<style scoped>
.nav {
  position: sticky;
  top: 0;
  z-index: 10;
  backdrop-filter: blur(10px);
  background: rgba(11, 15, 25, 0.55);
  border-bottom: 1px solid var(--border);
}
.nav-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14px;
  padding: 14px 0;
}
.brand {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-weight: 800;
  letter-spacing: 0.2px;
}
.dot {
  width: 12px;
  height: 12px;
  border-radius: 999px;
  background: var(--brand);
  box-shadow: 0 0 0 6px rgba(124,92,255,0.18);
}
.links {
  display: flex;
  gap: 14px;
  align-items: center;
}
.link {
  padding: 8px 10px;
  border-radius: 12px;
  color: var(--muted);
}
.link.router-link-exact-active {
  color: var(--text);
  background: rgba(255,255,255,0.06);
  border: 1px solid var(--border);
}
.right {
  display: flex;
  gap: 10px;
  align-items: center;
}
@media (max-width: 720px) {
  .links { display: none; }
}
</style>
