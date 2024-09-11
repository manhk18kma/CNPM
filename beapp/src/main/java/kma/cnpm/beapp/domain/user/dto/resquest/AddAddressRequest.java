package kma.cnpm.beapp.domain.user.dto.resquest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import kma.cnpm.beapp.domain.common.enumType.AddressType;
import kma.cnpm.beapp.domain.common.validation.EnumValue;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddAddressRequest {

    @Size(min = 5, max = 100, message = "Chi tiết địa chỉ phải có độ dài từ 5 đến 100 ký tự")
    private String addressDetail;

    @EnumValue(name = "AddressType", enumClass = AddressType.class, message = "Loại địa chỉ phải là một giá trị hợp lệ")
    private String addressType;

    @NotBlank(message = "ID phường/xã không được để trống")
    private String wardId;

    @NotBlank(message = "ID quận/huyện không được để trống")
    private String districtId;

    @NotBlank(message = "ID tỉnh/thành phố không được để trống")
    private String provinceId;
}