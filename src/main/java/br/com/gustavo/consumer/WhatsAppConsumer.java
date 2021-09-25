package br.com.gustavo.consumer;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class WhatsAppConsumer implements Runnable {

    private final Logger logger = Logger.getLogger(WhatsAppConsumer.class);

    @Inject
    ConnectionFactory connectionFactory;

    private final ExecutorService scheduler = Executors.newSingleThreadExecutor();

    private volatile String whatsapp;

    public String getWhatsapp() {
        return whatsapp;
    }

    void onStart(@Observes StartupEvent ev) {
        scheduler.submit(this);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            JMSConsumer consumer = context.createConsumer(context.createQueue("whatsapp"));
            while (true) {
                Message message = consumer.receive();
                if (message == null) return;
                whatsapp = message.getBody(String.class);
                logger.info("Message: " + whatsapp);
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
