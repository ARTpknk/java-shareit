# Java-ShareIt
### Перед вами бэкенд для сервиса по шэрингу, то есть короткой аренде вещей
#### Сервис хранит информацию о вещах, которые сдаются в аренду (название, описание, владелец), о пользователях (имя, почта) и о запросах пользователей на эти вещи.
Сервис позволяет не только бронировать вещь на определённые даты, но и закрывать к ней доступ на время бронирования от других желающих. <br>

В работе используются Spring Boot, SQL, Lombok, JpaRepository, RestTemplate, JUnit тесты, Mockito. <br>

Сервис разделён на два приложения: <br>
__Gateway__ - для работы с запросами пользователей <br>
__Server__ - основное приложение
##### Для взаимодействия с сервисом созданы следующие эндпоинты:







Сервис использует базу данных PostgreSql
###
Схема базы данных <br>
![Untitled (6)](https://github.com/ARTpknk/java-shareit/assets/108333044/464a5789-ea37-458c-b131-8a52d53bebc9)

###
используется кодировка UTF-8
