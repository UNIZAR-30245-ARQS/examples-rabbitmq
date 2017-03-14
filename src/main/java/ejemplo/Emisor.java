package ejemplo;

import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

/**
 * Adaptado de <https://www.rabbitmq.com/tutorials/tutorial-one-java.html>
 * @author Rubén Béjar <http://www.rubenbejar.com>
 *
 */
public class Emisor {

	private final static String QUEUE_NAME = "hola";   
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";

	public static void main(String[] argv) throws Exception {
		// Conexión al broker RabbitMQ broker (prueba en la URL de
		// la variable de entorno que se llame como diga ENV_AMQPURL_NAME
		// o sino en localhost)
		ConnectionFactory factory = new ConnectionFactory();
		String amqpURL = System.getenv().get(ENV_AMQPURL_NAME) != null ? System.getenv().get(ENV_AMQPURL_NAME) : "amqp://localhost";
		try {
			factory.setUri(amqpURL);
		} catch (Exception e) {
			System.out.println(" [*] AQMP broker not found in " + amqpURL);
			System.exit(-1);
		}
		System.out.println(" [*] AQMP broker found in " + amqpURL);
		Connection connection = factory.newConnection();
		// Con un solo canal
		Channel channel = connection.createChannel();

		// Declaramos una cola en el broker a través del canal
		// recién creado llamada QUEUE_NAME (operación
		// idempotente: solo se creará si no existe ya)
		// Se crea tanto en el emisor como en el receptor, porque no
		// sabemos cuál se lanzará antes.
		// Indicamos que no sea durable ni exclusiva
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		int messageNumber;
		boolean end = false;

		do {    	
			System.out.println("Escribe un número y pulsa <Enter> para enviarlo. El 0 para finalizar.");
			Scanner sc = new Scanner(System.in);
			messageNumber = sc.nextInt();
			if (messageNumber == 0) {
				end = true;
			} else {
				String message = "Mensaje: " + messageNumber;
				// En el modelo de mensajería de RabbitMQ los productores nunca mandan mensajes
				// directamente a colas, siempre los publican a un exchange (centralita) que
				// los enruta a colas (por criterios definidos según el tipo de exchange).
				// En este caso, el string vacío (primer parámetro) identifica el "default" o 
				// "nameless" exchange: los mensajes se enrutan a la cola indicad por 
				// routingKey (segundo parámetro) si existe.
				channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
				System.out.println(" [x] Enviado '" + message + "'");
			}
		} while (!end);

		channel.close();
		connection.close();
	}
}
