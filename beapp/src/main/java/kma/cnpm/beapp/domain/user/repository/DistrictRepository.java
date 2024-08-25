package kma.cnpm.beapp.domain.user.repository;

import kma.cnpm.beapp.domain.user.dto.response.PDWResponse;
import kma.cnpm.beapp.domain.user.entity.District;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, String> {
    @Query("SELECT COUNT(d) FROM District d")
    long countDistricts();

    @Query("SELECT new kma.cnpm.beapp.domain.user.dto.response.PDWResponse(d.id, d.name) " +
            "FROM District d WHERE d.province.id = :provinceId ORDER BY d.name ASC")
    List<PDWResponse> getAllDistrictsByProvinceId(@Param("provinceId") String provinceId);
}
