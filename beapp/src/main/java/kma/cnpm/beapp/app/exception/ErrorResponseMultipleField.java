package kma.cnpm.beapp.app.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ErrorResponseMultipleField {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private Map<String , String> message;
    private String path;
}
