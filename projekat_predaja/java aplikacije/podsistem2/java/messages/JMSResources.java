package messages;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSResources {

    private static final JMSResources instance = new JMSResources();
    private ConnectionFactory cf;
    private Queue responseQueue;
    private Queue requestQueue;

    private JMSResources() {
        try {
            Context ctx = new InitialContext();

            cf = (ConnectionFactory) ctx.lookup("cfProjekat");
            requestQueue = (Queue) ctx.lookup("requestQueue");
            responseQueue = (Queue) ctx.lookup("responseQueue");

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public Queue getResponseQueue() {
        return responseQueue;
    }

    public void setResponseQueue(Queue responseQueue) {
        this.responseQueue = responseQueue;
    }

    public Queue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(Queue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public static JMSResources getInstance() {
        return instance;
    }

    public ConnectionFactory getConnectionFactory() {
        return cf;
    }

}
