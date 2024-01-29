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

@Path("korisnik")
public class KorisnikEndpoint {

    @POST
    @Path("create")
    public Response napraviKorisnika(@FormParam("Ime") String ime, @FormParam("Email") String email, @FormParam("Godiste") String godiste,
            @FormParam("Pol") String pol, @FormParam("Naziv mesta") String mesto) {
        Request request = new Request();
        request.addParameter("Ime", ime);
        request.addParameter("Email", email);
        request.addParameter("Godiste", godiste);
        request.addParameter("Pol", pol);
        request.addParameter("Naziv mesta", mesto);
        request.setRequestNumber(2);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @PUT
    @Path("email")
    public Response promeniEmail(@FormParam("Email korisnika") String oldEmail, @FormParam("Novi email korisnika") String newEmail) {
        Request request = new Request();
        request.addParameter("Email korisnika", oldEmail);
        request.addParameter("Novi email korisnika", newEmail);
        request.setRequestNumber(3);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @PUT
    @Path("mesto")
    public Response promeniMesto(@FormParam("Email korisnika") String email, @FormParam("Naziv novog mesta") String imeMesta) {
        Request request = new Request();
        request.addParameter("Email korisnika", email);
        request.addParameter("Naziv novog mesta", imeMesta);
        request.setRequestNumber(4);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @GET
    @Path("sve")
    public Response dohvatiSveKorisnike() {
        Request request = new Request();
        request.setRequestNumber(18);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }
}
