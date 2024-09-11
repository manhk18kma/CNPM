package kma.cnpm.beapp.app.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kma.cnpm.beapp.domain.common.dto.PageResponse;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.user.dto.response.PDWResponse;
import kma.cnpm.beapp.domain.user.service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Address Controller", description = "APIs for managing addresses")
public class AddressController {

    AddressService addressService;

    @Operation(summary = "Get all provinces", description = "Retrieve a list of all provinces.")
    @GetMapping("/provinces")
    public ResponseData<PageResponse<List<PDWResponse>>> getAllProvinces() {
        PageResponse<List<PDWResponse>> provinceResponses = addressService.getAllProvinces();
        return new ResponseData<>(HttpStatus.OK.value(),
                "Provinces retrieved successfully",
                new Date(),
                provinceResponses);
    }

    @Operation(summary = "Get all districts by province", description = "Retrieve a list of all districts within a specific province.")
    @GetMapping("/provinces/{provinceId}/districts")
    public ResponseData<PageResponse<List<PDWResponse>>> getAllDistrictsByProvinceId(
            @Parameter(description = "ID of the province", required = true)
            @PathVariable String provinceId) {
        PageResponse<List<PDWResponse>> districtResponses = addressService.getAllDistrictsByProvinceId(provinceId);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Districts retrieved successfully",
                new Date(),
                districtResponses);
    }

    @Operation(summary = "Get all wards by province ID and district ID", description = "Retrieve a list of all wards within a specific district of a province")
    @GetMapping("/provinces/{provinceId}/districts/{districtId}/wards")
    public ResponseData<PageResponse<List<PDWResponse>>> getAllWardsByProvinceIdAndDistrictId(
            @Parameter(description = "ID of the province to retrieve wards for", required = true)
            @PathVariable("provinceId") String provinceId,
            @Parameter(description = "ID of the district to retrieve wards for", required = true)
            @PathVariable("districtId") String districtId
    ) {
        PageResponse<List<PDWResponse>> wardResponses = addressService.getAllWardsByProvinceIdAndDistrictId(provinceId, districtId);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Wards retrieved successfully",
                new Date(),
                wardResponses
        );
    }

    @Operation(summary = "Remove address from user", description = "Remove address from user using id of address")
    @DeleteMapping("{idAddress}")
    public ResponseData<?> removeAddressFromUser(
            @Parameter(description = "ID of the address", required = true)
            @PathVariable Long idAddress){
        addressService.removeAddressFromUser(idAddress);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Address removed successfully",
                new Date()
        );
    }

}
