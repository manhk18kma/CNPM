package kma.cnpm.beapp.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_follow")
public class Follow extends AbstractEntity<Long> {

    // Người theo dõi
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    // Người được theo dõi
    @ManyToOne
    @JoinColumn(name = "followed_id")
    private User followed;
}
