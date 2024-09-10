package kma.cnpm.beapp.domain.user.dto.response;

import kma.cnpm.beapp.domain.common.dto.AddressDTO;
import kma.cnpm.beapp.domain.common.dto.BankDTO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class PrivateUserDetailResponse {
    private String email;
    private BigDecimal balance;
    private List<AddressDTO> addresses;
    private List<BankDTO> bankResponses;
    private List<UserViewResponse> userViews;

    @Data
    @Builder
    public static class UserViewResponse {
        private String fullName;
        private LocalDateTime viewTime;
        private String avatar;
    }
}
