package filtrotuberia;


import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

/**
 * @author Rubén Béjar <http://www.rubenbejar.com>
 *
 */
public class Origen {

	private final static String QUEUE_NAME = "pipe1";   
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

		// Creamos una cola en el canal llamada QUEUE_NAME (operación
		// idempotente: solo se creará si no existe ya)
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		boolean end = false;

		do {    	
			System.out.println("Escribe un texto y pulsa <Enter> para enviarlo. Escribe FIN para salir");
			Scanner sc = new Scanner(System.in);
			String message = sc.nextLine();    
			if (message.equals("FIN")) {
				end = true;
			} else {
				channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
				System.out.println(" [x] Enviado '" + message + "'");
			}
		} while (!end);

		channel.close();
		connection.close();
	}
}