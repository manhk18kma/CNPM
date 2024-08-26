package kma.cnpm.beapp.domain.user.service.fetchApi;

import kma.cnpm.beapp.domain.user.entity.District;
import kma.cnpm.beapp.domain.user.entity.Province;
import kma.cnpm.beapp.domain.user.entity.Ward;
import kma.cnpm.beapp.domain.user.repository.DistrictRepository;
import kma.cnpm.beapp.domain.user.repository.ProvinceRepository;
import kma.cnpm.beapp.domain.user.repository.WardRepository;
import kma.cnpm.beapp.domain.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataInitializer
        implements CommandLineRunner
{

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private AddressService mapService;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private WardRepository wardRepository;



    @Override
    @Transactional
    public void run(String... args) throws Exception {
        long totalProvince = provinceRepository.countProvinces();
        long totalDistrict = districtRepository.countDistricts();
        long totalWard = wardRepository.countWards();
        if(totalProvince==63 && totalWard == 10597 && totalDistrict == 700){
            return;
        }else {
            wardRepository.deleteAll();
            districtRepository.deleteAll();
            provinceRepository.deleteAll();
        ApiResponse<Province> provinceResponse = apiClient.fetchData(
                "https://esgoo.net/api-tinhthanh/1/0.htm",
                new ParameterizedTypeReference<ApiResponse<Province>>() {}
        );
        List<Province> provinces = provinceResponse.getData();

        for (Province province : provinces) {
            mapService.saveProvince(province);

            ApiResponse<District> districtResponse = apiClient.fetchData(
                    "https://esgoo.net/api-tinhthanh/2/" + province.getId() + ".htm",
                    new ParameterizedTypeReference<ApiResponse<District>>() {}
            );
            List<District> districts = districtResponse.getData();

            for (District district : districts) {
                district.setProvince(province);
                mapService.saveDistrict(district);

                ApiResponse<Ward> wardResponse = apiClient.fetchData(
                        "https://esgoo.net/api-tinhthanh/3/" + district.getId() + ".htm",
                        new ParameterizedTypeReference<ApiResponse<Ward>>() {}
                );
                List<Ward> wards = wardResponse.getData();

                for (Ward ward : wards) {
                    ward.setDistrict(district);
                    mapService.saveWard(ward);
                }
            }
        }


        }
    }
}