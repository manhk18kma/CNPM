package kma.cnpm.beapp.domain.payment.service.FetchRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {

    private final ApiClientRate apiClient;

    @Autowired
    public ExchangeRateService(ApiClientRate apiClient) {
        this.apiClient = apiClient;
    }

    public ExchangeRate getExchangeRates() {
        String url = "https://api.exchangerate-api.com/v4/latest/USD";

        // Fetch data from API
        ApiResponse response = apiClient.fetchData(url);

        // Extract rates
        double usdToEur = (double) response.getRates().get("EUR");
        double usdToVnd = (double) response.getRates().get("VND");

        // Return ExchangeRates object
        return new ExchangeRate(usdToEur, usdToVnd);
    }
}
