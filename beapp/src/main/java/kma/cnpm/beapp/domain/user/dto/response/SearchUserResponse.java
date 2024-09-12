package kma.cnpm.beapp.domain.user.dto.response;

import kma.cnpm.beapp.domain.common.enumType.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserResponse implements Serializable {
    Long userId;
    String avatar;
    String fullName;
    RelationshipType type;
}
