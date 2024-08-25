package kma.cnpm.beapp.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_permission")
public class Permission extends AbstractEntity<Long>{

    @Column(name = "permission_name",  length = 100)
    private String permissionName;

    @Column(name = "description")
    private String description;
}
