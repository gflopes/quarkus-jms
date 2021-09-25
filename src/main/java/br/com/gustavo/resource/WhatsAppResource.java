package br.com.gustavo.resource;

import br.com.gustavo.consumer.WhatsAppConsumer;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/whatsapp")
public class WhatsAppResource {

    @Inject
    WhatsAppConsumer consumer;

    @GET
    @Path("last")
    @Produces(MediaType.TEXT_PLAIN)
    public String last() {
        return consumer.getWhatsapp();
    }
}