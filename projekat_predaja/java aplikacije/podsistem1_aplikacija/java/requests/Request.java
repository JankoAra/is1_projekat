package requests;

import java.io.Serializable;
import java.util.HashMap;

public class Request implements Serializable{
    private String method;
    private HashMap<String, String> parameters = new HashMap<>();
    private String url;
    private int requestNumber;
    private String origin;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(int requestNumber) {
        this.requestNumber = requestNumber;
    }
    
    public void addParameter(String key, String value){
        parameters.put(key, value);
    }
}
