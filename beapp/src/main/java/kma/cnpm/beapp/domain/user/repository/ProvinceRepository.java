package kma.cnpm.beapp.domain.user.repository;

import kma.cnwat.be.domain.user.dto.response.PDWResponse;
import kma.cnwat.be.domain.user.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, String> {

    @Query("SELECT COUNT(p) FROM Province p")
    long countProvinces();
    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END " +
            "FROM Ward w " +
            "JOIN w.district d " +
            "JOIN d.province p " +
            "WHERE p.id = :provinceId " +
            "AND d.id = :districtId " +
            "AND w.id = :wardId")
    boolean existsAddress(@Param("provinceId") String provinceId,
                          @Param("districtId") String districtId,
                          @Param("wardId") String wardId);

    @Query("SELECT new kma.cnwat.be.domain.user.dto.response.PDWResponse(p.id, p.name) FROM Province p ORDER BY p.name ASC")
    List<PDWResponse> getAllProvinces();


//    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
//            "FROM (" +
//            "    SELECT tbl1.pId AS pId, tbl2.dId AS dId " +
//            "    FROM (" +
//            "        SELECT p.id AS pId " +
//            "        FROM Province p " +
//            "        WHERE p.id = :provinceId" +
//            "    ) AS tbl1 " +
//            "    INNER JOIN (" +
//            "        SELECT d.province.id AS pId, d.id AS dId " +
//            "        FROM District d " +
//            "        WHERE d.id = :districtId" +
//            "    ) AS tbl2 " +
//            "    ON tbl1.pId = tbl2.pId" +
//            ") AS tbl3 " +
//            "INNER JOIN (" +
//            "    SELECT w.district.id AS dId, w.id AS wId " +
//            "    FROM Ward w " +
//            "    WHERE w.id = :wardId" +
//            ") AS tbl4 " +
//            "ON tbl3.dId = tbl4.dId")
//    boolean existsAddress(@Param("provinceId") String provinceId,
//                          @Param("districtId") String districtId,
//                          @Param("wardId") String wardId);






}

