package kma.cnpm.beapp.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_user_view")
public class UserView extends AbstractEntity<Long> {

    // Người dùng đã xem
    @ManyToOne
    @JoinColumn(name = "viewer_id", nullable = false)
    private User viewer;

    // Người dùng được xem
    @ManyToOne
    @JoinColumn(name = "viewed_id", nullable = false)
    private User viewed;

    @Column(name = "view_count")
    private int viewCount = 0;
}