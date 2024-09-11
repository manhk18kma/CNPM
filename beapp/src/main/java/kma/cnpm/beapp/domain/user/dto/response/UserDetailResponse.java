package kma.cnpm.beapp.domain.user.dto.response;
import kma.cnpm.beapp.domain.common.enumType.Gender;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class UserDetailResponse {

    private String fullName;
    private Date dateOfBirth;
    private Gender gender;
    private String phone;
    private String avatar;

    private int totalPosts;
    private int totalSoldProducts;
    private int totalFollowers;
    private int totalFollowing;
}
