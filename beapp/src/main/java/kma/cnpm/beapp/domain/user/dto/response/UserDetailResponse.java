package kma.cnpm.beapp.domain.user.dto.response;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.Gender;
import kma.cnpm.beapp.domain.common.enumType.UserStatus;
import kma.cnpm.beapp.domain.user.entity.Address;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
