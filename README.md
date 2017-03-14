# Ejemplos y ejercicios de arquitecturas software con RabbitMQ
Implementados en Java (aunque RabbitMQ soporta clientes en múltiples lenguajes).
Estilos de filtro y tubería y publicación-suscripción.

## Requisitos
Hace falta un broker RabbitMQ funcionando para que funcionen los ejemplos. Lo más sencillo puede ser usar la instancia incluida en el plan gratuito de [CloudAMQP](https://www.cloudamqp.com/). En ese caso tendrás una URL que puede ser parecida a esto: `amqp://USER:PASSWORD@spotted-monkey.rmq.cloudamqp.com/USER`. Otra opción es hacer una instalación local de RabbitMQ en tu computador o usar la imagen de Docker que te proporcionan los de RabbitMQ.

Luego tienes que tener una variable de entorno llamada `CLOUDAMQP_URL` con la URL de tu broker. Si no la encuentran, los ejemplos tratan de conectar con un broker en `amqp://localhost`. Los ficheros .sh incluidos en el repositorio intentan cargar un fichero $HOME/private-env-vars.sh, que es donde yo tengo definida esta variable (está en un fichero fuera del repositorio para no compartir en GitHub la URL a mi broker en CloudAMQP que incluye el password, y porque lo uso para otras cosas; si solo lo usara para esto, podría tenerlo en el repositorio y hacer que no se compartiase usando el .gitignore). Podéis crear uno en vuestro $HOME y aprovechar esto.

## Uso (desde consola)
Puesto que hay muchas clases con función main, he configurado el gradle.build para que se puedan lanzar indicando la que queremos desde línea de comandos. El comando genérico es `$ ./gradlew --quiet -PmainClass=LaClaseQueSea run` y las clases con main disponibles son:

Ejemplo básico:

- ejemplo.Emisor
- ejemplo.Receptor

Ejemplo de filtro y tubería:

- filtrotuberia.Origen
- filtrotuberia.FiltroLongPar
- filtrotuberia.FiltroAMayus
- filtrotuberia.Destino

Ejercicio propuesto de filtro y tubería:

- filtrotuberiapb.Origen
- filtrotuberiapb.FiltroAMayus
- filtrotuberiapb.FiltroLong
- filtrotuberiapb.DestinoPar
- filtrotuberiapb.DestinoImpar

Ejemplo de publicación-suscripción:

- pubsub.EmisorPubSub
- pubsub.ReceptorPubSub

Así que por ejemplo, para ejecutar el ejemplo básico puedes abrir dos consolas en el directorio pipe_and_filter_rabbitmq y ejecutar en una `$ ./gradlew --quiet -PmainClass=ejemplo.Emisor run` y en la otra `$ ./gradlew --quiet -PmainClass=ejemplo.Receptor run` y probar a enviar mensajes entre ellas.

## Uso (desde Eclipse)
Instala el plugin "Gradle Integration for Eclipse" y luego File > Import..., Gradle Project, busca el directorio que contiene el fichero build.gradle, haz click en Build Model, elige el que sale y Finish. Esto importa el proyecto en Eclipse. Si las dependencias no se refrescan bien, haz click derecho en el proyecto en el Package Explorer y elige Gradle > Refresh All.

Luego puedes ejecutar las clases que tienen main y probar. Puedes tener abiertas varias consolas simultáneas dentro de Eclipse para ver el intercambio de mensajes entre las distintas aplicaciones.

## Arquitectura
Diagrama de CyC del ejercicio propuesto de filtro y tubería (corresponde a las clases del paquete `filtrotuberiapb`).
![Diagrama de CyC del ejercicio](https://rbejar.github.io/images/arqs_pipefilterrabbitmq_ejer1.png)