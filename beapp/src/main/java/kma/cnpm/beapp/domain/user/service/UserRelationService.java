package kma.cnpm.beapp.domain.user.service;

import kma.cnpm.beapp.domain.common.dto.UserDTO;
import kma.cnpm.beapp.domain.common.enumType.FollowType;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.common.notificationDto.ShipperDTO;
import kma.cnpm.beapp.domain.user.dto.response.FollowResponse;
import kma.cnpm.beapp.domain.user.dto.response.UserResponse;
import kma.cnpm.beapp.domain.user.dto.resquest.CreateFollowRequest;
import kma.cnpm.beapp.domain.user.entity.Follow;
import kma.cnpm.beapp.domain.user.entity.User;
import kma.cnpm.beapp.domain.user.entity.UserView;
import kma.cnpm.beapp.domain.user.repository.FollowRepository;
import kma.cnpm.beapp.domain.user.repository.UserRepository;
import kma.cnpm.beapp.domain.user.repository.UserViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRelationService {

    private final FollowRepository followRepository;
    private final UserViewRepository userViewRepository;
    private final AuthService authService;
    private final UserRepository userRepository;


    @Transactional
    public UserResponse createFollow(CreateFollowRequest request) {
        Long userId = Long.valueOf(authService.getAuthenticationName());
        User user = getUserById(userId);
        User targetUser = getUserById(request.getUserIdTarget());

        validateSelfFollow(userId, targetUser.getId());
        checkIfAlreadyFollowing(userId, targetUser.getId());

        Follow follow = buildFollow(user, targetUser);
        followRepository.save(follow);
        return UserResponse.builder()
                .userId(userId)
                .userTargetId(targetUser.getId())
                .build();
    }
    @Transactional
    public UserResponse removeFollow(Long idFollow) {
        Long userId = Long.valueOf(authService.getAuthenticationName());
        User user = getUserById(userId);
        Follow follow = findFollowById(idFollow);
        validateOwnership(follow, userId);
        followRepository.delete(follow);
        return UserResponse.builder()
                .userId(userId)
                .userTargetId(follow.getFollowed().getId())
                .build();
    }


    @Transactional
    public void createOrUpdateUserView(User userViewer, User userTarget) {
        UserView existingUserView = userViewRepository.findByUserViewIdAndTargetId(userViewer.getId(), userTarget.getId()).orElse(null);

        if (existingUserView == null) {
            UserView newUserView = UserView.builder()
                    .viewer(userViewer)
                    .viewed(userTarget)
                    .viewCount(1)
                    .build();
            userViewRepository.save(newUserView);
        } else {
            existingUserView.setViewCount(existingUserView.getViewCount() + 1);
            userViewRepository.save(existingUserView);
        }
    }

    public List<FollowResponse> getFollows(Long userId, String type) {
        List<Follow> follows = new ArrayList<>();

        if (type.equals(FollowType.FOLLOWER)) {
            follows = followRepository.getFollowersOfUser(userId);
            return follows.stream().map(follow -> {
                return FollowResponse.builder()
                        .followId(follow.getId())
                        .userId(follow.getFollower().getId()) // Người theo dõi
                        .fullName(follow.getFollower().getFullName())
                        .avatar(follow.getFollower().getAvt())
                        .followerUserId(follow.getFollower().getId())
                        .followedUserId(userId)
                        .build();
            }).collect(Collectors.toList());

        } else if (type.equals(FollowType.FOLLOWING)) {
            follows = followRepository.getFollowingOfUser(userId); // Đúng hàm để lấy following
            return follows.stream().map(follow -> {
                return FollowResponse.builder()
                        .followId(follow.getId())
                        .userId(follow.getFollowed().getId()) // Người được theo dõi
                        .fullName(follow.getFollowed().getFullName())
                        .avatar(follow.getFollowed().getAvt())
                        .followerUserId(userId)
                        .followedUserId(follow.getFollowed().getId())
                        .build();
            }).collect(Collectors.toList());

        } else {
            throw new AppException(AppErrorCode.INVALID_PARAM);
        }
    }



    //helper functions
    private Follow findFollowById(Long idFollow) {
        return followRepository.findById(idFollow)
                .orElseThrow(() -> new AppException(AppErrorCode.FOLLOW_NOT_EXIST));
    }
    private void validateOwnership(Follow follow, Long userId) {
        if (!follow.getFollower().getId().equals(userId)) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }
    }
    private void validateSelfFollow(Long userId, Long targetUserId) {
        if (userId.equals(targetUserId)) {
            throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    private void checkIfAlreadyFollowing(Long userId, Long targetUserId) {
        boolean alreadyFollowing = followRepository.existsFollow(userId, targetUserId);
        if (alreadyFollowing) {
            throw new AppException(AppErrorCode.USER_ALREADY_FOLLOWED);
        }
    }
    private Follow buildFollow(User follower, User followed) {
        return Follow.builder()
                .follower(follower)
                .followed(followed)
                .build();
    }
    private User getUserById(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));
    }

    public String getTokenDeviceByUserId(Long userId){
        return userRepository.getTokenDeviceByUserId(userId);
    }

    public List<ShipperDTO> getTokenDeviceShipper() {
        return userRepository.getTokenDeviceShipper().stream()
                .map(user -> {
                    return ShipperDTO.builder()
                            .tokenDevice(user.getTokenDevice())
                            .id(user.getId())
                            .build();
                }).collect(Collectors.toList());
    }
    public UserDTO getUserInfo(Long id){
        User user = userRepository.findUserById((id))
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));
        return UserDTO.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .build();
    }


    public List<UserDTO> getFollowersOfUser(Long userId) {
        List<User> followers = followRepository.getUserFollowersOfUser(userId);
        return followers.stream()
                .map(user->{
                    return UserDTO.builder()
                            .userId(user.getId())
                            .tokenDevice(user.getTokenDevice())
                            .build();
                }).collect(Collectors.toList());
    }
}
