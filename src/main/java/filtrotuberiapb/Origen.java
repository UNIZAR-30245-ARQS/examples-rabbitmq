package filtrotuberiapb;

import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Rubén Béjar <http://www.rubenbejar.com>
 *
 */
public class Origen {

	private final static String QUEUE_NAME = "pipe1";   

	public static void main(String[] argv) throws Exception {
		//  Creamos una conexión al broker RabbitMQ en localhost
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");    
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
