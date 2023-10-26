package com.ssafy.journeymate.mateservice.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Where;

@Entity
@Builder
@Getter
@Setter
@Where(clause = "is_deleted = 0")
@Table(name = "mate")
public class Mate extends BaseEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false, updatable = false)
    private LocalDateTime startDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime endDate;


    @Column(nullable = false)
    private String creator;

    @OneToMany(mappedBy = "mate")
    private List<Docs> docs = new ArrayList<>();


}
