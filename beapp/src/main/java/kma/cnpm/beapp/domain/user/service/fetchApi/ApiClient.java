package kma.cnpm.beapp.domain.user.service.fetchApi;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class ApiClient {

    private final RestTemplate restTemplate;

    public ApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ApiResponse<T> fetchData(String url, ParameterizedTypeReference<ApiResponse<T>> responseType) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();
        ResponseEntity<ApiResponse<T>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, responseType);
        return responseEntity.getBody();
    }
}