package kma.cnpm.beapp.domain.product.service.impl;

import kma.cnpm.beapp.domain.common.upload.ImageService;
import kma.cnpm.beapp.domain.product.dto.response.MediaResponse;
import kma.cnpm.beapp.domain.product.entity.Media;
import kma.cnpm.beapp.domain.product.entity.Product;
import kma.cnpm.beapp.domain.product.mapper.MediaMapper;
import kma.cnpm.beapp.domain.product.repository.MediaRepository;
import kma.cnpm.beapp.domain.product.service.MediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaServiceImpl implements MediaService {

    MediaRepository mediaRepository;
    MediaMapper mediaMapper;
    ImageService imageService;

    @Override
    public void deleteMedia(Integer id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(""));
        mediaRepository.deleteById(id);
        if (media.getType().equals("IMAGE"))
            imageService.deleteImage(media.getUrl());
        if (media.getType().equals("VIDEO"))
            imageService.deleteVideo(media.getUrl());
    }

    @Override
    public MediaResponse getMediaById(Integer id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(""));
        return mediaMapper.map(media);
    }

    @Override
    public List<MediaResponse> getAllMedia() {
        List<Media> medias = mediaRepository.findAll();
        return medias.stream()
                .map(mediaMapper::map)
                .toList();
    }

    @Override
    public List<MediaResponse> getMediaByType(String type) {
        List<Media> medias = mediaRepository.findAllByType(type);
        return medias.stream()
                .map(mediaMapper::map)
                .toList();
    }

}
