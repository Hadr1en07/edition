<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BookForm from '@/components/BookForm.vue'
import { useBooksStore } from '@/stores/book'
import type { Book } from '@/types'

const route = useRoute()
const router = useRouter()
const booksStore = useBooksStore()

const isEditMode = computed(() => !!route.params.id)
const bookId = computed(() => Number(route.params.id))

onMounted(async () => {
  if (isEditMode.value) {
    await booksStore.fetchBook(bookId.value)
  } else {
    booksStore.currentBook = null
  }
})

async function handleSubmit(bookData: Omit<Book, 'id'>) {
  if (isEditMode.value) {
    await booksStore.updateBook(bookId.value, bookData)
  } else {
    await booksStore.createBook(bookData)
  }
  router.push({ name: 'admin' })
}

function handleCancel() {
  router.push({ name: 'admin' })
}
</script>

<template>
  <main>
    <h1>{{ isEditMode ? 'Modifier un livre' : 'Créer un livre' }}</h1>

    <p v-if="booksStore.loading">Chargement...</p>
    <p v-else-if="booksStore.error">{{ booksStore.error }}</p>

    <BookForm
      v-else
      :book="isEditMode ? booksStore.currentBook ?? undefined : undefined"
      @submit="handleSubmit"
      @cancel="handleCancel"
    />
  </main>
</template>
