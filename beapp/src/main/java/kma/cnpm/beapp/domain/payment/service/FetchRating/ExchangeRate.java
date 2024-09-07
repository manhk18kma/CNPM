package kma.cnpm.beapp.domain.payment.service.FetchRating;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class ExchangeRate {
    private BigDecimal usdToEur;
    private BigDecimal usdToVnd;

    @Override
    public String toString() {
        return String.format("1 USD = %.2f EUR\n1 USD = %.2f VND", usdToEur, usdToVnd);
    }
}
