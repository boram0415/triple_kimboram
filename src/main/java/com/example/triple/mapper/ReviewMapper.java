package com.example.triple.mapper;


import com.example.triple.domain.ReviewData;
import com.example.triple.domain.ReviewPoint;
import com.example.triple.util.ReviewUtil;
import org.apache.ibatis.annotations.Mapper;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface ReviewMapper {

    // 리뷰 작성
    int writeReview(ReviewData data);
    // 첫 방문 장소 확인
    int visitedPlace(String placeId);
    // 포인트 개별 적립
    int earnPointInsert(ReviewPoint pointDTO);

    int selectReview(ReviewData data);

    int photoInsert(List<Map<String,String>> item);

    ReviewData validCheck(String reviewId);
    int modifyReviewContent(ReviewData data);

    int deleteImg(String reviewId);

    int selectImgLen(String reviewId);

    int userPointSelect(String userId);

    int userPointUpdate(ReviewPoint pointDTO);

    void deleteReviewUpdate(String reviewId);

    void deletePointUpdate(ReviewPoint pointDTO);

    String userPointCheck(String userId);

}
