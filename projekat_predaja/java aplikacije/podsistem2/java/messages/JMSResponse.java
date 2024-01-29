package messages;

import java.io.Serializable;

public class JMSResponse implements Serializable{
    
    private boolean success;
    private String body;
    private int responseCode;
    
    public JMSResponse(){}

    public JMSResponse(boolean success, String body, int responseCode) {
        this.success = success;
        this.body = body;
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
