#acá con este archivo dockerfile se estan construyendo una imagen con dos stages (etapas).La primera con el código y las librerías obtenidas desde el repositorio local de maveny la segunda (basada en la primera) que sólo contiene el jar y por lo mismo es mucho mas liviana. Con esto al igual que en el video anterior optimizamos los tiempos de ejecución de docker, pero ocupando el concepto de stages
#el jdk desde donde se ejecutó localmente en forma exitosa el jar
#con "as builder" le damos un nombre al stage
FROM openjdk:8-jdk-alpine as builder
#nombre de la carpeta adonde se va a ejecutar la imagen en elcontainer
WORKDIR /app/msvc-cursos
# copiar desde ./target/msvc-cursos-0.0.1-SNAPSHOT.jar a /app (el punto es /app)
#COPY ./target/msvc-cursos-0.0.1-SNAPSHOT.jar .
#copiar pom del proyecto padre en app
COPY ./pom.xml /app
#copiar desde carpet .mvn desde ruta ./msvc-cursos/.mvn en local a ruta /app/msvc-cursos/.mvn en la imagen
COPY ./msvc-cursos/.mvn ./.mvn
COPY ./msvc-cursos/mvnw .
COPY ./msvc-cursos/pom.xml .
#Crear el jar. Descargar librerias desde internet (Nexus por ejemplo) solo la primera vez, despues descargarlas desde el caché. No correr nunca el código (la idea es que esta instruccion vea exclusivamente dependencias)
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#lo mismo que la linea anterior q esta comentada
RUN ./mvnw dependency:go-offline
#esta instrucción si se ocupa de copiar el código desde local a /app/msvc-cursos/src en la imagen si hacemos algun cambio en el código fuente
COPY ./msvc-cursos/src ./src
#crear el jar
RUN ./mvnw clean package -DskipTests
#cada vez que hay un FROM es un stage aparte.
FROM openjdk:8-jdk-alpine
WORKDIR /app
#acá se ocupa el stage anterior para copiar el jar desde /app/msvc-cursos/target/msvc-cursos-0.0.1-SNAPSHOT.jar en el directorio del jar anterior hacia el directorio /app en el nuevo stage
#para que guarde los logs en ./logs
RUN mkdir ./logs
COPY --from=builder /app/msvc-cursos/target/msvc-cursos-0.0.1-SNAPSHOT.jar .
#exponer puerto
EXPOSE 8002
#cada vez que ejecutamos un contenedor automaticamente se dirige a ejecutar el comando que está dentro delpunto de entrada. En este caso va a ejecutar el jar.
#ojo con el target, ya q si lo hemos generado nosotros con comando mvn package en entrypoint solo debe ir ./msvc-cursos-0.0.1-SNAPSHOT.jar . El punto en este caso hace referencia al directorio donde estamos en la linea de comandos
#ENTRYPOINT ["java","-jar","msvc-cursos-0.0.1-SNAPSHOT.jar"]
#el ENTRYPOINT no permite modificar la linea de domandos en modo interactivo. CMD sí lo permite, Puede ser remplazado en los comandos por docker run -p 8001:80
#el modo interactivo permite trabajar dentro de docker como si fuera un terminal normal de linux
CMD ["java","-jar","msvc-cursos-0.0.1-SNAPSHOT.jar"]