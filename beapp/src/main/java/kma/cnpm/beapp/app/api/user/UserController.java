package kma.cnpm.beapp.app.api.user;

import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kma.cnwat.be.domain.common.dto.ResponseData;
import kma.cnwat.be.domain.user.dto.response.TokenResponse;
import kma.cnwat.be.domain.user.dto.response.UserResponse;
import kma.cnwat.be.domain.user.dto.resquest.ActiveUserRequest;
import kma.cnwat.be.domain.user.dto.resquest.AddAddressRequest;
import kma.cnwat.be.domain.user.dto.resquest.CreateUserRequest;
import kma.cnwat.be.domain.user.dto.resquest.UpdateUserRequest;
import kma.cnwat.be.domain.user.service.AddressService;
import kma.cnwat.be.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

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

    @Operation(summary = "Create new user", description = "Register a new user account with provided details.")
    @PostMapping
    public ResponseData<UserResponse> createUser(
            @Parameter(description = "User creation request payload with user details", required = true)
            @RequestBody @Valid CreateUserRequest request) throws ParseException, JOSEException {
        UserResponse response = userService.saveUser(request);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "User created successfully, check your email to activate",
                new Date(),
                response);
    }

    @Operation(summary = "Activate user", description = "Activate a user account using an activation token sent to the user’s email.")
    @PostMapping("/activate")
    public ResponseData<TokenResponse> activateUser(
            @Parameter(description = "User activation request payload containing activation token", required = true)
            @RequestBody @Valid ActiveUserRequest request) throws ParseException, JOSEException {
        TokenResponse response = userService.activateUser(request);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "User activated successfully",
                new Date(),
                response);
    }

    @Operation(summary = "Update user", description = "Update details of an existing user account , call api with BEARER TOKEN")
    @PutMapping
    public ResponseData<UserResponse> updateUser(
            @Parameter(description = "User update request payload containing updated user details", required = true)
            @RequestBody @Valid UpdateUserRequest request) throws ParseException {
        UserResponse response = userService.updateUser(request);
        return new ResponseData<>(HttpStatus.OK.value(),
                "User updated successfully",
                new Date(),
                response);
    }

    @Operation(summary = "Add address to user", description = "Add a new address for the user. call api with BEARER TOKEN")
    @PostMapping("/addresses")
    public ResponseData<UserResponse> addAddress(
            @Parameter(description = "Add address request payload containing address details", required = true)
            @RequestBody @Valid AddAddressRequest request) {
        UserResponse response = addressService.addAddressToUser(request);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Address added successfully",
                new Date(),
                response);
    }
}
