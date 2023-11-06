package com.ssafy.journeymate.mateservice.entity;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "mate")
public class Mate extends BaseEntity {

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

    public void modifyDestination(String destination) {
        this.destination = destination;
    }

    public void modifyName(String name) {
        this.name = name;
    }

}
