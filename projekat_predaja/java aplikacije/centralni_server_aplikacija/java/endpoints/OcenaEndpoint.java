package endpoints;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import messages.JMSMessages;
import messages.JMSResponse;
import requests.Request;

@Path("ocena")
public class OcenaEndpoint {

    @POST
    @Path("create")
    public Response dodajOcenu(@FormParam("Email korisnika") String email, @FormParam("ID video snimka") String idVid, @FormParam("Ocena") String ocena) {
        Request request = new Request();
        request.addParameter("ID video snimka", idVid);
        request.addParameter("Email korisnika", email);
        request.addParameter("Ocena", ocena);
        request.setRequestNumber(13);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @PUT
    @Path("promeni")
    public Response promeniOcenu(@FormParam("Email korisnika") String email, @FormParam("ID video snimka") String idVid, @FormParam("Ocena") String ocena) {
        Request request = new Request();
        request.addParameter("ID video snimka", idVid);
        request.addParameter("Email korisnika", email);
        request.addParameter("Ocena", ocena);
        request.setRequestNumber(14);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @DELETE
    @Path("brisanje")
    public Response obrisiOcenu(@QueryParam("Email korisnika") String email, @QueryParam("ID video snimka") String idVid) {
        Request request = new Request();
        request.addParameter("ID video snimka", idVid);
        request.addParameter("Email korisnika", email);
        request.setRequestNumber(15);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @GET
    public Response dohvatiOceneZaSnimak(@QueryParam("ID video snimka") String idVid) {
        Request request = new Request();
        request.addParameter("ID video snimka", idVid);
        request.setRequestNumber(25);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }
}
