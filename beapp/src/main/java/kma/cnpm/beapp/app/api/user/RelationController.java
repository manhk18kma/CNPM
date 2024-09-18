package kma.cnpm.beapp.app.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.common.enumType.FollowType;
import kma.cnpm.beapp.domain.common.enumType.TransactionType;
import kma.cnpm.beapp.domain.common.validation.EnumValue;
import kma.cnpm.beapp.domain.user.dto.response.FollowResponse;
import kma.cnpm.beapp.domain.user.dto.response.UserResponse;
import kma.cnpm.beapp.domain.user.dto.resquest.CreateFollowRequest;
import kma.cnpm.beapp.domain.user.service.UserRelationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/follows")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Relations Controller", description = "APIs for user management")
public class RelationController {

    UserRelationService userRelationService;

    @Operation(summary = "Create a follow relationship", description = "Create a new follow relationship between users. Call this API with BEARER TOKEN for authentication.")
    @PostMapping
    public ResponseData<?> createFollow(
            @Parameter(description = "Request payload to create a follow relationship", required = true)
            @RequestBody @Valid CreateFollowRequest request) {
        UserResponse response =  userRelationService.createFollow(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Tạo mối quan hệ theo dõi thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Remove a follow relationship", description = "Remove an existing follow relationship. Call this API with BEARER TOKEN for authentication.")
    @DeleteMapping("/{idFollow}")
    public ResponseData<?> removeFollow(
            @Parameter(description = "ID of the follow relationship to be removed", required = true)
            @PathVariable @NotNull Long idFollow) {
        UserResponse response = userRelationService.removeFollow(idFollow);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Xóa mối quan hệ theo dõi thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Get a list of follows", description = "Retrieve a list of follows based on the provided user ID and type (followers or following).")
    @GetMapping
    public ResponseData<List<FollowResponse>> getFollows(
            @Parameter(description = "ID of the user whose follows are being retrieved", required = true)
            @RequestParam @NotNull Long userId,
            @Parameter(description = "Type of follows to retrieve, can be 'followers' or 'following'", required = true)
            @RequestParam(defaultValue = "FOLLOWER") @EnumValue(name = "type", enumClass = FollowType.class) String type) {
        List<FollowResponse> responses = userRelationService.getFollows(userId, type);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Danh sách theo dõi được lấy thành công",
                LocalDateTime.now(),
                responses
        );
    }
}
