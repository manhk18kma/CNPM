package kma.cnpm.beapp.domain.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
public class Like extends AbstractEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private Long userId;

}
