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
        if (bankRepository.count() != 2) {
            // Xóa tất cả ngân hàng nếu số lượng không bằng 12
            bankRepository.deleteAll();

            // Tạo các đối tượng Bank
            Bank ncb = Bank.builder()
                    .bankCode("NCB")
                    .bankName("Ngân hàng Quốc Dân")
//                    .bankAvt()
                    .build();

            Bank mb = Bank.builder()
                    .bankCode("MB")
                    .bankName("Ngân hàng Quân Đội")
//                    .bankAvt()
                    .build();

//            Bank vcb = Bank.builder()
//                    .bankCode("VCB")
//                    .bankName("Ngân hàng Ngoại Thương Việt Nam")
//                    .build();
//
//            Bank bidv = Bank.builder()
//                    .bankCode("BIDV")
//                    .bankName("Ngân hàng Đầu Tư và Phát Triển Việt Nam")
//                    .build();
//
//            Bank tech = Bank.builder()
//                    .bankCode("Techcombank")
//                    .bankName("Ngân hàng Kỹ Thương Việt Nam")
//                    .build();
//
//            Bank acb = Bank.builder()
//                    .bankCode("ACB")
//                    .bankName("Ngân hàng Á Châu")
//                    .build();
//
//            Bank agribank = Bank.builder()
//                    .bankCode("Agribank")
//                    .bankName("Ngân hàng Nông Nghiệp và Phát Triển Nông Thôn Việt Nam")
//                    .build();
//
//            Bank sacombank = Bank.builder()
//                    .bankCode("Sacombank")
//                    .bankName("Ngân hàng Sài Gòn Thương Tín")
//                    .build();
//
//            Bank vpbank = Bank.builder()
//                    .bankCode("VPBank")
//                    .bankName("Ngân hàng Việt Nam Thịnh Vượng")
//                    .build();
//
//            Bank vietinbank = Bank.builder()
//                    .bankCode("Vietinbank")
//                    .bankName("Ngân hàng Công Thương Việt Nam")
//                    .build();
//
//            Bank eximbank = Bank.builder()
//                    .bankCode("Eximbank")
//                    .bankName("Ngân hàng Xuất Nhập Khẩu Việt Nam")
//                    .build();
//
//            Bank hdbank = Bank.builder()
//                    .bankCode("HDBank")
//                    .bankName("Ngân hàng Phát Triển TP. Hồ Chí Minh")
//                    .build();

            // Tạo danh sách và thêm các đối tượng Bank vào danh sách
            List<Bank> banks = new ArrayList<>();
            banks.add(ncb);
            banks.add(mb);
//            banks.add(vcb);
//            banks.add(bidv);
//            banks.add(tech);
//            banks.add(acb);
//            banks.add(agribank);
//            banks.add(sacombank);
//            banks.add(vpbank);
//            banks.add(vietinbank);
//            banks.add(eximbank);
//            banks.add(hdbank);

            // Lưu danh sách Bank vào cơ sở dữ liệu
            bankRepository.saveAll(banks);
        }
    }


}
