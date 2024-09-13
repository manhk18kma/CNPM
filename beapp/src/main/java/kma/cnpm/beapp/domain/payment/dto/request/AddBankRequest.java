package kma.cnpm.beapp.domain.payment.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddBankRequest {

    @NotNull(message = "ID ngân hàng không được để trống")
    private Long bankId;

    @NotBlank(message = "Số tài khoản không được để trống")
    private String accountNumber;

}
