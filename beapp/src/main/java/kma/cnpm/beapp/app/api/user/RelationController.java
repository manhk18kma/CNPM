package kma.cnpm.beapp.app.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.user.dto.response.FollowResponse;
import kma.cnpm.beapp.domain.user.dto.resquest.CreateFollowRequest;
import kma.cnpm.beapp.domain.user.service.UserRelationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Create a follow relationship", description = "Create a new follow relationship between users.")
    @PostMapping
    public ResponseData<?> createFollow(
            @Parameter(description = "Request payload to create a follow relationship", required = true)
            @RequestBody @Valid CreateFollowRequest request) {
        userRelationService.createFollow(request);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Follow relationship created successfully",
                new Date());
    }

    @Operation(summary = "Remove a follow relationship", description = "Remove an existing follow relationship.")
    @DeleteMapping("/{idFollow}")
    public ResponseData<?> removeFollow(
            @Parameter(description = "ID of the follow relationship to be removed", required = true)
            @PathVariable Long idFollow) {
        userRelationService.removeFollow(idFollow);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Follow relationship removed successfully",
                new Date());
    }

    @Operation(summary = "Get a list of follows", description = "Retrieve a list of follows based on the provided user ID and type (followers or following).")
    @GetMapping
    public ResponseData<List<FollowResponse>> getFollows(
            @Parameter(description = "ID of the user whose follows are being retrieved", required = true)
            @RequestParam Long id,
            @Parameter(description = "Type of follows to retrieve, can be 'followers' or 'following'", required = true)
            @RequestParam String type) {
        List<FollowResponse> responses = userRelationService.getFollows(id, type);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Follows retrieved successfully",
                new Date(),
                responses);
    }
}
