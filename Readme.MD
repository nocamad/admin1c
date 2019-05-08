
## Краткое описание
Утилита-клиент для взаимодействия с RAS сервером 1с через java. Требуется установленного JRE

## Использование

Иcпользуйте скомпилированную утилиту по адресу releases/admin1c.jar. 

## Блокирование / Разблокирование РЗ

На данный момент поддерживается одна команда, которая запускает блокировку регламентных заданий на сервере 1С с таймером. Блокировка работает следующим образом: 

* Скрипт каждые 3 секунды обрабатывает все базы, в которых есть любое активное соединение с базой. Время работы скрипта зависит от текущей нагрузки на 1с сервер и количества баз, которые нужно заблокировать.
* В случае если в базе открыт тонкий, веб или толстый клиенты, то регл. задания будут разблокированы в базе
* В случае если в базе открыт конфигуратор или есть фоновые задания, но нет других сеансов, то регл. задания будут заблокированы в базе
* В случае если к базе не удается присоединиться через логин Администратор, пароль 111, то база переводится в offline режим в sql сервере и удаляется из кластера.
Если вам нужно разблокировать РЗ на конкретной базе, то в свойство Описание базы в кластере серверов нужно добавить параметр IGNORE

Пример вызова команды:

```
java -jar admin1c.jar -mode unlock -server1c localhost -portras 1545 -admin1c Administrator -pwd1c 111 -timer 120000
```
Где:

* -mode - учётная запись пользователя AD
* -server1c - сервер 1С с установленной службой RAS
* -portras - адрес порта RAS ( по-умочланию 1545)
* -admin1c - имя администратора для входа в ИБ на кластере 1С (должно быть одинаково для всех баз). Если доступ к ИБ невозможно, то база будет пропущена
* -pwd1c - пароль администратора 1С
* -timer - время в мс в течение которого блокиро

## Компиляция исходников

Компиляция исходников выполняется с одним трюком, о чем ниже:

1. Установите Intellij IDEA
2. Откройте проект
3. Т.к. все либы jar поставляемые 1С (папка lib) не имеют удаленного репозитория, то необходимо организовать свой локальный maven репозиторий. Для этого нужно запустить команду mvn validate для КАЖДОЙ зависимости, перечисленной в  секции "configuration" плагина org.apache.maven.plugins файла pom.xml.
Для этого просто комментируйте с помощью символов /* */ все либы по очереди и запускайте mvn validate
4. Выполните mvn package

## Технологии

- plain java 1.8 проект без фреймворков на базе maven
