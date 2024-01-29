package endpoints;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import messages.JMSMessages;
import messages.JMSResponse;
import requests.Request;

@Path("paket")
public class PaketEndpoint {

    @POST
    @Path("create")
    public Response napraviPaket(@FormParam("Naziv paketa") String naziv, @FormParam("Mesecna cena") String cena) {
        Request request = new Request();
        request.addParameter("Naziv paketa", naziv);
        request.addParameter("Mesecna cena", cena);
        request.setRequestNumber(9);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @PUT
    @Path("cena")
    public Response proemniCenuPaketa(@FormParam("Naziv paketa") String naziv, @FormParam("Nova mesecna cena") String cena) {
        Request request = new Request();
        request.addParameter("Naziv paketa", naziv);
        request.addParameter("Nova mesecna cena", cena);
        request.setRequestNumber(10);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @GET
    @Path("sve")
    public Response dohvatiSvePakete() {
        Request request = new Request();
        request.setRequestNumber(22);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }
}
