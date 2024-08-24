package kma.cnwat.be.app.exception;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ErrorResponseMultipleField {
    private Date timestamp;
    private int status;
    private String error;
    private Map<String , String> message;
    private String path;
}
