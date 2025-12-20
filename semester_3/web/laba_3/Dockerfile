# --- STAGE 1: BUILD ---
FROM gradle:7.6.4-jdk11 AS build

WORKDIR /app

# Копируем только те файлы, которые нужны для сборки
COPY build.gradle settings.gradle /app/
COPY src /app/src

# Выполняем сборку и копирование зависимостей (для Stage 2)
# Предполагается, что задача 'copyDependencies' определена в build.gradle
RUN gradle clean build copyDependencies --no-daemon


# --- STAGE 2: RUN (WildFly) ---
FROM jboss/wildfly:20.0.1.Final

# 1. КОНФИГУРАЦИЯ WILDFLY
# Копируем конфигурацию standalone.xml с настроенным DataSource
COPY forDockerStandalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml

# 2. УСТАНОВКА ДРАЙВЕРА POSTGRESQL (Правильно настроенное копирование)
RUN mkdir -p /opt/jboss/wildfly/modules/org/postgresql/main/
COPY forDockerModule.xml /opt/jboss/wildfly/modules/org/postgresql/main/module.xml
COPY --from=build /app/build/dependencies/postgresql-42.7.7.jar \
    /opt/jboss/wildfly/modules/org/postgresql/main/postgresql-42.7.7.jar


# 3. ОЧИСТКА /deployments (Очистка от примеров, важно!)
RUN rm -rf /opt/jboss/wildfly/standalone/deployments/*

# 4. КОПИРУЕМ WAR (Самый простой способ развертывания)
COPY --from=build /app/build/libs/laba_3.war \
    /opt/jboss/wildfly/standalone/deployments/laba_3.war

# 5. !!! УДАЛЕНО: RUN echo "" > ...laba_3.war.dodeploy !!!
#    Удаляем файл-маркер, чтобы избежать конфликта Duplicate resource.

# 6. ПОРТЫ И ЗАПУСК
EXPOSE 8080 9990
# ИСПРАВЛЕНИЕ: Отключаем автоматическое сканирование директории deployments
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone.xml", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-Djboss.as.server.deployer.enabled=false"]