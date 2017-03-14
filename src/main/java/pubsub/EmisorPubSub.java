package pubsub;

import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

/**
 * @author Rubén Béjar <http://www.rubenbejar.com>
 */
public class EmisorPubSub {

	private final static String EXCHANGE_NAME = "expubsub";   
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
		
		// Declaramos una centralita de tipo fanout llamada EXCHANGE_NAME
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

		int messageNumber;
		boolean end = false;

		do {    	
			System.out.println("Escribe un número y pulsa <Enter> para distribuirlo. El 0 para finalizar.");
			Scanner sc = new Scanner(System.in);
			messageNumber = sc.nextInt();
			if (messageNumber == 0) {
				end = true;
			} else {
				String message = "Mensaje: " + messageNumber;				
				// Publicamos el mensaje en la centralita EXCHANGE_NAME declarada
				// antes. La clave de enrutado la dejamos vacía (la va a ignorar), 
				// y no indicamos propiedades para el mensaje (por ejemplo,
				// el mensaje no será durable)
				channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());				
				System.out.println(" [x] Enviado '" + message + "'");
			}
		} while (!end);

		channel.close();
		connection.close();
	}
}
