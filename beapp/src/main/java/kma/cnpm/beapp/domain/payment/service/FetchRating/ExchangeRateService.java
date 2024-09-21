package kma.cnpm.beapp.domain.payment.service.FetchRating;
import kma.cnpm.beapp.domain.common.upload.ImageService;
import kma.cnpm.beapp.domain.payment.entity.Bank;
import kma.cnpm.beapp.domain.payment.entity.PaymentGateway;
import kma.cnpm.beapp.domain.payment.repository.BankRepository;
import kma.cnpm.beapp.domain.payment.repository.PaymentGatewayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService implements CommandLineRunner {

    private final ApiClientRate apiClient;
    private final BankRepository bankRepository;
    private final PaymentGatewayRepository paymentGatewayRepository;

    public ExchangeRate getExchangeRates() {
        String url = "https://api.exchangerate-api.com/v4/latest/USD";

        // Fetch data from API
        ApiResponse response = apiClient.fetchData(url);

        // Extract rates
        BigDecimal usdToEur = (BigDecimal) response.getRates().get("EUR");
        BigDecimal usdToVnd = (BigDecimal) response.getRates().get("VND");

        // Return ExchangeRates object
        return new ExchangeRate(usdToEur, usdToVnd);
    }


    @Override
    public void run(String... args) throws Exception {
        if (paymentGatewayRepository.count()!=2){
            paymentGatewayRepository.deleteAll();

            PaymentGateway vnpay = PaymentGateway.builder()
                    .name("VnPay")
                    .paymentGatewayAvt("")
                    .build();

            PaymentGateway paypal = PaymentGateway.builder()
                    .name("Paypal")
                    .paymentGatewayAvt("")
                    .build();
            paymentGatewayRepository.save(vnpay);
            paymentGatewayRepository.save(paypal);

        }


        // Kiểm tra số lượng ngân hàng hiện có trong cơ sở dữ liệu
        if (bankRepository.count() != 4) {
            bankRepository.deleteAll();
            Bank ncb = Bank.builder()
                    .bankCode("NCB")
                    .bankName("Ngân hàng Quốc Dân")
                    .bankAvt("https://tse1.mm.bing.net/th?id=OIP.VGBudb8POsvkBFID6vX6swHaCl&pid=Api&P=0&h=220")
                    .build();

            Bank mb = Bank.builder()
                    .bankCode("MB")
                    .bankName("Ngân hàng Quân Đội")
                    .bankAvt("https://tse1.mm.bing.net/th?id=OIP.3MJ8rPFa2oVl383lKz7mSgHaEO&pid=Api&P=0&h=220")
                    .build();

            Bank vcb = Bank.builder()
                    .bankCode("VCB")
                    .bankName("Ngân hàng Ngoại Thương Việt Nam")
                    .bankAvt("https://tse2.mm.bing.net/th?id=OIP.g6sI-yqchxDoF83iGVcr0QAAAA&pid=Api&P=0&h=220")
                    .build();

            Bank bidv = Bank.builder()
                    .bankCode("BIDV")
                    .bankName("Ngân hàng Đầu Tư và Phát Triển Việt Nam")
                    .bankAvt("https://tse1.mm.bing.net/th?id=OIP.PtWsivDxUrqOwB3TbLak9wHaD3&pid=Api&P=0&h=220")
                    .build();

            List<Bank> banks = new ArrayList<>();
            banks.add(ncb);
            banks.add(mb);
            banks.add(vcb);
            banks.add(bidv);
            bankRepository.saveAll(banks);
        }
    }


}
