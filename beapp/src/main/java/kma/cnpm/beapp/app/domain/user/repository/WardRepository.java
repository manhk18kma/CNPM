package kma.cnwat.be.domain.user.repository;

import kma.cnwat.be.domain.user.dto.response.PDWResponse;
import kma.cnwat.be.domain.user.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, String> {
    @Query("SELECT COUNT(w) FROM Ward w")
    long countWards();

    @Query("SELECT new kma.cnwat.be.domain.user.dto.response.PDWResponse(w.id, w.name) " +
            "FROM Ward w WHERE w.district.id = :districtId AND w.district.province.id = :provinceId " +
            "ORDER BY w.name ASC")
    List<PDWResponse> getAllWardsByProvinceIdAndDistrictId(@Param("provinceId") String provinceId,
                                                           @Param("districtId") String districtId);
}
