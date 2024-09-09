package kma.cnpm.beapp.domain.user.entity;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.Gender;
import kma.cnpm.beapp.domain.common.enumType.UserStatus;

import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_user")
public class User extends AbstractEntity<Long> {

    @Column(name = "password",  length = 255)
    private String password;

    @Column(name = "email",  length = 100)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @Column(name = "token_device")
    private String tokenDevice;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
//    private Set<TokenDevice> tokenDevices;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Address> addresses = new HashSet<>();

    @Column(name = "url_avt")
    private String avt;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> following = new HashSet<>(); // Những người mà mình đang theo dõi

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followers = new HashSet<>(); // Những người theo dõi mình

    // Danh sách người dùng đã vào xem trang của user này
    @OneToMany(mappedBy = "viewed", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserView> viewers;

    // Danh sách user mà người này đã vào xem
    @OneToMany(mappedBy = "viewer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserView> viewedUsers;

    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void addFollowing(Follow follow) {
        if (following == null) {
            following = new HashSet<>();
        }
        following.add(follow);
        follow.setFollower(this);
    }

    public void addFollowers(Follow follow) {
        if (followers == null) {
            followers = new HashSet<>();
        }
        followers.add(follow);
        follow.setFollowed(this);
    }


}
