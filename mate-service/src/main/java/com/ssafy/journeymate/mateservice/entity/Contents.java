package com.ssafy.journeymate.mateservice.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Builder
@Table(name = "contents")
@Where(clause = "is_deleted = 0")
public class Contents extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    @JoinColumn(name = "mate_id")
    private Mate mate;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private String fileName;

    @Column(name = "img_url")
    private String imgUrl;

    private Boolean type;

}
