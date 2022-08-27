#el jdk desde donde se ejecutó localmente en forma exitosa el jar
FROM openjdk:8-jdk-alpine
#nombre de la carpeta adonde se va a ejecutar la imagen en elcontainer
WORKDIR /app/msvc-usuarios
# copiar desde ./target/msvc-usuarios-0.0.1-SNAPSHOT.jar a /app (el punto es /app)
#COPY ./target/msvc-usuarios-0.0.1-SNAPSHOT.jar .
#copiar pom del proyecto padre en app
COPY ./pom.xml /app
#copiar el proyecto usuarios en /app/msvc-usuarios
COPY ./msvc-usuarios .
#generar jar al crear la imagen, para que no lo tengamos q hacer nosotros con linea de comandos
RUN ./mvnw clean package -DskipTests
#exponer puerto
EXPOSE 8001
#cada vez que ejecutamos un contenedor automaticamente se dirige a ejecutar el comando que está dentro delpunto de entrada. En este caso va a ejecutar el jar.
#ojo con el target, ya q si lo hemos generado nosotros con comando mvn package en entrypoint solo debe ir ./msvc-usuarios-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","./target/msvc-usuarios-0.0.1-SNAPSHOT.jar"]