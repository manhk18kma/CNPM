package kma.cnpm.beapp.domain.product.service;

import kma.cnpm.beapp.domain.product.dto.response.MediaResponse;

import java.util.List;

public interface MediaService {

    void deleteMedia(Integer id);

    MediaResponse getMediaById(Integer id);
    List<MediaResponse> getAllMedia();
    List<MediaResponse> getMediaByType(String type);

}
