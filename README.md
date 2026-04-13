# Smart Task Manager

## 1. Описание проекта

Smart Task Manager — это backend-приложение на Spring Boot для управления задачами.
Проект предоставляет REST API для создания, просмотра, обновления и удаления задач,
а также поддерживает фильтрацию, поиск и AI-интеграцию для рекомендации приоритета задачи.

Приложение использует PostgreSQL для хранения данных, Liquibase для миграций схемы базы данных
и Groq API для AI-функциональности.

## 2. Реализованные возможности

- создание задачи
- получение задачи по идентификатору
- получение списка задач с фильтрацией
- обновление задачи
- удаление задачи
- фильтрация по статусу
- фильтрация по приоритету
- фильтрация по сроку выполнения
- полнотекстовый поиск по `title` и `description`
- единый формат ответов об ошибках
- Swagger UI документация
- AI-рекомендация приоритета для существующей задачи

## 3. AI-интеграция

В проекте реализован отдельный AI-endpoint:

`POST /api/v1/tasks/{id}/llm/suggest-priority`

Что делает этот endpoint:

1. загружает задачу по `id`
2. формирует промпт на основе данных задачи
3. отправляет запрос в Groq LLM
4. получает JSON-ответ
5. возвращает клиенту рекомендуемый приоритет и краткое объяснение

Пример ответа:

```json
{
  "taskId": 42,
  "suggestedPriority": "HIGH",
  "reason": "Task has a near deadline and looks urgent."
}
```

Если внешний AI-провайдер недоступен или возвращает невалидный ответ, API вернёт контролируемую ошибку интеграции.

## 4. Технологический стек

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Liquibase
- MapStruct
- Lombok
- Spring AI
- SpringDoc OpenAPI (Swagger UI)
- Docker Compose
- Maven

## 5. Требования для локального запуска

Для локального запуска нужны:

- Java 21
- Docker
- Maven
- аккаунт в Groq
- API key для Groq

## 6. Локальная конфигурация через `.env`

Перед запуском создайте в корне проекта файл `.env`.

Пример содержимого:

```env
DB_URL=jdbc:postgresql://localhost:5432/smart_task_manager
DB_USERNAME=postgres
DB_PASSWORD=postgres
GROQ_API_KEY=your-api-key
GROQ_BASE_URL=https://api.groq.com/openai
GROQ_MODEL=llama-3.3-70b-versatile
```

### Назначение переменных

| Переменная | Описание |
| --- | --- |
| `DB_URL` | URL подключения к PostgreSQL |
| `DB_USERNAME` | пользователь базы данных |
| `DB_PASSWORD` | пароль базы данных |
| `GROQ_API_KEY` | API key для доступа к Groq |
| `GROQ_BASE_URL` | базовый URL OpenAI-совместимого API Groq |
| `GROQ_MODEL` | модель для AI-рекомендации приоритета |

Важно:

- файл `.env` используется только локально
- `.env` добавлен в `.gitignore`
- не коммитьте реальные credentials в репозиторий
- приложение читает эти значения из `.env` через `application.yml`

## 7. Как получить API key в Groq

1. Зарегистрируйтесь или войдите в аккаунт Groq:
   `https://console.groq.com/`
2. Откройте страницу управления ключами:
   `https://console.groq.com/keys`
3. Создайте новый API key
4. Скопируйте его в `GROQ_API_KEY` в вашем локальном `.env`

Полезные ссылки:

- документация Groq: `https://console.groq.com/docs`
- ключи Groq: `https://console.groq.com/keys`

## 8. Локальный запуск проекта

### Шаг 1. Поднять PostgreSQL

```bash
docker compose up -d
```

### Шаг 2. Создать `.env`

Создайте в корне проекта файл `.env` и заполните его реальными значениями.

### Шаг 3. Запустить приложение

Для Linux/macOS:

```bash
./mvnw spring-boot:run
```

Для Windows:

```bash
./mvnw.cmd spring-boot:run
```

### Шаг 4. Открыть Swagger UI

После запуска приложение доступно по адресу:

`http://localhost:8080`

Swagger UI:

`http://localhost:8080/swagger-ui.html`

## 9. API endpoints

| Method | Endpoint | Описание |
| --- | --- | --- |
| `POST` | `/api/v1/tasks` | создать задачу |
| `GET` | `/api/v1/tasks` | получить список задач с фильтрами |
| `GET` | `/api/v1/tasks/{id}` | получить задачу по идентификатору |
| `PUT` | `/api/v1/tasks/{id}` | обновить задачу |
| `DELETE` | `/api/v1/tasks/{id}` | удалить задачу |
| `POST` | `/api/v1/tasks/{id}/llm/suggest-priority` | получить AI-рекомендацию приоритета |

## 10. Параметры фильтрации для `GET /api/v1/tasks`

| Параметр | Тип | Описание |
| --- | --- | --- |
| `status` | `PENDING`, `IN_PROGRESS`, `DONE` | фильтр по статусу |
| `priority` | `LOW`, `MEDIUM`, `HIGH` | фильтр по приоритету |
| `dueBefore` | `yyyy-MM-dd` | задачи со сроком до указанной даты |
| `search` | `String` | поиск по `title` и `description` |

Пример:

```http
GET /api/v1/tasks?status=IN_PROGRESS&priority=HIGH&dueBefore=2026-04-15&search=payment
```

## 11. Пример вызова AI endpoint

```http
POST /api/v1/tasks/42/llm/suggest-priority
```

Пример ответа:

```json
{
  "taskId": 42,
  "suggestedPriority": "MEDIUM",
  "reason": "Task is important, but does not have immediate deadline pressure."
}
```

Возможные причины ошибки:

- задача с таким `id` не найдена
- не настроен `GROQ_API_KEY`
- внешний AI-провайдер недоступен
- модель вернула невалидный JSON

## 12. Архитектурные решения

- слоистая архитектура: `rest -> service -> repository`
- DTO отделены от entity
- MapStruct используется для преобразования entity и DTO
- JPA Specification используется для динамической фильтрации
- `GlobalExceptionHandler` формирует единый формат ошибок
- AI-логика вынесена в отдельный слой `service/llm`
- шаблоны промптов вынесены в `LlmPromptTemplates`
- конфигурация окружения читается из `.env`

## 13. Примечания

- для работы AI-функции нужен валидный ключ Groq
- без корректно заполненного `.env` приложение может не стартовать
- `.env` не должен попадать в git
