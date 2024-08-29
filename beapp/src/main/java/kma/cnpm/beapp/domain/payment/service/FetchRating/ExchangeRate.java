package kma.cnpm.beapp.domain.payment.service.FetchRating;


public class ExchangeRate {
    private double usdToEur;
    private double usdToVnd;

    public ExchangeRate(double usdToEur, double usdToVnd) {
        this.usdToEur = usdToEur;
        this.usdToVnd = usdToVnd;
    }

    public double getUsdToEur() {
        return usdToEur;
    }

    public void setUsdToEur(double usdToEur) {
        this.usdToEur = usdToEur;
    }

    public double getUsdToVnd() {
        return usdToVnd;
    }

    public void setUsdToVnd(double usdToVnd) {
        this.usdToVnd = usdToVnd;
    }

    @Override
    public String toString() {
        return String.format("1 USD = %.2f EUR\n1 USD = %.2f VND", usdToEur, usdToVnd);
    }
}
