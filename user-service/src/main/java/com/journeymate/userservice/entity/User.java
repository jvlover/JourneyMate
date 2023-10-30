package com.journeymate.userservice.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
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
@Table(name = "users")
public class User {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private byte[] id;

    @Column(nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false)
    @ColumnDefault("'noImage'")
    private String imgUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false, columnDefinition = "TINYINT default 0")
    private Boolean isDeleted;

    public void deleteUser() {
        this.isDeleted = true;
    }

    public void modifyProfile(String nickname, String imgUrl) {
        this.nickname = nickname;
        this.imgUrl = imgUrl;
    }

}
