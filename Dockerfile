# Указываем базовый образ с Maven и OpenJDK 17
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем settings.xml
COPY settings.xml /root/.m2/settings.xml

# Копируем файлы проекта
COPY . .

# Собираем проект с помощью Maven
RUN mvn clean package -DskipTests

# Создаем новый образ для запуска
FROM amazoncorretto:17 AS runtime

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar-файл из этапа сборки
COPY --from=build /app/target/*.jar app.jar

# Указываем команду для запуска
ENTRYPOINT ["java", "-jar", "app.jar"]

# Указываем порт, который будет использовать приложение
EXPOSE 8080
