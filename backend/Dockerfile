# ============================================
# Build Stage - Compilation avec JDK
# ============================================
FROM eclipse-temurin:17-jdk-alpine AS builder

# Installer Maven wrapper dependencies
RUN apk add --no-cache wget

WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Télécharger les dépendances (cache layer)
RUN chmod +x ./mvnw && \
    ./mvnw dependency:go-offline -B || true

# Copier le code source
COPY src ./src

# Build l'application (skip tests pour build plus rapide)
RUN ./mvnw clean package -DskipTests -B && \
    find . -name "*.jar" -not -path "./target/*" -delete

# ============================================
# Runtime Stage - Exécution avec JRE
# ============================================
FROM eclipse-temurin:17-jre-alpine

# Créer un utilisateur non-root pour sécurité
RUN addgroup -S spring && \
    adduser -S spring -G spring -u 1001

# Installer wget pour health checks
RUN apk add --no-cache wget tzdata && \
    cp /usr/share/zoneinfo/UTC /etc/localtime && \
    echo "UTC" > /etc/timezone

# Définir l'utilisateur
USER spring:spring

WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Variables d'environnement par défaut
ENV SPRING_PROFILES_ACTIVE=production \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200" \
    TZ=UTC

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Point d'entrée
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
