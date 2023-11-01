package com.ssafy.journeymate.mateservice.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

@Entity
@Getter
@AllArgsConstructor
@DynamicInsert
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = '0'")
@Table(name = "docs")
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

    @Column(name = "image_exist")
    private Boolean imageExist;

    public void modifyTitle(String title){
        this.title = title;
    }

    public void modifyContent(String content){
        this.content = content;
    }

    public void modifyImageExist(Boolean imageExist){
        this.imageExist = imageExist;
    }

}
