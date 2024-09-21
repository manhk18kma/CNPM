package kma.cnpm.beapp.app.api.product;

import io.swagger.v3.oas.annotations.tags.Tag;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.product.dto.response.MediaResponse;
import kma.cnpm.beapp.domain.product.service.MediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medias")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Media Controller", description = "APIs for Media management")
public class MediaController {

     MediaService mediaService;

    @DeleteMapping("/{id}")
    public ResponseData<String> deleteMedia(@PathVariable Integer id) {
        mediaService.deleteMedia(id);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Phương tiện đã được xóa thành công",
                LocalDateTime.now());
    }

    @GetMapping("/{id}")
    public ResponseData<MediaResponse> getMediaById(@PathVariable Integer id) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Phương tiện được hiển thị theo ID thành công",
                LocalDateTime.now(),
                mediaService.getMediaById(id));
    }

    @GetMapping
    public ResponseData<List<MediaResponse>> getAllMedias() {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Tất cả phương tiện đã được hiển thị thành công",
                LocalDateTime.now(),
                mediaService.getAllMedia());
    }

    @GetMapping("/type/{type}")
    public ResponseData<List<MediaResponse>> getMediaByType(@PathVariable String type) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Phương tiện được hiển thị theo loại thành công",
                LocalDateTime.now(),
                mediaService.getMediaByType(type));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseData<List<MediaResponse>> getMediasBySeller(@PathVariable Long sellerId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Tất cả phương tiện đã được hiển thị theo người bán thành công",
                LocalDateTime.now(),
                mediaService.getAllMediaBySellerId(sellerId));
    }


}
