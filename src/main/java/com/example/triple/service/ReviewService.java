package com.example.triple.service;

import com.example.triple.domain.ReviewData;
import com.example.triple.domain.ReviewPoint;
import com.example.triple.mapper.ReviewMapper;
import com.example.triple.util.ReviewUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewService {
    ReviewMapper reviewMapper;

    @Autowired
    public ReviewService(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    public String events(ReviewData data) {

        ReviewUtil.msg = null;

        // 유효성 체크
        valid(data);
        if (ReviewUtil.msg != null) {
            return ReviewUtil.msg;
        }

        if (ReviewUtil.ADD.equals(data.getAction())) {

            try {
                // 포인트 적용
                addPoint(data);
                // 리뷰 저장
                reviewMapper.writeReview(data);
                return ReviewUtil.REVIEW_INSERTED;

            } catch (DuplicateKeyException e) {
                log.info("[ DuplicateKeyException >>  placeId : {} , userId : {} , ReviewId : {} ] ", data.getPlaceId(), data.getUserId(), data.getReviewId());
            }


        } else if (ReviewUtil.MOD.equals(data.getAction())) {
            try {

                int imgCheck = reviewMapper.selectImgLen(data.getReviewId());

                // 이전에는 이미지가 있었는데
                if (imgCheck > 0) {
                    // 현재는 없는거
                    if (data.getAttachedPhotoIds() == null) {
                        reviewMapper.deleteImg(data.getReviewId());
                        data.setImgLength(0);
                        modPoint(data);
                        reviewMapper.modifyReviewContent(data);
                        return ReviewUtil.REVIEW_MODIFIED;
                    } else {
                        reviewMapper.deleteImg(data.getReviewId());
                        reviewMapper.photoInsert(ReviewUtil.photoSave(data));
                        data.setImgLength(data.getAttachedPhotoIds().length);
                        reviewMapper.modifyReviewContent(data);
                        return ReviewUtil.REVIEW_MODIFIED;
                    }
                } else {
                    // 이미지가 이미 0이라서 포인트 차감 없음
                    if (data.getAttachedPhotoIds() == null) {
                        data.setImgLength(0);
                        reviewMapper.modifyReviewContent(data);
                        return ReviewUtil.REVIEW_MODIFIED;
                    } else {
                        reviewMapper.photoInsert(ReviewUtil.photoSave(data));
                        data.setImgLength(data.getAttachedPhotoIds().length);
                        modPoint(data);
                        reviewMapper.modifyReviewContent(data);
                        return ReviewUtil.REVIEW_MODIFIED;
                    }
                }

            } catch (DuplicateKeyException e) {
                e.printStackTrace();
            }


        } else if (ReviewUtil.DELETE.equals(data.getAction())) {

            ReviewData delCheck = reviewMapper.validCheck(data.getReviewId());
            int point = reviewMapper.userPointSelect(data.getUserId());
            ReviewPoint pointDTO = new ReviewPoint();
            pointDTO.setReviewId(data.getReviewId());
            pointDTO.setUserId(data.getUserId());
            pointDTO.setSummary(ReviewUtil.POINT_DELETED);

            if (point > 0) {
                if (delCheck.getImgLength() > 0 && delCheck.getFirstVisited() > 0) {
                    point = point - 3;
                    pointDTO.setPoint(point);
                    pointDTO.setDummyPoint(3);
                } else if ((delCheck.getImgLength() == 0 && delCheck.getFirstVisited() > 0) ||
                        (delCheck.getImgLength() > 0 && delCheck.getFirstVisited() == 0)) {
                    point = point - 2;
                    pointDTO.setPoint(point);
                    pointDTO.setDummyPoint(2);
                } else {
                    point = point - 1;
                    pointDTO.setPoint(point);
                    pointDTO.setDummyPoint(1);
                }
                reviewMapper.userPointUpdate(pointDTO);
                reviewMapper.deletePointUpdate(pointDTO);
                reviewMapper.deleteReviewUpdate(data.getReviewId());
            } else {
                reviewMapper.deleteReviewUpdate(data.getReviewId());
            }

            return ReviewUtil.REVIEW_DELETED;
        }

        return ReviewUtil.DIFFERENT_REVIEW;
    }

    private void valid(ReviewData data) {

        // null 값 체크
        if (data.getReviewId().equals("") || data.getUserId().equals("") || data.getPlaceId().equals("")) {
            ReviewUtil.msg = ReviewUtil.DIFFERENT_REVIEW;
            return;
        }


        try {
            int alreadyReviewCk = reviewMapper.selectReview(data);
            ReviewData validReview = reviewMapper.validCheck(data.getReviewId());

            if (ReviewUtil.ADD.equals(data.getAction())) {
                // 리뷰 중복 체크
                if (alreadyReviewCk > 0) {
                    ReviewUtil.msg = ReviewUtil.ALREADY_WRITTEN;
                    return;
                }
                // 리뷰Id 유효성 체크
                if (validReview != null) {
                    if (!(data.getUserId().equals(validReview.getUserId()))) {
                        ReviewUtil.msg = "[유효하지 않은 ReviewId] " + ReviewUtil.DIFFERENT_REVIEW;
                        return;
                    }
                    if (data.getUserId().equals(validReview.getUserId()) && data.getReviewId().equals(validReview.getReviewId()) && !(data.getPlaceId().equals(validReview.getPlaceId()))) {
                        ReviewUtil.msg = "[유효하지 않은 PlaceId] " + ReviewUtil.DIFFERENT_REVIEW;
                        return;
                    }
                }
            } else {
                if (alreadyReviewCk == 0) {
                    ReviewUtil.msg = ReviewUtil.DIFFERENT_REVIEW;
                    return;
                }

                if (validReview != null) {
                    if (!(validReview.getUserId().equals(data.getUserId()) && validReview.getPlaceId().equals(data.getPlaceId()))) {
                        ReviewUtil.msg = ReviewUtil.DIFFERENT_REVIEW;
                        return;
                    }
                }

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void addPoint(ReviewData data) {

        ReviewPoint pointDTO = new ReviewPoint();
        int userPoint = reviewMapper.userPointSelect(data.getUserId());
        int point = 1;
        int visit = reviewMapper.visitedPlace(data.getPlaceId());

        pointDTO.setReviewId(data.getReviewId());
        pointDTO.setUserId(data.getUserId());
        pointDTO.setSummary(ReviewUtil.POINT_INSERTED);
        reviewMapper.earnPointInsert(pointDTO);

        // 첨부 사진 있을 경우
        if (data.getAttachedPhotoIds() != null) {
            point++;
            pointDTO.setSummary(ReviewUtil.POINT_IMG_INSERTED);
            data.setImgLength(data.getAttachedPhotoIds().length);
            reviewMapper.photoInsert(ReviewUtil.photoSave(data));
            reviewMapper.earnPointInsert(pointDTO);
        }

        // 첫 방문일 경우
        if (visit == 0) {
            point++;
            pointDTO.setSummary(ReviewUtil.POINT_FIRST_INSERTED);
            data.setFirstVisited(1);
            reviewMapper.earnPointInsert(pointDTO);
        }

        pointDTO.setPoint(userPoint += point);
        reviewMapper.userPointUpdate(pointDTO);
    }

    private void modPoint(ReviewData data) {

        ReviewPoint pointDTO = new ReviewPoint();
        int userPoint = reviewMapper.userPointSelect(data.getUserId());

        if (data.getAttachedPhotoIds() == null) {
            pointDTO.setPoint(userPoint -= 1);
            pointDTO.setReviewId(data.getReviewId());
            pointDTO.setUserId(data.getUserId());
            pointDTO.setSummary(ReviewUtil.POINT_IMG_DELETED);
            reviewMapper.userPointUpdate(pointDTO);
            reviewMapper.earnPointInsert(pointDTO);
        } else {
            pointDTO.setPoint(userPoint += 1);
            pointDTO.setReviewId(data.getReviewId());
            pointDTO.setUserId(data.getUserId());
            pointDTO.setSummary(ReviewUtil.POINT_IMG_INSERTED);
            reviewMapper.userPointUpdate(pointDTO);
            reviewMapper.earnPointInsert(pointDTO);
        }


    }


    public String pointCheck(String userId) {

        String point = reviewMapper.userPointCheck(userId);

        if (point != null) {
            return point;
        }

        return ReviewUtil.POINT_CHECK_FAIL;
    }

}