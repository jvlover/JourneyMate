package com.ssafy.journeymate.mateservice.entity;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseEntity {


    @CreatedDate
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "edit_date")
    private LocalDateTime updatedDate;

    @Column(name = "is_deleted", columnDefinition = "TINYINT default '0'")
    private boolean isDeleted;

    public void delete() {
        this.isDeleted = true;
    }

}
