package filtrotuberiapb;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author Rubén Béjar <http://www.rubenbejar.com>
 *
 */
public class FiltroLong {
	private final static String IN_QUEUE_NAME = "pipe2";
	private final static String OUTPAR_QUEUE_NAME = "pipe3";
	private final static String OUTIMPAR_QUEUE_NAME = "pipe4";
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

		// Creamos tres colas en el canal. Serán la
		// tubería de entrada y las dos de salida del filtro
		channel.queueDeclare(IN_QUEUE_NAME, false, false, false, null);
		channel.queueDeclare(OUTPAR_QUEUE_NAME, false, false, false, null);
		channel.queueDeclare(OUTIMPAR_QUEUE_NAME, false, false, false, null);
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
			if (message.length() % 2 == 0) {
				channel.basicPublish("", OUTPAR_QUEUE_NAME, null, message.getBytes());
				System.out.println(" [x] Enviado mensaje de longitud par '" + message + "'");
			} else {
				channel.basicPublish("", OUTIMPAR_QUEUE_NAME, null, message.getBytes());
				System.out.println(" [x] Enviado mensaje de longitud impar '" + message + "'");
			}
		}
	}
}
