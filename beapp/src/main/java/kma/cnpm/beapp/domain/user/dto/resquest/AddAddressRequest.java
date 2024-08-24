package kma.cnpm.beapp.domain.user.dto.resquest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kma.cnwat.be.domain.common.enumType.AddressType;
import kma.cnwat.be.domain.common.enumType.Gender;
import kma.cnwat.be.domain.common.validation.EnumValue;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddAddressRequest {

    @Size(min = 5, max = 100, message = "Address detail must be between 5 and 100 characters")
    private String addressDetail;

    @EnumValue(name = "AddressType", enumClass = AddressType.class, message = "Type must be a valid AddressType")
    private String addressType;

    @NotBlank(message = "Ward ID cannot be null")
    private String wardId;

    @NotBlank(message = "District ID cannot be null")
    private String districtId;

    @NotBlank(message = "Province ID cannot be null")
    private String provinceId;
}
