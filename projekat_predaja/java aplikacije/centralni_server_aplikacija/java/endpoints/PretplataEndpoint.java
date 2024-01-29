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

@Path("pretplata")
public class PretplataEndpoint {

    @POST
    @Path("create")
    public Response napraviPretplatu(@FormParam("Email korisnika") String email, @FormParam("Naziv paketa") String nazivPaketa) {
        Request request = new Request();
        request.addParameter("Naziv paketa", nazivPaketa);
        request.addParameter("Email korisnika", email);
        request.setRequestNumber(11);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @GET
    public Response dohvatiPretplateZaKorisnika(@QueryParam("Email korisnika") String email) {
        Request request = new Request();
        request.addParameter("Email korisnika", email);
        request.setRequestNumber(23);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }
}
