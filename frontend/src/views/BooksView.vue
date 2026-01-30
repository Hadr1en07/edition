<script setup lang="ts">
import { onMounted } from 'vue'
import { useBooksStore } from '../stores/book'

const booksStore = useBooksStore()

onMounted(() => {
  booksStore.fetchBooks()
})
</script>

<template>
  <main>
    <h1>Catalogue</h1>

    <p v-if="booksStore.loading">Chargement...</p>
    <p v-else-if="booksStore.error">{{ booksStore.error }}</p>

    <ul v-else>
      <li v-for="b in booksStore.filteredBooks" :key="b.id">
        {{ b.title }} — {{ b.author }}
      </li>
    </ul>
  </main>
</template>
