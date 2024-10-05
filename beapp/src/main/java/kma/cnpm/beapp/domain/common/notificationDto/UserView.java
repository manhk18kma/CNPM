package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserView {
    String userViewFullName;
    String userViewAvt;
    Long userViewId;
    Long userViewedId;
    int totalOtherViews;
}
