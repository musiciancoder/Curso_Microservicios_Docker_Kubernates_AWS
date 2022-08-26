#el jdk desde donde se ejecutó localmente en forma exitosa el jar
FROM openjdk:8-jdk-alpine
#nombre de la carpeta adonde se va a ejecutar la imagen en elcontainer
WORKDIR /app
# copiar desde ./target/msvc-usuarios-0.0.1-SNAPSHOT.jar a /app (el punto es /app)
COPY ./target/msvc-usuarios-0.0.1-SNAPSHOT.jar .
#exponer puerto
EXPOSE 8001
#cada vez que ejecutamos un contenedor automaticamente se dirige a ejecutar el comando que está dentro delpunto de entrada. En este caso va a ejecutar el jar.
ENTRYPOINT ["java","-jar","msvc-usuarios-0.0.1-SNAPSHOT.jar"]