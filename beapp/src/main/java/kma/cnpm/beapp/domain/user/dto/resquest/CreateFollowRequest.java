package kma.cnpm.beapp.domain.user.dto.resquest;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateFollowRequest {
    @NotNull(message = "ID người dùng mục tiêu không được để trống")
    Long userIdTarget;
}
