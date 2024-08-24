package kma.cnpm.beapp.domain.user.repository;

import kma.cnwat.be.domain.common.enumType.UserStatus;
import kma.cnwat.be.domain.user.entity.Permission;
import kma.cnwat.be.domain.user.entity.Role;
import kma.cnwat.be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User , Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END" +
            " FROM User u " +
            "WHERE u.username = :username  AND u.status = 'ACTIVE'")
    boolean existsUserByUsername(@Param("username") String username);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END" +
            " FROM User u " +
            "WHERE u.email = :email  AND u.status = 'ACTIVE'")
    boolean existsUserByEmail(@Param("email") String username);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END" +
            " FROM User u " +
            "WHERE u.phone = :phone  AND u.status = 'ACTIVE'")
    boolean existsUserByPhone(@Param("phone") String username);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.status = 'ACTIVE'")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT r FROM Role r " +
            "LEFT JOIN UserHasRole uhr ON r.id = uhr.role.id " +
            "WHERE uhr.user.id = :id")
    List<Role> getRolesByUserId(@Param("id") Long id);


    @Query("SELECT p FROM Permission p " +
            "LEFT JOIN RoleHasPermission rhp ON p.id = rhp.permission.id " +
            "WHERE rhp.role.id = :id")
    List<Permission> getPermissionByRoleId(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsernameNotActivate(String username);


    @Query("SELECT u FROM User u " +
            "WHERE (u.username = :username OR u.email = :email OR u.phone = :phone) " +
            "AND u.status = 'INACTIVE'")
    List<User> findByUsernameNotActivateByPhoneEmailUsername(@Param("username") String username,
                                                             @Param("email") String email,
                                                             @Param("phone") String phone);


    @Query("SELECT u FROM User u WHERE u.email = :email AND u.status = 'ACTIVE'")
    Optional<User> findByEmail(@Param("email") String email);


}
