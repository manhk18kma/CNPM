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
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    private String fullName;

    @DateAnnotationCus(format = "yyyy-MM-dd", message = "Date of birth must be in the format yyyy-MM-dd and be between 1930 and today")
    private String dateOfBirth;

    @EnumValue(name = "type", enumClass = Gender.class, message = "Type must be a valid GenderType")
    private String gender;

    @PhoneNumber(message = "Phone number must be in a valid format")
    private String phone;


}
