<script setup lang="ts">
import { onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { useBooksStore } from '@/stores/book'
import { BookCategoryLabels, type BookCategory } from '@/types'

const booksStore = useBooksStore()

function labelOf(cat: string): string {
  return BookCategoryLabels[cat as BookCategory] ?? cat
}

onMounted(async () => {
  if (booksStore.books.length === 0) {
    await booksStore.fetchBooks()
  }
})
</script>

<template>
  <section class="hero card">
    <div class="hero-inner">
      <div>
        <h1>Bienvenue dans <span class="accent">Les Belles Lettres</span></h1>
        <p class="sub">
          Une mini-app full-stack (Vue + Spring Boot) avec JWT, Admin CRUD et base de données.
        </p>

        <div class="cta">
          <RouterLink to="/books" class="btn primary">Explorer le catalogue</RouterLink>
          <RouterLink to="/login" class="btn">Connexion</RouterLink>
        </div>
      </div>

      <div class="stats">
        <div class="stat" v-for="(count, cat) in booksStore.categoryStats" :key="cat">
          <div class="k">{{ count }}</div>
          <div class="v">{{ labelOf(cat) }}</div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.hero { padding: 22px; }
.hero-inner {
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: 18px;
  align-items: center;
}
h1 { margin: 0 0 10px; font-size: 2rem; line-height: 1.15; }
.accent { color: #b9b0ff; }
.sub { margin: 0 0 16px; color: var(--muted); max-width: 56ch; }
.cta { display: flex; gap: 10px; flex-wrap: wrap; }

.stats { display: grid; gap: 10px; }

.stat {
  padding: 14px;
  border-radius: 16px;
  border: 1px solid var(--border);
  background: rgba(255,255,255,0.05);
}

.k { font-weight: 900; font-size: 1.6rem; }
.v { color: var(--muted); }

@media (max-width: 860px) {
  .hero-inner { grid-template-columns: 1fr; }
}
</style>
