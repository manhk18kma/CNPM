package kma.cnpm.beapp.domain.post.mapper;

import kma.cnpm.beapp.domain.post.dto.request.CommentRequest;
import kma.cnpm.beapp.domain.post.dto.response.CommentResponse;
import kma.cnpm.beapp.domain.post.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment map(CommentRequest commentRequest);
    CommentResponse map(Comment comment);

}
