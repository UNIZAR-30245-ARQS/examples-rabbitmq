# Ejemplos y ejercicios de arquitecturas software con RabbitMQ
Implementados en Java (aunque RabbitMQ soporta clientes en múltiples lenguajes).
Estilos de filtro y tubería y publicación-suscripción.

## Requisitos
Hay que tener instalado y funcionando el servidor de RabbitMQ en el computador para que funcionen los ejemplos. RabbitMQ es un middleware de mensajería basado en el protocolo abierto AMQP e implementado en Erlang. Las instrucciones para descargar e instalar el servidor en distintos sistemas están en <http://www.rabbitmq.com/download.html>. La versión que he usado y probado es la 3.5.1, pero es posible que los ejemplos funcionen con versiones anteriores y/o posteriores de RabbitMQ.

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
