package kma.cnpm.beapp.domain.user.service;

import kma.cnpm.beapp.domain.common.dto.AddressDTO;
import kma.cnpm.beapp.domain.common.dto.BankDTO;
import kma.cnpm.beapp.domain.common.dto.PageResponse;
import kma.cnpm.beapp.domain.common.enumType.RelationshipType;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.payment.service.AccountService;
import kma.cnpm.beapp.domain.post.service.PostService;
import kma.cnpm.beapp.domain.product.service.ProductService;
import kma.cnpm.beapp.domain.user.dto.response.PrivateUserDetailResponse;
import kma.cnpm.beapp.domain.user.dto.response.SearchUserResponse;
import kma.cnpm.beapp.domain.user.dto.response.UserDetailResponse;
import kma.cnpm.beapp.domain.user.entity.*;
import kma.cnpm.beapp.domain.user.repository.AddressRepository;
import kma.cnpm.beapp.domain.user.repository.FollowRepository;
import kma.cnpm.beapp.domain.user.repository.UserRepository;
import kma.cnpm.beapp.domain.user.repository.UserViewRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserReadService {
    final UserRepository userRepository;
    final AddressRepository addressRepository;
    final PostService postService;
    final ProductService productService;
    final AuthService authService;
    final FollowRepository followRepository;
    final UserRelationService userRelationService;
    final AccountService accountService;
    final UserViewRepository userViewRepository;

    // Helper function to retrieve a user by ID

    List<AddressDTO> buildAddressResponse(Long userId){
        // Fetch addresses associated with the user
        List<Address> addresses = addressRepository.findAddressByUserId(userId);

        // Convert addresses to a formatted string and set it to the response
        List<AddressDTO> addressStrings = addresses.stream()
                .map(address -> {
                    Province province = address.getProvince();
                    District district = address.getDistrict();
                    Ward ward = address.getWard();
                    String addressDetail = address.getAddressDetail();
                    String addressString =  addressDetail + ", " + ward.getName() + ", " + district.getName() + ", " + province.getName();
                    return AddressDTO.builder()
                            .addressId(address.getId())
                            .address(addressString)
                            .build();
                })
                .collect(Collectors.toList());

        return addressStrings;
    }

    private void handleUserViewProfile(User userTarget) {
        String authName = authService.authNameCanBeNull();

        Long userViewId = null;
        try {
            if (authName != null) {
                userViewId = Long.valueOf(authName);
            }
        } catch (NumberFormatException e) {
            log.error("Invalid user ID format: {}", authName);
            return;
        }

        if (userViewId != null && !userViewId.equals(userTarget.getId())) {
            User userView = getUserById(userViewId);
            userRelationService.createOrUpdateUserView(userView, userTarget);
        }
    }

    private User getUserById(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));
    }

    // Main function to get the profile of a user by ID
    public UserDetailResponse getProfileById(Long id) {

        User user = getUserById(id);
        handleUserViewProfile(user);

        UserDetailResponse response = UserDetailResponse.builder()
                .fullName(user.getFullName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .phone(user.getPhone())
                .avatar(user.getAvt())
                .build();


        response.setTotalPosts(postService.countPostOfUser(id));
        response.setTotalSoldProducts(productService.countSoldProductOfUser(id));
        response.setTotalFollowers(followRepository.countFollowerOfUser(id));
        response.setTotalFollowing(followRepository.countFollowingOfUser(id));

        return response;
    }



    public PrivateUserDetailResponse getPrivateProfile() {
        Long userId = Long.valueOf(authService.getAuthenticationName());
        User user = getUserById(userId);

        List<BankDTO> bankDTOS = accountService.getBanksOfUser(userId);
        List<AddressDTO> addressDTOS = buildAddressResponse(userId);
        BigDecimal balance = accountService.getBalance(userId);

        List<PrivateUserDetailResponse.UserViewResponse> userViewResponses = userViewRepository.findUserViewByUserTargetId(userId)
                .stream()
                .map(userView -> {
                    System.out.println(userView.getUpdatedAt());
                    return PrivateUserDetailResponse.UserViewResponse.builder()
                            .fullName(userView.getViewer().getFullName())
                            .viewTime(userView.getUpdatedAt())
                            .avatar(userView.getViewer().getAvt())
                            .build();
                }).collect(Collectors.toList());

        return PrivateUserDetailResponse.builder()
                .email(user.getEmail())
                .balance(balance)
                .userViews(userViewResponses)
                .addresses(addressDTOS)
                .bankResponses(bankDTOS)
                .build();


    }

    public PageResponse<List<SearchUserResponse>> searchByFullName(String fullName) {
        Long userId = Long.valueOf(authService.getAuthenticationName());
        User userSearch = getUserById(userId);
        List<User> users = userRepository.searchByFullName(fullName);

        List<SearchUserResponse> responses =  users.stream()
                .map(user -> {
                    RelationshipType type;
                    if (userId.equals(user.getId())) {
                        type = RelationshipType.YOU;
                    } else {
                        type = followRepository.getRelationshipTypeBetweenUser(userId, user.getId());
                    }
                    return SearchUserResponse.builder()
                            .idUser(user.getId())
                            .avatar(user.getAvt())
                            .fullName(user.getFullName())
                            .type(type)
                            .build();
                }).collect(Collectors.toList());

        int totalUsers = (int) userRepository.count();
        return PageResponse.<List<SearchUserResponse>>builder()
                .pageSize(totalUsers)
                .totalElements(totalUsers)
                .totalPages(1)
                .pageNo(0)
                .items(responses)
                .build();
    }
}
