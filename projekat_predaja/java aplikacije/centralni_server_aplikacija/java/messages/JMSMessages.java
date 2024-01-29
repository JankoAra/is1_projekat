package messages;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import requests.Request;

public class JMSMessages {

    ConnectionFactory cf;
    Queue requestQueue;
    Queue responseQueue;

    public JMSMessages() {
        JMSResources resources = JMSResources.getInstance();
        this.cf = resources.getConnectionFactory();
        this.requestQueue = resources.getRequestQueue();
        this.responseQueue = resources.getResponseQueue();
    }

    public JMSResponse sendRequest(Request req) {
        JMSContext context = null;
        JMSProducer producer = null;
        JMSConsumer consumer = null;
        try {
            System.out.println("Ulazim u slanje zahteva");
            context = cf.createContext();
            producer = context.createProducer();
            producer.setTimeToLive(5000);
            consumer = context.createConsumer(responseQueue, "target='server'");

            req.setOrigin("server");
            ObjectMessage msg = context.createObjectMessage(req);
            msg.setJMSReplyTo(responseQueue);

            switch (req.getRequestNumber()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 17:
                case 18:
                    msg.setStringProperty("target", "ps1");
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 16:
                case 19:
                case 20:
                case 21:
                    msg.setStringProperty("target", "ps2");
                    break;
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 22:
                case 23:
                case 24:
                case 25:
                    msg.setStringProperty("target", "ps3");
                    break;
                default:
                    return new JMSResponse(false, "Zahtev je nepostojeci", 400);
            }
            System.out.println("Saljem poruku");
            producer.send(requestQueue, msg);
            System.out.println("Poslao poruku");

            System.out.println("Cekam odgovor");
            Message responseMessage = consumer.receive(45000);
            System.out.println("Dobio odgovor");
            if (responseMessage == null) {
                return new JMSResponse(false, "Request timed out", 500);
            }
            ObjectMessage msgResponse = (ObjectMessage) responseMessage;
            Object msgBody = msgResponse.getObject();
            if (!(msgBody instanceof JMSResponse)) {
                return new JMSResponse(false, "Wrong message recieved", 500);
            }
            return (JMSResponse) msgBody;
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (consumer != null) {
                consumer.close();
            }
            if (context != null) {
                context.close();
            }
        }
        return new JMSResponse(false, "Nepredvidjeni izuzetak pri slanju zahteva podsistemu", 500);
    }
}
