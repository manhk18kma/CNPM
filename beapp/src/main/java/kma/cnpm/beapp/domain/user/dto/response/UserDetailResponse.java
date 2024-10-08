package kma.cnpm.beapp.domain.user.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kma.cnpm.beapp.domain.common.enumType.Gender;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class UserDetailResponse {
    Long userId;
    private String fullName;
    private Date dateOfBirth;
    private Gender gender;
    private String phone;
    private String avatar;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isFollower;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long idFollow; //if exists
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isFollowing;

    private int totalPosts;
    private int totalSoldProducts;
    private int totalFollowers;
    private int totalFollowing;
}
