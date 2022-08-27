#el jdk desde donde se ejecutó localmente en forma exitosa el jar
FROM openjdk:8-jdk-alpine
#nombre de la carpeta adonde se va a ejecutar la imagen en elcontainer
WORKDIR /app/msvc-usuarios
# copiar desde ./target/msvc-usuarios-0.0.1-SNAPSHOT.jar a /app (el punto es /app)
#COPY ./target/msvc-usuarios-0.0.1-SNAPSHOT.jar .
#copiar pom del proyecto padre en app
COPY ./pom.xml /app
#copiar desde carpet .mvn desde ruta ./msvc-usuarios/.mvn en local a ruta /app/msvc-usuarios/.mvn en la imagen
COPY ./msvc-usuarios/.mvn ./.mvn
COPY ./msvc-usuarios/mvnw .
COPY ./msvc-usuarios/pom.xml .
#Crear el jar. Descargar librerias desde internet (Nexus por ejemplo) solo la primera vez, despues descargarlas desde el caché. No correr nunca el código (la idea es que esta instruccion vea exclusivamente dependencias)
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#esta instrucción si se ocupa de copiar el código desde local a /app/msvc-usuarios/src en la imagen si hacemos algun cambio en el código fuente
COPY ./msvc-usuarios/src ./src
#crear el jar
RUN ./mvnw clean package -DskipTests
#exponer puerto
EXPOSE 8001
#cada vez que ejecutamos un contenedor automaticamente se dirige a ejecutar el comando que está dentro delpunto de entrada. En este caso va a ejecutar el jar.
#ojo con el target, ya q si lo hemos generado nosotros con comando mvn package en entrypoint solo debe ir ./msvc-usuarios-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","./target/msvc-usuarios-0.0.1-SNAPSHOT.jar"]