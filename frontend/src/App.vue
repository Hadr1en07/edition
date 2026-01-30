<script setup lang="ts">
import NavBar from '@/components/NavBar.vue'
import { RouterView } from 'vue-router'
</script>

<template>
  <NavBar />
  <main class="container" style="padding: 22px 0 34px;">
    <RouterView />
  </main>
</template>
