package com.ssafy.journeymate.mateservice.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "docs_img")
public class DocsImg {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id")
    private Docs docs;

    private String filename;

    @Column(name = "img_url")
    private String imgUrl;



}
