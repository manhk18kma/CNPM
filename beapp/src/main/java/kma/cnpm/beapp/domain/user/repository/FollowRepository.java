package kma.cnpm.beapp.domain.user.repository;

import kma.cnpm.beapp.domain.common.enumType.RelationshipType;
import kma.cnpm.beapp.domain.user.entity.Follow;
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



}
