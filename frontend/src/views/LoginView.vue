<template>
  <div class="login">
    <h1>Connexion</h1>

    <form @submit.prevent="handleSubmit" class="form">
      <div class="field">
        <label for="username">Nom d’utilisateur</label>
        <input
          id="username"
          v-model="form.username"
          type="text"
          autocomplete="username"
          required
        />
      </div>

      <div class="field">
        <label for="password">Mot de passe</label>
        <input
          id="password"
          v-model="form.password"
          type="password"
          autocomplete="current-password"
          required
        />
      </div>

      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

      <button type="submit" :disabled="loading">
        {{ loading ? 'Connexion...' : 'Se connecter' }}
      </button>
    </form>

    <p class="hint">
      Comptes test : <br />
      <code>admin / admin123</code> (ROLE_ADMIN) <br />
      <code>user / user123</code> (ROLE_USER)
    </p>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import type { LoginCredentials } from '@/types'

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()

const form = reactive<LoginCredentials>({
  username: '',
  password: ''
})

const loading = ref(false)
const errorMessage = ref<string | null>(null)

async function handleSubmit() {
  errorMessage.value = null
  loading.value = true
  try {
    await authStore.login({ ...form })

    // Si on vient d'une route protégée, on y retourne
    const redirect = (route.query.redirect as string) || '/'
    await router.push(redirect)
  } catch (e: any) {
    errorMessage.value = 'Identifiants invalides'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login {
  max-width: 420px;
  margin: 40px auto;
  padding: 24px;
  border: 1px solid #ddd;
  border-radius: 10px;
}
.form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
input {
  padding: 10px 12px;
  border: 1px solid #ccc;
  border-radius: 8px;
}
button {
  padding: 10px 12px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.error {
  color: #c0392b;
  margin: 0;
}
.hint {
  margin-top: 18px;
  color: #555;
}
code {
  background: #f3f3f3;
  padding: 2px 6px;
  border-radius: 6px;
}
</style>
