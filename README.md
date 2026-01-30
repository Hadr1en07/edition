# TP3 — Gestion de l'État avec Pinia, Validation & Authentification JWT

## Prérequis
- Java 17
- Maven
- Node.js + npm

## Lancer le backend
```bash
cd backend
mvn clean spring-boot:run
```

## Lancer le frontend
```bash
cd frontend
npm install
npm install axios
npm run dev
Frontend : http://localhost:5173
```

## Comptes de test
admin / admin123 (ROLE_ADMIN)
user / user123 (ROLE_USER)

## Endpoints utiles
- Lister les livres (public)
GET http://localhost:8080/api/books
- Détail d’un livre (public)
GET http://localhost:8080/api/books/{id}
- Login (public)
POST http://localhost:8080/api/auth/login