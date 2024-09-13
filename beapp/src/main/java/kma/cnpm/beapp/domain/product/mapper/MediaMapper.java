package kma.cnpm.beapp.domain.product.mapper;

import kma.cnpm.beapp.domain.product.dto.response.MediaResponse;
import kma.cnpm.beapp.domain.product.entity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    MediaResponse map(Media media);

}
