package com.ssafy.journeymate.journeyservice.entity;

import com.ssafy.journeymate.journeyservice.dto.request.JourneyModifyPutReq;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyRegistPostReq;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

@Getter // Getter 자동 생성
@ToString   // toString 메소드 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 변수를 파라미터로 받는 생성자
@SuperBuilder   // Builder를 보완한 Annotation. 상속 받은 필드도 build 해줌, but experimental
@DynamicInsert  // INSERT 구문에서 null이 아닌 컬럼들만 실제로 insert
@Where(clause = "is_deleted = '0'")   // 일괄적으로 적용할 where 조건. 현재 clause는 soft delete를 위함
@Entity
public class Journey extends BaseEntity {


    @Column(nullable = false)
    private Long mateId;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false, length = 40)
    private String title;

    @Column(nullable = false)
    private Integer day;

    @Column(nullable = false)
    private Integer sequence;

    @Column(nullable = false)
    private Double xcoordinate;

    @Column(nullable = false)
    private Double ycoordinate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Integer isDeleted; // 0 false  1 true


    public void modifyJourney(JourneyModifyPutReq journeyModifyReq) {

        this.mateId = journeyModifyReq.getMateId();
        this.categoryId = journeyModifyReq.getCategoryId();
        this.title = journeyModifyReq.getTitle();
        this.xcoordinate = journeyModifyReq.getXcoordinate();
        this.ycoordinate = journeyModifyReq.getYcoordinate();
        this.isDeleted = 0;
        this.updatedAt = LocalDateTime.now();

    }


    public void deleteJourney() {
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = 1;
    }

    public void updateCategoryJourney() {
    }


}
