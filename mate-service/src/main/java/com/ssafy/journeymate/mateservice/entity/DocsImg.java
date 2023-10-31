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
@Where(clause = "is_deleted = 0")
@Table(name = "docs_img")
@AllArgsConstructor
@NoArgsConstructor
public class DocsImg extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "docs_id")
    private Docs docs;

    private String fileName;

    @Column(name = "img_url")
    private String imgUrl;

}
