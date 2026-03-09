# Recipe API

Small Spring Boot backend for your cooking app.

It supports:
- creating users
- creating recipes
- listing recipes with search
- sorting by newest or most liked
- recipe details with ingredients and steps
- likes and dislikes per user

## Tech stack
- Java 21
- Spring Boot
- Spring Data JPA
- Flyway
- MySQL

## Project structure

```text
src/main/java/com/recipeapp/api
├── config
├── controller
├── dto
├── entity
├── exception
├── mapper
├── repository
└── service
```

## Database setup

The app is already configured for your Aiven MySQL host, port, database name, and username.

For safety, the password is **not hardcoded** into the project. Set it before starting the app:

### Linux / macOS
```bash
export DB_PASSWORD='PASTE_YOUR_AIVEN_PASSWORD_HERE'
```

### Windows PowerShell
```powershell
$env:DB_PASSWORD='PASTE_YOUR_AIVEN_PASSWORD_HERE'
```

You can also override host, port, DB name, and username:

```bash
export DB_HOST='mysql-miqo8-osvaldomarkaj88-452a.i.aivencloud.com'
export DB_PORT='15201'
export DB_NAME='defaultdb'
export DB_USERNAME='avnadmin'
```

## Run locally

If you have Maven installed:

```bash
mvn spring-boot:run
```

Or build a jar:

```bash
mvn clean package
java -jar target/recipe-api-0.0.1-SNAPSHOT.jar
```

The first start will automatically create all tables through Flyway.

## Main endpoints

### Users
- `POST /api/users`
- `GET /api/users`

### Recipes
- `POST /api/recipes`
- `GET /api/recipes?search=chicken&sort=mostLiked`
- `GET /api/recipes/{id}`
- `GET /api/recipes/top-liked?limit=10`

### Reactions
- `POST /api/recipes/{id}/reactions`

## Example requests

### Create user
```http
POST /api/users
Content-Type: application/json

{
  "displayName": "Osvaldo",
  "email": "osvaldo@example.com"
}
```

### Create recipe
```http
POST /api/recipes
Content-Type: application/json

{
  "title": "Spaghetti Carbonara",
  "shortDescription": "Creamy pasta with eggs, cheese, and bacon.",
  "authorId": 1,
  "ingredients": [
    "200g spaghetti",
    "2 eggs",
    "50g parmesan cheese",
    "100g bacon",
    "Salt",
    "Black pepper"
  ],
  "steps": [
    "Boil the spaghetti until al dente.",
    "Cook the bacon until crispy.",
    "Whisk eggs with parmesan cheese.",
    "Mix hot spaghetti with bacon.",
    "Remove from heat and stir in egg mixture.",
    "Serve with black pepper."
  ]
}
```

### Like or dislike a recipe
```http
POST /api/recipes/1/reactions
Content-Type: application/json

{
  "userId": 1,
  "reactionType": "LIKE"
}
```

## Notes
- One user can have only one reaction per recipe.
- Sending a new reaction updates the previous one.
- Search looks at recipe title, short description, and author name.
- Sorting supports `latest` and `mostLiked`.
