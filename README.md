# Smart Task Manager

## 1. Краткое описание проекта

Это тестовое задание "Интеллектуальный менеджер задач".  
Проект представляет собой Spring Boot REST API для управления задачами с поддержкой фильтрации, поиска и серверной валидации запросов.  
Приложение использует PostgreSQL для хранения данных и Liquibase для управления миграциями схемы.

## 2. Реализованные функции

- CRUD задач: создание, чтение, обновление и удаление
- Фильтрация по статусу, приоритету и сроку выполнения
- Полнотекстовый поиск по названию и описанию задачи
- Комбинируемые фильтры на стороне сервера
- Валидация входящих запросов
- Единый формат ответов об ошибках
- API документация через Swagger UI

## 3. Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Liquibase
- MapStruct
- Lombok
- SpringDoc OpenAPI (Swagger UI)
- Docker Compose
- Maven

## 4. Настройка окружения

### Требования

- Java 21
- Docker
- Maven

### Запуск приложения

1. Поднять PostgreSQL:

```bash
docker compose up -d
```

2. Запустить приложение:

```bash
./mvnw spring-boot:run
```

Для Windows можно использовать:

```bash
./mvnw.cmd spring-boot:run
```

### Swagger UI

Swagger UI доступен по адресу:

`http://localhost:8080/swagger-ui.html`

## 5. API Endpoints

| Method | Endpoint | Описание |
| --- | --- | --- |
| `POST` | `/api/v1/tasks` | Создать задачу |
| `GET` | `/api/v1/tasks` | Получить список задач с фильтрами |
| `GET` | `/api/v1/tasks/{id}` | Получить задачу по идентификатору |
| `PUT` | `/api/v1/tasks/{id}` | Обновить задачу |
| `DELETE` | `/api/v1/tasks/{id}` | Удалить задачу |

### Параметры фильтрации для `GET /api/v1/tasks`

| Параметр | Тип | Описание |
| --- | --- | --- |
| `status` | `PENDING`, `IN_PROGRESS`, `DONE` | Фильтр по статусу |
| `priority` | `LOW`, `MEDIUM`, `HIGH` | Фильтр по приоритету |
| `dueBefore` | `yyyy-MM-dd` | Задачи со сроком до указанной даты |
| `search` | `String` | Поиск по `title` и `description` |

Пример запроса:

```http
GET /api/v1/tasks?status=IN_PROGRESS&priority=HIGH&dueBefore=2026-04-15&search=payment
```

## 6. Архитектурные решения

- Layered architecture: `controller -> service -> repository`
- DTO отделены от entity
- MapStruct используется для маппинга entity и DTO
- JPA Specification используется для динамических фильтров
- `GlobalExceptionHandler` обеспечивает единый формат ошибок
- Liquibase используется для миграций базы данных
