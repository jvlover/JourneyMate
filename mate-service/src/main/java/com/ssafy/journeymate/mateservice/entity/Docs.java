package com.ssafy.journeymate.mateservice.entity;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "docs")
public class Docs extends BaseEntity{

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name =  "id")
    private Mate mate;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image_exist")
    private Boolean imageExist;

    @Column(name = "is_deleted")
    private Boolean isDeleted;


}
