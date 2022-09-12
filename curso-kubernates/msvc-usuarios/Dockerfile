#cuando una variable se repite mucho en el Dockerfile, la definimos como argumento (constante) y le damos un nombre para reutilizarla en las lineas de mas abajo. Se define en forma global al comienzo del dockerfile y luego solo se menciona en  cada stage
ARG MSVC_NAME=msvc-usuarios
#acá con este archivo dockerfile se estan construyendo una imagen con dos stages (etapas).La primera con el código y las librerías obtenidas desde el repositorio local de maveny la segunda (basada en la primera) que sólo contiene el jar y por lo mismo es mucho mas liviana. Con esto al igual que en el video anterior optimizamos los tiempos de ejecución de docker, pero ocupando el concepto de stages
#el jdk desde donde se ejecutó localmente en forma exitosa el jar
#con "as builder" le damos un nombre al stage
FROM openjdk:8-jdk-alpine as builder
ARG MSVC_NAME
#nombre de la carpeta adonde se va a ejecutar la imagen en elcontainer
WORKDIR /app/$MSVC_NAME
# copiar desde ./target/msvc-usuarios-0.0.1-SNAPSHOT.jar a /app (el punto es /app)
#COPY ./target/msvc-usuarios-0.0.1-SNAPSHOT.jar .
#copiar pom del proyecto padre en app
COPY ./pom.xml /app
#copiar desde carpet .mvn desde ruta ./msvc-usuarios/.mvn en local a ruta /app/msvc-usuarios/.mvn en la imagen
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .
#Crear el jar. Descargar librerias desde internet (Nexus por ejemplo) solo la primera vez, despues descargarlas desde el caché. No correr nunca el código (la idea es que esta instruccion vea exclusivamente dependencias)
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#esta instrucción si se ocupa de copiar el código desde local a /app/msvc-usuarios/src en la imagen si hacemos algun cambio en el código fuente
COPY ./$MSVC_NAME/src ./src
#crear el jar
RUN ./mvnw clean package -DskipTests
#cada vez que hay un FROM es un stage aparte.
FROM openjdk:8-jdk-alpine
WORKDIR /app
#acá se ocupa el stage anterior para copiar el jar desde /app/msvc-usuarios/target/msvc-usuarios-0.0.1-SNAPSHOT.jar en el directorio del jar anterior hacia el directorio /app en el nuevo stage
#para que guarde los logs en ./logs
RUN mkdir ./logs
ARG MSVC_NAME
ARG TARGET_FOLDER=/app/$MSVC_NAME/target
COPY --from=builder $TARGET_FOLDER/msvc-usuarios-0.0.1-SNAPSHOT.jar .
#esta es la variable del puerto por defecto, pero podemos sobrescribirla al construir la imagen en la linea de comandos con --build-arg PORT_APP=8080 por ejemplo
ARG PORT_APP=8001
#variable de entorno para puerto interno  del contenedor
ENV PORT $PORT_APP
#exponer puerto de la linea anterior
EXPOSE $PORT
#cada vez que ejecutamos un contenedor automaticamente se dirige a ejecutar el comando que está dentro delpunto de entrada. En este caso va a ejecutar el jar.
#ojo con el target, ya q si lo hemos generado nosotros con comando mvn package en entrypoint solo debe ir ./msvc-usuarios-0.0.1-SNAPSHOT.jar . El punto en este caso hace referencia al directorio donde estamos en la linea de comandos
#ENTRYPOINT ["java","-jar","msvc-usuarios-0.0.1-SNAPSHOT.jar"]
#el ENTRYPOINT no permite modificar la linea de domandos en modo interactivo. CMD sí lo permite, Puede ser remplazado en los comandos por docker run -p 8001:80
#el modo interactivo permite trabajar dentro de docker como si fuera un terminal normal de linux
#CMD ["java","-jar","msvc-usuarios-0.0.1-SNAPSHOT.jar"]
CMD sleep 20 && java -jar msvc-usuarios-0.1.1-SNAPSHOT.jar