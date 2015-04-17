package ejemplo;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

/**
 * Adaptado de <https://www.rabbitmq.com/tutorials/tutorial-one-java.html>
 * @author Rubén Béjar <http://www.rubenbejar.com>
 *
 */
public class Receptor {

	private final static String QUEUE_NAME = "hola";

	public static void main(String[] argv) throws Exception {
		//  Creamos una conexión al broker RabbitMQ en localhost
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		// Con un solo canal
		Channel channel = connection.createChannel();

		// Declaramos una cola en el broker a través del canal
		// recién creado llamada QUEUE_NAME (operación
		// idempotente: solo se creará si no existe ya)
		// Se crea tanto en el emisor como en el receptor, porque no
		// sabemos cuál se lanzará antes
		// Indicamos que no sea durable ni exclusiva
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Esperando mensajes. CTRL+C para salir");

		// El objeto consumer guardará los mensajes que lleguen
		// a la cola QUEUE_NAME hasta que los usemos
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumer);

		while (true) {
			// bloquea hasta que llege un mensaje 
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Recibido '" + message + "'");
		}
	}
}
