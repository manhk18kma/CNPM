package kma.cnpm.beapp.domain.user.service;

import kma.cnpm.beapp.domain.common.dto.PageResponse;
import kma.cnpm.beapp.domain.common.enumType.AddressType;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.user.dto.response.PDWResponse;

import kma.cnpm.beapp.domain.user.dto.response.UserResponse;
import kma.cnpm.beapp.domain.user.dto.resquest.AddAddressRequest;
import kma.cnpm.beapp.domain.user.entity.*;
import kma.cnpm.beapp.domain.user.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
    private final UserRepository userRepository;
    AuthService authService;
    UserService userService;
    ProvinceRepository provinceRepository;
    AddressRepository addressRepository;
    DistrictRepository districtRepository;
    WardRepository wardRepository;

    public UserResponse addAddressToUser(AddAddressRequest request) {
        String provinceId = request.getProvinceId();
        String districtId = request.getDistrictId();
        String wardId = request.getWardId();
        if(!provinceRepository.existsAddress(provinceId,districtId,wardId)){
            throw new AppException(AppErrorCode.ADDRESS_NOT_EXISTED);
        }
        Ward ward = wardRepository.findById(wardId).get();
        District district = districtRepository.findById(districtId).get();
        Province province = provinceRepository.findById(provinceId).get();

        String id = authService.getAuthenticationName();
        User user = userService.findUserById(id);



        Address address = Address.builder()
                .addressDetail(request.getAddressDetail())
                .addressType(AddressType.valueOf(request.getAddressType()))
                .ward(ward)
                .district(district)
                .province(province)
                .build();
        user.addAddress(address);
        addressRepository.save(address);

        return UserResponse.builder().id(user.getId()).build();


    }

    public PageResponse<List<PDWResponse>> getAllProvinces(){
        List<PDWResponse> provinces = provinceRepository.getAllProvinces();
        return PageResponse.<List<PDWResponse>>builder()
                .pageNo(0)
                .pageSize(provinces.size())
                .totalElements(provinces.size())
                .totalPages(1)
                .items(provinces)
                .build();
    }

    public PageResponse<List<PDWResponse>> getAllDistrictsByProvinceId(String provinceId) {
        List<PDWResponse> district = districtRepository.getAllDistrictsByProvinceId(provinceId);
        return PageResponse.<List<PDWResponse>>builder()
                .pageNo(0)
                .pageSize(district.size())
                .totalElements(district.size())
                .totalPages(1)
                .items(district)
                .build();
    }

    public PageResponse<List<PDWResponse>> getAllWardsByProvinceIdAndDistrictId(String provinceId, String districtId) {
        List<PDWResponse> district = wardRepository.getAllWardsByProvinceIdAndDistrictId(provinceId , districtId);
        return PageResponse.<List<PDWResponse>>builder()
                .pageNo(0)
                .pageSize(district.size())
                .totalElements(district.size())
                .totalPages(1)
                .items(district)
                .build();
    }

    @Transactional
    public void saveProvince(Province province) {
         provinceRepository.save(province);
    }

    @Transactional
    public void saveDistrict(District district) {
        districtRepository.save(district);
    }

    @Transactional
    public void saveWard(Ward ward) {
        wardRepository.save(ward);
    }


    public void removeAddressFromUser(Long idAddress) {
        String id = authService.getAuthenticationName();
        User user = userService.findUserById(id);
        Address address = addressRepository.findById(idAddress)
                .orElseThrow(() -> new AppException(AppErrorCode.ADDRESS_NOT_EXISTED));
        if (!user.getId().equals(address.getUser().getId())) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }

        addressRepository.delete(address);
    }
}
