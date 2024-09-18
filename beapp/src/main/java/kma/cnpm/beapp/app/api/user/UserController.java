package kma.cnpm.beapp.app.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import kma.cnpm.beapp.domain.common.dto.PageResponse;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.user.dto.response.*;
import kma.cnpm.beapp.domain.user.dto.resquest.ActiveUserRequest;
import kma.cnpm.beapp.domain.user.dto.resquest.AddAddressRequest;
import kma.cnpm.beapp.domain.user.dto.resquest.CreateUserRequest;
import kma.cnpm.beapp.domain.user.dto.resquest.UpdateUserRequest;
import kma.cnpm.beapp.domain.user.service.AddressService;
import kma.cnpm.beapp.domain.user.service.UserReadService;
import kma.cnpm.beapp.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "APIs for user management")
public class UserController {

    UserService userService;
    AddressService addressService;
    UserReadService userReadService;

    @Operation(summary = "Create new user",
            description = "Register a new user account with provided details.")
    @PostMapping
    public ResponseData<?> createUser(
            @Parameter(description = "User creation request payload with user details", required = true)
            @RequestBody @Valid CreateUserRequest request) {
        UserResponse response = userService.saveUser(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Người dùng đã được tạo thành công, kiểm tra email của bạn để kích hoạt",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Activate user",
            description = "Activate a user account using an activation token sent to the user’s email.")
    @PostMapping("/activate")
    public ResponseData<TokenResponse> activateUser(
            @Parameter(description = "User activation request payload containing activation token", required = true)
            @RequestBody @Valid ActiveUserRequest request) {
        TokenResponse response = userService.activateUser(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Người dùng đã được kích hoạt thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Update user",
            description = "Update details of an existing user account. Call this API with BEARER TOKEN for authentication.")
    @PutMapping
    public ResponseData<?> updateUser(
            @Parameter(description = "User update request payload containing updated user details", required = true)
            @RequestBody @Valid UpdateUserRequest request) throws ParseException {
        UserResponse response = userService.updateUser(request);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Người dùng đã được cập nhật thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Add address to user",
            description = "Add a new address for the user. Call this API with BEARER TOKEN for authentication.")
    @PostMapping("/addresses")
    public ResponseData<?> addAddress(
            @Parameter(description = "Add address request payload containing address details", required = true)
            @RequestBody @Valid AddAddressRequest request) {
        UserResponse response =  addressService.addAddressToUser(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Địa chỉ đã được thêm thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Get user profile by ID",
            description = "Retrieve details of a user profile by user ID.")
    @GetMapping
    public ResponseData<UserDetailResponse> getProfileById(
            @Parameter(description = "User ID to retrieve profile details", required = true)
            @RequestParam(value = "userId") @NotNull Long userId) {
        UserDetailResponse response = userReadService.getProfileById(userId);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Hồ sơ người dùng đã được lấy thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Get private user profile",
            description = "Retrieve details of the currently authenticated user's private profile. Call API with BEARER TOKEN.")
    @GetMapping("/private-info")
    public ResponseData<PrivateUserDetailResponse> getPrivateProfile() {
        PrivateUserDetailResponse response = userReadService.getPrivateProfile();
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Hồ sơ cá nhân đã được lấy thành công",
                LocalDateTime.now(),
                response
        );
    }

    @GetMapping("/search")
    @Operation(summary = "Search profiles by name", description = "Returns a paginated list of profiles matching the specified name.")
    public ResponseData<PageResponse<List<SearchUserResponse>>> searchByName(
            @Parameter(description = "Name to search for", required = true)
            @RequestParam String fullName) {
        PageResponse<List<SearchUserResponse>> response = userReadService.searchByFullName(fullName);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Danh sách tìm kiếm được trả về thành công",
                LocalDateTime.now(),
                response
        );
    }
}