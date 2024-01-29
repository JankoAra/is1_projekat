package http;

public class HttpResponse {

    private int responseCode;
    private String responseBody;
    private String responseMesasge;
    
    public HttpResponse(){}

    public HttpResponse(int responseCode, String responseBody, String responseMesasge) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.responseMesasge = responseMesasge;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseMesasge() {
        return responseMesasge;
    }

    public void setResponseMesasge(String responseMesasge) {
        this.responseMesasge = responseMesasge;
    }

    @Override
    public String toString() {
        return "RequestResponse{" + "responseCode=" + responseCode + ",\nresponseBody=" + responseBody + ",\nresponseMesasge=" + responseMesasge + '}';
    }
}
