package kma.cnpm.beapp.domain.user.dto.resquest;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kma.cnpm.beapp.domain.common.enumType.Gender;
import kma.cnpm.beapp.domain.common.enumType.UserStatus;
import kma.cnpm.beapp.domain.common.validation.DateAnnotationCus;
import kma.cnpm.beapp.domain.common.validation.EnumValue;
import kma.cnpm.beapp.domain.common.validation.PhoneNumber;
import kma.cnpm.beapp.domain.user.entity.Address;
import lombok.Getter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
public class UpdateUserRequest {
    @NotBlank(message = "Họ tên là bắt buộc")
    @Size(min = 2, max = 50, message = "Họ tên phải có độ dài từ 2 đến 50 ký tự")
    private String fullName;

    @DateAnnotationCus(format = "yyyy-MM-dd", message = "Ngày sinh phải có định dạng yyyy-MM-dd và nằm trong khoảng từ năm 1930 đến hiện tại")
    private String dateOfBirth;

    @EnumValue(name = "Giới tính", enumClass = Gender.class, message = "Giới tính phải là giá trị hợp lệ")
    private String gender;

    @PhoneNumber(message = "Số điện thoại phải đúng định dạng")
    private String phone;

    // String base 64 nếu là tạo mới, cái cũ thì cứ gửi URL lên bình thường
    private String urlAvtOrBase64;
}
