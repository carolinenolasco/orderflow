# Usa imagem base com Java 17
FROM eclipse-temurin:17-jdk-alpine

# Cria diretório para o app
VOLUME /tmp

# Copia o arquivo .jar para dentro da imagem
COPY target/customer-service-1.0.0.jar app.jar

# Comando para executar o app
ENTRYPOINT ["java","-jar","/app.jar"]
