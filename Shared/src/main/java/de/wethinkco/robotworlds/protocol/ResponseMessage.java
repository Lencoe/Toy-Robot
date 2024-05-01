package de.wethinkco.robotworlds.protocol;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.util.HashMap;
import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "result"
)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = SuccessResponseMessage.class, name = "success"),
//        @JsonSubTypes.Type(value = ErrorResponseMessage.class, name = "error"),
//
//})

@JsonSubTypes({
        @JsonSubTypes.Type(value = SuccessResponseMessage.class, name = "OK"),
        @JsonSubTypes.Type(value = ErrorResponseMessage.class, name = "ERROR"),

})

public class ResponseMessage {

    public ResponseMessage(){

    }

    public ResponseMessage(String result, Map<String, Object> data ){
        this.result = result;
        this.data = data;
    }

    private String result;
    private Map<String, Object> data = new HashMap<>();

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public void setAdditionalData(String name, Object value) {
        this.data.put(name, value);
    }

}
