# Java-ShareIt
### Перед вами бэкенд для сервиса по шэрингу, то есть короткой аренде вещей
#### Сервис хранит информацию о вещах, которые сдаются в аренду (название, описание, владелец), о пользователях (имя, почта) и о запросах пользователей на эти вещи.
Сервис позволяет не только бронировать вещь на определённые даты, но и закрывать к ней доступ на время бронирования от других желающих. <br>

В работе используются Spring Boot, SQL, Lombok, JpaRepository, RestTemplate, JUnit тесты, Mockito, Docker. <br>

Сервис разделён на два приложения: <br>
__Gateway__ - для работы с запросами пользователей <br>
__Server__ - основное приложение
##### Для взаимодействия с сервисом созданы следующие эндпоинты:

* __Создать пользователя__ : POST http://localhost:8080/users
{
"name": "user",
"email": "user@user.com"
} <br>
* __Обновить пользователя__ : PATCH http://localhost:8080/users/{userId}
{
"name": "update",
"email": "update@user.com"
} <br>
* __Получить пользователя__ : GET http://localhost:8080/users/{userId} <br>
* __Получить всех пользователей__ : GET http://localhost:8080/users <br>
* __Удалить пользователя__ : DELETE http://localhost:8080/users/{userId} <br>

* __Создать вещь__ : POST http://localhost:8080/items
{
"name": "Дрель",
"description": "Простая дрель",
"available": true
}  <br>
Headers: X-Sharer-User-Id - 1 <br>
* __Обновить вещь__ : PATCH http://localhost:8080/items/{itemId}
{
"id": 1,
"name": "Дрель+",
"description": "Аккумуляторная дрель",
"available": false
} <br>
Headers: X-Sharer-User-Id - 1 <br>
* __Получить вещь__ : GET http://localhost:8080/items/{itemId} <br>
* __Получить все вещи__ : GET http://localhost:8080/items <br>
RequestParams:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какой размер список нужно получить), default value = 20

* __Поиск вещей по названию__ : GET http://localhost:8080/items/search?text=дрель <br>
RequestParams:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какой размер список нужно получить), default value = 20

* __Опубликовать комментарий к вещи__ : POST http://localhost:8080/items/{itemId}/comment
{
"text": "Comment for item 1"
} <br>
Headers: X-Sharer-User-Id - 1 <br>

* __Создать бронирование вещи__ : POST http://localhost:8080/bookings
{
"itemId": 2,
"start": "2024-10-10T12:00:00",
"end": "2024-11-10T12:00:00"
} <br>
Headers: X-Sharer-User-Id - 1 <br>
* __Одобрить или отклонить бронирование__ : PATCH http://localhost:8080/bookings/{bookingId}?approved=true <br>
Headers: X-Sharer-User-Id - 2 <br>
RequestParam: 
  * boolean approved, default true <br>
* __Получить своё бронирование__ : GET http://localhost:8080/bookings/{bookingId} <br>
Headers: X-Sharer-User-Id - 1 <br>
* __Получить свои бронирования__ : GET http://localhost:8080/bookings <br>
Headers: X-Sharer-User-Id - 1 <br>
RequestParams:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какой размер список нужно получить), default value = 20
  * state (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED) (Фильтр бронирований), not required <br>

* __Получить бронирования своих вещей__ : GET http://localhost:8080/bookings/owner <br>
Headers: X-Sharer-User-Id - 2 <br>
RequestParams:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какой размер список нужно получить), default value = 20
  * state (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED) (Фильтр бронирований), not required <br>

* __Создать запрос на вещь__ : POST http://localhost:8080/requests
{
"description": "Дрель"
} <br>
Headers: X-Sharer-User-Id - 1 <br>
* __Получить свои запросы__ : GET http://localhost:8080/requests <br>
Headers: X-Sharer-User-Id - 1 <br>
* __Получить все запросы, кроме своих__ : GET http://localhost:8080/requests/all <br>
Headers: X-Sharer-User-Id - 2 <br>
RequestParams:
  * int __from__ (С какого номера по порядку начать поиск), default value = 0
  * int __size__ (Какой размер список нужно получить), default value = 20

* __Получить запрос__ : GET http://localhost:8080/requests/{requestId} <br>
Headers: X-Sharer-User-Id - 2 <br>

Сервис использует базу данных PostgreSql
###
Схема базы данных <br>
![DB Server](https://github.com/ARTpknk/java-shareit/assets/108333044/464a5789-ea37-458c-b131-8a52d53bebc9)

###
используется кодировка UTF-8