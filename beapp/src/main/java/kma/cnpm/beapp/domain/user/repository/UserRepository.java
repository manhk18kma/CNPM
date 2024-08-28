package kma.cnpm.beapp.domain.user.repository;

import kma.cnpm.beapp.domain.user.entity.Permission;
import kma.cnpm.beapp.domain.user.entity.Role;
import kma.cnpm.beapp.domain.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END" +
            " FROM User u " +
            "WHERE u.email = :email  AND u.status = 'ACTIVE'")
    boolean existsUserByUsername(@Param("email") String email);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END" +
            " FROM User u " +
            "WHERE u.email = :email  AND u.status = 'ACTIVE'")
    boolean existsUserByEmail(@Param("email") String email);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END" +
            " FROM User u " +
            "WHERE u.phone = :phone  AND u.status = 'ACTIVE'")
    boolean existsUserByPhone(@Param("phone") String phone);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.status = 'ACTIVE'")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT r FROM Role r " +
            "LEFT JOIN UserHasRole uhr ON r.id = uhr.role.id " +
            "WHERE uhr.user.id = :id")
    List<Role> getRolesByUserId(@Param("id") Long id);


    @Query("SELECT p FROM Permission p " +
            "LEFT JOIN RoleHasPermission rhp ON p.id = rhp.permission.id " +
            "WHERE rhp.role.id = :id")
    List<Permission> getPermissionByRoleId(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailActivateOrInactive(String email);


//    @Query("SELECT u FROM User u " +
//            "WHERE  u.email = :email AND u.status = 'INACTIVE'")
    @Query("SELECT u FROM User u " +
            "WHERE  u.email = :email")
    List<User> findUsersByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u " +
            "WHERE  u.id = :id")
    Optional<User> findUserById(Long id);

}
