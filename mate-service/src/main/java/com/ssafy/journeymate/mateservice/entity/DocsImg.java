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
@Where(clause = "is_deleted = 0")
@Table(name = "docs_img")
public class DocsImg extends BaseEntity{

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id")
    private Docs docs;

    private String filename;

    @Column(name = "img_url")
    private String imgUrl;

}
