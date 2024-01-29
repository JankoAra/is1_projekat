package endpoints;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import messages.JMSMessages;
import messages.JMSResponse;
import requests.Request;

@Path("kategorija")
public class KategorijaEndpoint {


    @POST
    @Path("create")
    public Response napraviKategoriju(@FormParam("Naziv kategorije") String naziv) {
        Request request = new Request();
        request.addParameter("Naziv kategorije", naziv);
        request.setRequestNumber(5);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @GET
    @Path("sve")
    public Response dohvatiSveKategorije() {
        Request request = new Request();
        request.setRequestNumber(19);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }
}
