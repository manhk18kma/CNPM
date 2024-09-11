package kma.cnpm.beapp.domain.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddressDTO {
    Long addressId;
    String address;
}
