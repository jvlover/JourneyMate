package com.ssafy.journeymate.mateservice.entity;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Entity
@Builder
@Getter
@Setter
@Where(clause = "is_deleted = 0")
@Table(name = "mate")
@RequiredArgsConstructor
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

    @OneToMany(mappedBy = "mate")
    private List<Contents> contents = new ArrayList<>();


}
