package endpoints;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import messages.JMSMessages;
import messages.JMSResponse;
import requests.Request;

@Path("mesto")
public class MestoEndpoint {

    @POST
    @Path("create")
    public Response napraviMesto(@FormParam("Naziv grada") String naziv) {
        Request request = new Request();
        request.addParameter("Naziv grada", naziv);
        request.setRequestNumber(1);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }

    @GET
    @Path("sve")
    public Response dohvatiSvaMesta() {
        Request request = new Request();
        request.setRequestNumber(17);
        JMSMessages temp = new JMSMessages();
        JMSResponse response = temp.sendRequest(request);
        return Response.status(response.getResponseCode()).entity(response.getBody()).build();
    }
}
