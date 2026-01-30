<!-- src/components/BookForm.vue -->
<template>
  <form class="book-form" @submit.prevent="handleSubmit">
    <div class="field">
      <label for="title">Titre</label>
      <input
        id="title"
        type="text"
        v-model="formData.title"
        @blur="handleBlur('title')"
        @input="handleInput('title')"
        placeholder="Titre du livre"
      />
      <p v-if="touched.title && errors.title" class="error">{{ errors.title }}</p>
    </div>

    <div class="field">
      <label for="author">Auteur</label>
      <input
        id="author"
        type="text"
        v-model="formData.author"
        @blur="handleBlur('author')"
        @input="handleInput('author')"
        placeholder="Nom de l'auteur"
      />
      <p v-if="touched.author && errors.author" class="error">{{ errors.author }}</p>
    </div>

    <div class="field">
      <label for="isbn">ISBN</label>
      <input
        id="isbn"
        type="text"
        v-model="formData.isbn"
        @blur="handleBlur('isbn')"
        @input="handleInput('isbn')"
        placeholder="978-1234567890"
      />
      <p v-if="touched.isbn && errors.isbn" class="error">{{ errors.isbn }}</p>
    </div>

    <div class="field">
      <label for="price">Prix (€)</label>
      <input
        id="price"
        type="number"
        step="0.01"
        v-model.number="formData.price"
        @blur="handleBlur('price')"
        @input="handleInput('price')"
      />
      <p v-if="touched.price && errors.price" class="error">{{ errors.price }}</p>
    </div>

    <div class="field">
      <label for="category">Catégorie</label>
      <select
        id="category"
        v-model="formData.category"
        @blur="handleBlur('category')"
        @change="handleInput('category')"
      >
        <option disabled value="">-- Sélectionner --</option>
        <option v-for="cat in categories" :key="cat" :value="cat">
          {{ BookCategoryLabels[cat] }}
        </option>
      </select>
      <p v-if="touched.category && errors.category" class="error">{{ errors.category }}</p>
    </div>

    <div class="field">
      <label for="publicationYear">Année de publication</label>
      <input
        id="publicationYear"
        type="number"
        v-model.number="formData.publicationYear"
        @blur="handleBlur('publicationYear')"
        @input="handleInput('publicationYear')"
      />
      <p v-if="touched.publicationYear && errors.publicationYear" class="error">
        {{ errors.publicationYear }}
      </p>
    </div>

    <div class="field">
      <label for="coverUrl">URL couverture (optionnel)</label>
      <input
        id="coverUrl"
        type="text"
        v-model="formData.coverUrl"
        @blur="handleBlur('coverUrl')"
        @input="handleInput('coverUrl')"
        placeholder="https://..."
      />
      <p v-if="touched.coverUrl && errors.coverUrl" class="error">{{ errors.coverUrl }}</p>
    </div>

    <div class="field">
      <label for="description">Description</label>
      <textarea
        id="description"
        rows="6"
        v-model="formData.description"
        @blur="handleBlur('description')"
        @input="onDescriptionInput"
        placeholder="Description (max 1000 caractères)"
      />
      <div class="meta">
        <span class="counter">{{ descriptionLength }}/1000</span>
      </div>
      <p v-if="touched.description && errors.description" class="error">
        {{ errors.description }}
      </p>
    </div>

    <div class="actions">
      <button type="button" class="btn" @click="emit('cancel')">
        Annuler
      </button>

      <button type="submit" class="btn primary" :disabled="!canSubmit">
        {{ props.book ? 'Enregistrer' : 'Créer' }}
      </button>
    </div>
  </form>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useValidation, validationRules } from '@/composables/useValidation'
import type { Book, BookCategory } from '@/types'
import { BookCategoryLabels, BookCategory as BookCategoryEnum } from '@/types'

const props = defineProps<{
  book?: Book // Si fourni, mode edition
}>()

const emit = defineEmits<{
  submit: [book: Omit<Book, 'id'>]
  cancel: []
}>()

const currentYear = new Date().getFullYear()

const categories = Object.values(BookCategoryEnum) as BookCategory[]

const { formData, errors, touched, handleBlur, handleInput, validateAll, isValid } =
  useValidation(
    {
      title: props.book?.title || '',
      author: props.book?.author || '',
      isbn: props.book?.isbn || '',
      price: props.book?.price ?? 0,
      description: props.book?.description || '',
      category: (props.book?.category || '') as BookCategory,
      publicationYear: props.book?.publicationYear || currentYear,
      coverUrl: props.book?.coverUrl || ''
    },
    {
      title: [
        validationRules.required(),
        validationRules.minLength(2),
        validationRules.maxLength(200)
      ],
      author: [
        validationRules.required(),
        validationRules.minLength(2),
        validationRules.maxLength(100)
      ],
      isbn: [validationRules.required(), validationRules.isbn()],
      price: [validationRules.required(), validationRules.positiveNumber()],
      description: [validationRules.maxLength(1000)],
      category: [validationRules.required('Selectionnez une categorie')],
      publicationYear: [validationRules.required(), validationRules.year(1800, currentYear)]
    }
  )

const descriptionLength = computed(() => (formData.description || '').length)

function onDescriptionInput() {
  handleInput('description')
}

const canSubmit = computed(() => {
  // isValid = pas d'erreurs, mais on veut aussi empêcher le submit tant que les champs
  // obligatoires n'ont pas été validés une première fois => validateAll au submit.
  return isValid.value
})

function handleSubmit() {
  if (validateAll()) {
    const payload: Omit<Book, 'id'> = {
      title: formData.title,
      author: formData.author,
      isbn: formData.isbn,
      price: formData.price,
      description: formData.description || undefined,
      category: formData.category,
      publicationYear: formData.publicationYear,
      coverUrl: formData.coverUrl || undefined
    }
    emit('submit', payload)
  }
}
</script>

<style scoped>
.book-form {
  display: grid;
  gap: 14px;
  max-width: 700px;
}

.field {
  display: grid;
  gap: 6px;
}

label {
  font-weight: 600;
}

input, select, textarea {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.error {
  color: #c62828;
  font-size: 0.9rem;
  margin: 0;
}

.meta {
  display: flex;
  justify-content: flex-end;
}

.counter {
  font-size: 0.85rem;
  color: #666;
}

.actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 6px;
}

.btn {
  padding: 10px 14px;
  border-radius: 10px;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
}

.btn.primary {
  border-color: #111;
  background: #111;
  color: white;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
