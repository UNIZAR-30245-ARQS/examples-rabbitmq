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

	public static void main(String[] argv) throws Exception {
		//  Creamos una conexión al broker RabbitMQ en localhost
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
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
