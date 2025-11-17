# ========================================
# STAGE 1: BUILD
# ========================================
# Usa imagem Maven com JDK 17 para compilar a aplicação
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Define diretório de trabalho
WORKDIR /app

# Copia apenas pom.xml primeiro (aproveitamento de cache do Docker)
COPY pom.xml .

# Baixa dependências (layer separado para cache)
RUN mvn dependency:go-offline -B

# Copia código fonte
COPY src ./src

# Compila e gera o JAR (pula testes para acelerar build)
RUN mvn clean package -DskipTests

# ========================================
# STAGE 2: RUNTIME
# ========================================
# Usa imagem JRE leve (sem Maven, sem compilador)
FROM eclipse-temurin:17-jre-alpine

# Metadados da imagem
LABEL maintainer="pratica4@example.com"
LABEL version="1.0.0"
LABEL description="Aplicação Spring Boot - Pratica4"

# Define diretório de trabalho
WORKDIR /app

# Cria usuário não-privilegiado para executar a aplicação (segurança)
RUN addgroup -S spring && adduser -S spring -G spring

# Copia JAR da stage de build
COPY --from=build /app/target/*.jar app.jar

# Muda proprietário do JAR para usuário spring
RUN chown spring:spring app.jar

# Executa como usuário não-root
USER spring:spring

# Expõe porta 8080
EXPOSE 8080

# Configurações JVM otimizadas
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
