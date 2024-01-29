package endpoints;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import messages.JMSMessages;
import messages.JMSResponse;
import requests.Request;

@Path("gledanje")
public class GledanjeEndpoint {

    @POST
    @Path("create")
    public Response napraviGledanje(@FormParam("ID video snimka") String idVid, @FormParam("Email korisnika") String email,
            @FormParam("Sekund snimka pocetka gledanja") String sekundPocetka, @FormParam("Trajanje gledanja u sekundama") String trajanje) {
        Request request = new Request();
        request.addParameter("ID video snimka", idVid);
        request.addParameter("Email korisnika", email);
        request.addParameter("Sekund snimka pocetka gledanja", sekundPocetka);
        request.addParameter("Trajanje gledanja u sekundama", trajanje);
        request.setRequestNumber(12);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @GET
    public Response dohvatiGledanjaZaSnimak(@QueryParam("ID video snimka") String idVid) {
        Request request = new Request();
        request.addParameter("ID video snimka", idVid);
        request.setRequestNumber(24);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }
}
