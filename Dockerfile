# Use a imagem oficial do OpenJDK para Java 20
FROM openjdk:20-jdk-slim

# Diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie os arquivos JAR para o diretório de trabalho
COPY target/cadastroPessoas-0.0.1-SNAPSHOT.jar /app/cadastroPessoas.jar

# Expor a porta que seu aplicativo Java está ouvindo
EXPOSE 8080

# Comando para executar o aplicativo Java
CMD ["java", "-jar", "cadastroPessoas.jar"]


ENV DB_HOST=jdbc:postgresql://localhost:5432/postgres
ENV DB_PORT=5432
ENV DB_NAME=postgres
ENV DB_USER=postgres
ENV DB_PASSWORD=password

# ...
