package com.ssafy.journeymate.mateservice.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Builder
@Table(name = "docs")
@Where(clause = "is_deleted = 0")
public class Docs extends BaseEntity {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id")
    private Mate mate;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "image_exist")
    private Boolean imageExist;

}
