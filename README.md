# VelocityLibraryLoader

**VelocityLibraryLoader** — это библиотека для Velocity Proxy, позволяющая динамически загружать сторонние Java-библиотеки (JAR) во время работы прокси. Поддерживает загрузку по Maven-координатам с сохранением структуры папок, как на Bukkit/Spigot, а также по прямым ссылкам.

---

## Возможности
- Загрузка JAR-библиотек по Maven-координатам из любого репозитория
- Сохранение библиотек в структуре папок `libraries/group/artifact/version/artifact-version.jar`
- Загрузка по прямым HTTP/HTTPS ссылкам
- Автоматическое добавление JAR в ClassLoader Velocity
- Логирование в стиле Bukkit/Spigot
- Кэширование уже загруженных библиотек

---

## Требования
- Velocity Proxy 3.x+
- Java 11+

---

## Установка
1. Скопируйте исходники или соберите JAR и добавьте в папку плагинов Velocity.
2. Подключите библиотеку к вашему плагину через зависимость (compileOnly или implementation).

---

## Пример использования

### Загрузка библиотеки по Maven-координатам
```java
@Inject
private VelocityLibraryLoader loader;

// ...

try {
    loader.loadLibraryMaven(
        "https://repo1.maven.org/maven2", // Maven-репозиторий
        "org.jetbrains",                  // groupId
        "annotations",                    // artifactId
        "26.0.2",                         // version
        "DiscordBMB"                      // имя вашего плагина/модуля
    );
} catch (Exception e) {
    logger.error("Ошибка загрузки библиотеки", e);
}
```

### Загрузка библиотеки по прямой ссылке
```java
try {
    loader.loadLibrary(
        "https://repo1.maven.org/maven2/com/google/guava/guava/31.1-jre/guava-31.1-jre.jar",
        "MyPlugin"
    );
} catch (Exception e) {
    logger.error("Ошибка загрузки библиотеки", e);
}
```

---

## Как это работает
- Если библиотека уже скачана — повторно скачивать не будет.
- JAR добавляется в ClassLoader через reflection (аналогично Bukkit/Spigot).
- После успешной загрузки в консоль выводится строка:
  ```
  [VelocityLibraryLoader] [DiscordBMB] Loaded library C:\...\libraries\org\jetbrains\annotations\26.0.2\annotations-26.0.2.jar
  ```

---

## Контакты
- Автор: 1wairesd
- Discord: wairesd 