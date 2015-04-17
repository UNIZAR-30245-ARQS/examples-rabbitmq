package filtrotuberia;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author Rubén Béjar <http://www.rubenbejar.com>
 *
 */
public class FiltroAMayus {

	private final static String IN_QUEUE_NAME = "pipe2";
	private final static String OUT_QUEUE_NAME = "pipe3";

	public static void main(String[] argv) throws Exception {
		//  Creamos una conexión al broker RabbitMQ en localhost
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		// Con un solo canal
		Channel channel = connection.createChannel();

		// Creamos dos colas en el canal. Serán la
		// tubería de entrada y la de salida del filtro
		channel.queueDeclare(IN_QUEUE_NAME, false, false, false, null);
		channel.queueDeclare(OUT_QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Esperando mensajes. CTRL+C para salir");

		// El objeto consumer guardará los mensajes que lleguen
		// a la cola QUEUE_NAME hasta que los usemos
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(IN_QUEUE_NAME, true, consumer);

		while (true) {
			// consumer.nextDelivery() bloquea hasta que llege un mensaje 
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Recibido '" + message + "'");
			String messageMays = message.toUpperCase();
			channel.basicPublish("", OUT_QUEUE_NAME, null, messageMays.getBytes());
			System.out.println(" [x] Enviado '" + messageMays + "'");
		}
	}
}