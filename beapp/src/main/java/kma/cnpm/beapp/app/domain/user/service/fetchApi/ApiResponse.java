package kma.cnwat.be.domain.user.service.fetchApi;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
public class ApiResponse<T> {
    private int error;
    private String error_text;
    private String data_name;
    private List<T> data;

    // Getters and Setters
}
