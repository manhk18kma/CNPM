package kma.cnpm.beapp.domain.post.mapper;

import kma.cnpm.beapp.domain.post.dto.request.PostRequest;
import kma.cnpm.beapp.domain.post.dto.response.PostResponse;
import kma.cnpm.beapp.domain.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {


    @Mapping(target = "productResponse.id", source = "productId")
    PostResponse map(Post post);
    Post map(PostRequest postRequest);

}
