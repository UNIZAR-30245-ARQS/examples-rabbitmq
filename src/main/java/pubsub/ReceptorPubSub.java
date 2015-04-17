package pubsub;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

/** 
 * @author Rubén Béjar <http://www.rubenbejar.com>
 */
public class ReceptorPubSub {

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
		// Creamos una nueva cola temporal (no durable, exclusiva y
		// que se borrará automáticamente cuando nos desconectemos
		// del servidor de RabbitMQ). El servidor le dará un
		// nombre aleatorio que guardaremos en queueName
		String queueName = channel.queueDeclare().getQueue();
		// E indicamos que queremos que la centralita EXCHANGE_NAME
		// envíe los mensajes a la cola recién creada. Para ello creamos
		// una unión (binding) entre ellas (la clave de enrutado
		// la ponemos vacía, porque se va a ignorar)	
		channel.queueBind(queueName, EXCHANGE_NAME, "");
		
		
		System.out.println(" [*] Esperando mensajes. CTRL+C para salir");

		// El objeto consumer guardará los mensajes que lleguen
		// a la cola queueName hasta que los usemos
		QueueingConsumer consumer = new QueueingConsumer(channel);
		// autoAck a true
		channel.basicConsume(queueName, true, consumer);

		while (true) {
			// bloquea hasta que llege un mensaje 
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Recibido '" + message + "'");
		}
	}
}
