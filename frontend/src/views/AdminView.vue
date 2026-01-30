<template>
  <div class="admin">
    <div class="header">
      <h1>Administration</h1>
      <div class="actions">
        <button @click="goCreate">+ Nouveau livre</button>
        <button class="secondary" @click="logout">Déconnexion</button>
      </div>
    </div>

    <p class="info" v-if="authStore.user">
      Connecté en tant que <strong>{{ authStore.user.username }}</strong>
      ({{ authStore.user.role }})
    </p>

    <div v-if="booksStore.loading" class="status">Chargement...</div>
    <div v-else-if="booksStore.error" class="status error">{{ booksStore.error }}</div>

    <table v-else class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Titre</th>
          <th>Auteur</th>
          <th>Catégorie</th>
          <th>Année</th>
          <th>Prix</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="b in booksStore.books" :key="b.id">
          <td>{{ b.id }}</td>
          <td>{{ b.title }}</td>
          <td>{{ b.author }}</td>
          <td>{{ b.category }}</td>
          <td>{{ b.publicationYear }}</td>
          <td>{{ b.price }} €</td>
          <td class="row-actions">
            <button class="secondary" @click="goEdit(b.id!)">Modifier</button>
            <button class="danger" @click="remove(b.id!)">Supprimer</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useBooksStore } from '@/stores/book'

const router = useRouter()
const authStore = useAuthStore()
const booksStore = useBooksStore()

onMounted(async () => {
  await booksStore.fetchBooks()
})

function goCreate() {
  router.push({ name: 'book-create' })
}

function goEdit(id: number) {
  router.push({ name: 'book-edit', params: { id } })
}

async function remove(id: number) {
  const ok = confirm('Supprimer ce livre ?')
  if (!ok) return
  await booksStore.deleteBook(id)
}

function logout() {
  authStore.logout()
  router.push({ name: 'home' })
}
</script>

<style scoped>
.admin {
  max-width: 1100px;
  margin: 32px auto;
  padding: 0 16px;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}
.actions {
  display: flex;
  gap: 10px;
}
.info {
  margin: 10px 0 18px;
  color: #444;
}
.status {
  padding: 14px;
  border: 1px solid #ddd;
  border-radius: 10px;
}
.status.error {
  border-color: #e0b4b4;
  color: #c0392b;
}
.table {
  width: 100%;
  border-collapse: collapse;
}
th,
td {
  padding: 10px;
  border-bottom: 1px solid #eee;
  text-align: left;
}
.row-actions {
  display: flex;
  gap: 8px;
}
button {
  padding: 8px 10px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
button.secondary {
  background: #f3f3f3;
}
button.danger {
  background: #ffdddd;
}
</style>
