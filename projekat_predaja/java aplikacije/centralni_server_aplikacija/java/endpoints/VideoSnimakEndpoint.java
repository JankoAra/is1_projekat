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

@Path("video")
public class VideoSnimakEndpoint {

    @POST
    @Path("create")
    public Response napraviVideoSnimak(@FormParam("Naziv video snimka") String naziv, @FormParam("Trajanje") String trajanje,
            @FormParam("Email korisnika") String emailVlasnika) {
        Request request = new Request();
        request.addParameter("Naziv video snimka", naziv);
        request.addParameter("Trajanje", trajanje);
        request.addParameter("Email korisnika", emailVlasnika);
        request.setRequestNumber(6);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @PUT
    @Path("promeniNaziv")
    public Response promeniNazivSnimka(@FormParam("ID video snimka") String idVid, @FormParam("Novi naziv video snimka") String noviNaziv) {
        Request request = new Request();
        request.addParameter("ID video snimka", idVid);
        request.addParameter("Novi naziv video snimka", noviNaziv);
        request.setRequestNumber(7);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @PUT
    @Path("dodajKategoriju")
    public Response dodajKategoriju(@FormParam("ID video snimka") String idVid, @FormParam("Naziv kategorije") String nazivKategorije) {
        Request request = new Request();
        request.addParameter("ID video snimka", idVid);
        request.addParameter("Naziv kategorije", nazivKategorije);
        request.setRequestNumber(8);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @DELETE
    @Path("brisanje")
    public Response obrisiVideoSnimak(@QueryParam("ID video snimka") String idVid, @QueryParam("Email korisnika") String emailKorisnika) {
        Request request = new Request();
        request.addParameter("ID video snimka", idVid);
        request.addParameter("Email korisnika", emailKorisnika);
        request.setRequestNumber(16);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @GET
    @Path("sve")
    public Response dohvatiSveVideoSnimke() {
        Request request = new Request();
        request.setRequestNumber(20);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @GET
    @Path("kategorije")
    public Response dohvatiKategorijeZaSnimak(@QueryParam("ID video snimka") String idVid) {
        Request request = new Request();
        request.addParameter("ID video snimka", idVid);
        request.setRequestNumber(21);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    
}
