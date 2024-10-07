package kma.cnpm.beapp.domain.user.repository;

import kma.cnpm.beapp.domain.common.enumType.RelationshipType;
import kma.cnpm.beapp.domain.user.entity.Follow;
import kma.cnpm.beapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow , Long> {


    @Query("SELECT CASE WHEN EXISTS " +
            "(SELECT 1 FROM Follow f WHERE f.follower.id = :followerId AND f.followed.id = :followedId) " +
            "THEN true ELSE false END")
    boolean existsFollow(@Param("followerId") Long followerId, @Param("followedId") Long followedId);

    @Query("SELECT f FROM Follow f WHERE f.follower.id = :followerId AND f.followed.id = :followedId")
    Optional<Follow> findFollowByFollowerAndFollowing(Long followerId, Long followedId);


    @Query("SELECT count(f.id) FROM Follow f WHERE f.follower.id = :userId")
    int countFollowingOfUser(@Param("userId") Long userId);

    @Query("SELECT count(f.id) FROM Follow f WHERE f.followed.id = :userId")
    int countFollowerOfUser(@Param("userId") Long userId);


    @Query("SELECT f FROM Follow f WHERE f.followed.id = :userId")
    List<Follow> getFollowersOfUser(@Param("userId") Long userId);

    @Query("SELECT f FROM Follow f WHERE f.follower.id = :userId")
    List<Follow> getFollowingOfUser(@Param("userId") Long userId);

    @Query("SELECT CASE " +
            "WHEN EXISTS (SELECT 1 FROM Follow f WHERE f.follower.id = :searchUserId AND f.followed.id = :targetUserId) THEN 'FOLLOWING' " +
            "WHEN EXISTS (SELECT 1 FROM Follow f WHERE f.follower.id = :targetUserId AND f.followed.id = :searchUserId) THEN 'FOLLOWER' " +
            "ELSE 'NONE' " +
            "END")
    RelationshipType getRelationshipTypeBetweenUser(@Param("searchUserId") Long searchUserId,
                                                    @Param("targetUserId") Long targetUserId);

    //=================
//    @Query("SELECT u FROM User u " +
//            "INNER JOIN Follow f ON f.follower.id = u.id " +
//            "WHERE f.followed.id = :userId")

//    @Query("SELECT u FROM User u " +
//            "WHERE EXISTS (" +
//            "   SELECT f FROM Follow f " +
//            "   WHERE f.follower.id = u.id " +
//            "   AND f.followed.id = :userId" +
//            ")")

//    @Query("SELECT u FROM User u INNER JOIN " +
//            "(SELECT f.follower.id as id1 FROM Follow f WHERE f.followed.id = :userId) AS tbl_follow " +
//            "ON u.id = tbl_follow.id1")
//

    @Query("SELECT u FROM User u WHERE u.id IN " +
            "(SELECT f.follower.id FROM Follow f WHERE f.followed.id = :userId)")
    List<User> getUserFollowersOfUser(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END FROM Follow f WHERE f.follower.id = :userId AND f.followed.id = :idUserGet")
    boolean isFollowingOfUser(@Param("userId") Long userId, @Param("idUserGet") Long idUserGet);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END FROM Follow f WHERE f.followed.id = :userId AND f.follower.id = :idUserGet")
    boolean isFollowerOfUser(@Param("userId") Long userId, @Param("idUserGet") Long idUserGet);

}
