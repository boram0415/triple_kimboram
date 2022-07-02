//package com.example.triple.__repository;
//
//import com.example.triple.domain.ReviewData;
//import com.example.triple.domain.ReviewPoint;
//import com.example.triple.mapper.ReviewMapper;
//import com.example.triple.util.ReviewUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DuplicateKeyException;
//
//
//@Slf4j
//public class __AddReviewRepositoryImpl implements __ReviewRepository {
//
//    private ReviewMapper reviewMapper;
//
//    @Autowired
//    public __AddReviewRepositoryImpl(ReviewMapper reviewMapper){
//        this.reviewMapper = reviewMapper;
//    }
//
//    @Override
//    public String events(ReviewData data) {
//
//        if("ADD".equals(data.getAction())){
//
//            try {
//                // 유효성 체크
//                validReview(data);
//                if (ReviewUtil.msg != null) {
//                    return ReviewUtil.msg;
//                }
//                // 포인트 적용
//                earnPoint(data);
//                // 리뷰 저장
//                reviewMapper.writeReview(data);
//                ReviewUtil.msg = ReviewUtil.REVIEW_INSERTED;
//                return ReviewUtil.msg;
//
//            } catch (DuplicateKeyException e) {
//                log.info("[ DuplicateKeyException >>  placeId : {} , userId : {} , ReviewId : {} ] ", data.getPlaceId(), data.getUserId(), data.getReviewId());
//            } finally {
//                ReviewUtil.msg = null;
//            }
//
//            return ReviewUtil.DIFFERENT_REVIEW;
//        } else if ("MOD".equals(data.getAction())) {
//
////        } else if ("DELETE".equals(action)) {
//
////        }else{
//
//        }
//
//
//
//
//
//
//
//
//
//
//        return null;
//    }
//    private void validReview(ReviewData data) {
//
//        try {
//            ReviewData reviewDTO = reviewMapper.selectReview(data);
//            ReviewData validReview = reviewMapper.validReviewCheck(data.getReviewId());
//
//            // 리뷰 중복 체크
//            if (reviewDTO != null) {
//                System.out.println("해당 장소 리뷰 기 작성");
//                ReviewUtil.msg = ReviewUtil.ALREADY_WRITTEN;
//                return;
//            }
//            // 리뷰Id 유효성 체크
//            if (validReview != null) {
//                if (!(data.getUserId().equals(validReview.getUserId()))) {
//                    System.out.println("ReviewId 유효하지 않음 !! ");
//                    ReviewUtil.msg = "[유효하지 않은 ReviewId] " + ReviewUtil.DIFFERENT_REVIEW;
//                    return;
//                }
//                if(data.getUserId().equals(validReview.getUserId()) && data.getReviewId().equals(validReview.getReviewId()) &&
//                  !(data.getPlaceId().equals(validReview.getPlaceId()))) {
//                    ReviewUtil.msg = "[유효하지 않은 PlaceId] " + ReviewUtil.DIFFERENT_REVIEW;
//                    return;
//                }
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 포인트 차등 적용 메소드
//    private void earnPoint(ReviewData data) {
//
//        ReviewPoint pointDTO = new ReviewPoint();
//        int point = 1;
//        int visit = reviewMapper.visitedPlace(data.getPlaceId());
//
//        pointDTO.setReviewId(data.getReviewId());
//        pointDTO.setUserId(data.getUserId());
//        pointDTO.setSummary(ReviewUtil.POINT_INSERTED);
//        reviewMapper.earnPointInsert(pointDTO);
//
//        // 첨부 사진 있을 경우
//        if (data.getAttachedPhotoIds() != null) {
//            point++;
//            pointDTO.setSummary(ReviewUtil.POINT_IMG_INSERTED);
//            data.setImgLength(data.getAttachedPhotoIds().length);
//            reviewMapper.photoInsert(ReviewUtil.photoSave(data));
//            reviewMapper.earnPointInsert(pointDTO);
//        }
//
//        // 첫 방문일 경우
//        if (visit == 0) {
//            point++;
//            pointDTO.setSummary(ReviewUtil.POINT_FIRST_INSERTED);
//            data.setFirstVisited(1);
//            reviewMapper.earnPointInsert(pointDTO);
//        }
//        data.setPoint(point);
////        reviewMapper.pointUpdate(data);
//    }
//
//
//
//}
