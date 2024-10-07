package kma.cnpm.beapp.domain.user.repository;

import kma.cnpm.beapp.domain.user.entity.UserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserViewRepository extends JpaRepository<UserView , Long> {

    @Query("SELECT uv FROM UserView uv WHERE uv.viewer.id = :idViewer AND uv.viewed.id = :idTarget")
    Optional<UserView> findByUserViewIdAndTargetId(@Param("idViewer") Long idViewer, @Param("idTarget") Long idTarget);

    @Query("SELECT uv FROM UserView uv WHERE uv.viewed.id = :userTarget ORDER BY uv.updatedAt DESC")
    List<UserView> findUserViewByUserTargetId(@Param("userTarget") Long userTarget);

    @Query("SELECT COUNT(uv) FROM UserView uv WHERE uv.viewed.id = :id AND uv.viewer.id != :id1")
    int countOtherView(@Param("id") Long id, @Param("id1") Long id1);


}
