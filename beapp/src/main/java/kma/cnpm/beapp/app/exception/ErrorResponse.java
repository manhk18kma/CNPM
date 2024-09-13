package kma.cnpm.beapp.app.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

}
