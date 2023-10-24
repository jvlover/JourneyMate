package com.ssafy.journeymate.mateservice.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@Table(name = "mate")
public class Mate extends BaseEntity{

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

    @Column
    private Boolean isDeleted;

    @Column(nullable = false)
    private String creator;

    public void deleteMate(){
        this.isDeleted = true;
    }

}
