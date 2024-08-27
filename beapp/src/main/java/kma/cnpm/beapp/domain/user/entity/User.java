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

    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

}
