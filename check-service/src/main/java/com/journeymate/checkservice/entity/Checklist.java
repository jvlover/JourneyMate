package com.journeymate.checkservice.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.ws.rs.DefaultValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicInsert
@Where(clause = "is_deleted = '0'")
@Entity
public class Checklist {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private byte[] userId;

    @Column
    private Long journeyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @DefaultValue("1")
    private Integer num;

    @Column(nullable = false, columnDefinition = "TINYINT default 0")
    private Boolean isChecked;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false, columnDefinition = "TINYINT default 0")
    private Boolean isDeleted;

    public void deleteChecklist() {
        this.isDeleted = true;
    }

    public void check() {
        this.isChecked = true;
    }

}
