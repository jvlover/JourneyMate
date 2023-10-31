package com.ssafy.journeymate.mateservice.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Builder
@Table(name = "docs")
@Where(clause = "is_deleted = 0")
@AllArgsConstructor
@NoArgsConstructor
public class Docs extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "mate_id")
    private Mate mate;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "image_exist", columnDefinition = "TINYINT default '0'")
    private Boolean imageExist;

}
