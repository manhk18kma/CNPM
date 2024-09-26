package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipperDTO implements Serializable {
    String tokenDevice; // Khớp với `u.tokenDevice`
    Long id; // Khớp với `u.id`
}
