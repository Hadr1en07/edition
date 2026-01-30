// src/composables/useValidation.ts
import { computed, reactive, ref } from 'vue'

// Type pour une règle de validation
type ValidationRule = (value: any) => string | true

// Type pour un ensemble de règles par champ
type ValidationRules<T> = {
  [K in keyof T]?: ValidationRule[]
}

export function useValidation<T extends Record<string, any>>(
  initialData: T,
  rules: ValidationRules<T>
) {
  type Field = Extract<keyof T, string>

  // Données du formulaire
  const formData = reactive({ ...initialData }) as T

  // Erreurs par champ
  // ref plutôt que reactive pour éviter les erreurs TS
  // sur l’indexation dynamique (errors[field])
  const errors = ref<Record<Field, string>>({} as Record<Field, string>)

  // Champs touchés (pour n'afficher les erreurs qu'après interaction)
  // ref pour pouvoir faire touched[field] proprement
  const touched = ref<Record<Field, boolean>>({} as Record<Field, boolean>)

  // Valider un champ spécifique
  function validateField(field: Field): boolean {
    const fieldRules = (rules[field] ?? []) as ValidationRule[]

    for (const rule of fieldRules) {
      const result = rule((formData as any)[field])
      if (result !== true) {
        errors.value[field] = result
        return false
      }
    }

    errors.value[field] = ''
    return true
  }

  // Valider tous les champs
  function validateAll(): boolean {
    let isValidForm = true

    for (const field of Object.keys(rules) as Field[]) {
      touched.value[field] = true
      if (!validateField(field)) {
        isValidForm = false
      }
    }

    return isValidForm
  }

  // Marquer un champ comme touché (sur blur)
  function handleBlur(field: Field): void {
    touched.value[field] = true
    validateField(field)
  }

  // Valider en temps réel (sur input)
  function handleInput(field: Field): void {
    if (touched.value[field]) {
      validateField(field)
    }
  }

  // Réinitialiser le formulaire
  function resetForm(): void {
    Object.assign(formData, initialData)

    // On remet erreurs + touched à zéro pour tous les champs connus dans rules
    for (const key of Object.keys(rules) as Field[]) {
      errors.value[key] = ''
      touched.value[key] = false
    }
  }

  // Le formulaire est-il valide ?
  const isValid = computed(() => {
    return Object.values(errors.value).every(e => !e)
  })

  return {
    formData,
    errors,
    touched,
    validateField,
    validateAll,
    handleBlur,
    handleInput,
    resetForm,
    isValid
  }
}

// Règles de validation réutilisables
export const validationRules = {
  required:
    (message = 'Ce champ est obligatoire') =>
    (value: any) => {
      if (value === null || value === undefined || value === '') {
        return message
      }
      return true
    },

  minLength:
    (min: number, message?: string) =>
    (value: string) => {
      if (!value || value.length < min) {
        return message || `Minimum ${min} caracteres`
      }
      return true
    },

  maxLength:
    (max: number, message?: string) =>
    (value: string) => {
      if (value && value.length > max) {
        return message || `Maximum ${max} caracteres`
      }
      return true
    },

  isbn:
    (message = 'Format ISBN invalide (978-XXXXXXXXXX)') =>
    (value: string) => {
      const isbnRegex = /^978-\d{10}$/
      if (!isbnRegex.test(value)) {
        return message
      }
      return true
    },

  positiveNumber:
    (message = 'Doit etre un nombre positif') =>
    (value: number) => {
      if (typeof value !== 'number' || value <= 0) {
        return message
      }
      return true
    },

  year:
    (min: number, max: number) =>
    (value: number) => {
      if (value < min || value > max) {
        return `Annee entre ${min} et ${max}`
      }
      return true
    }
}
