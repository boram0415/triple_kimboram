package com.example.triple.util;

import com.example.triple.domain.ReviewData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewUtil {


    public static String msg ;
    public static final int POINT = 1;
    public static final String REVIEW_INSERTED = "리뷰 작성이 완료 되었습니다.";
    public static final String REVIEW_MODIFIED = "리뷰 수정이 완료 되었습니다.";
    public static final String REVIEW_DELETED = "리뷰 삭제 완료 되었습니다.";
    public static final String ALREADY_WRITTEN = "이미 작성 했거나 유효하지 않는 리뷰 입니다.!";
    public static final String DIFFERENT_REVIEW = "유효하지 않은 리뷰 입니다.";


    public static final String POINT_INSERTED = "리뷰 작성 포인트 1점 추가";
    public static final String POINT_IMG_INSERTED = "사진 첨부 포인트 1점 추가";
    public static final String POINT_FIRST_INSERTED = "특정 장소 첫 리뷰 1점 추가";

    public static final String POINT_DELETED = "리뷰 삭제로 인한 포인트 차감";
    public static final String POINT_IMG_DELETED = "리뷰 이미지 삭제로 인한 포인트 차감";

    public static final String POINT_CHECK_FAIL = "사용자 ID를 확인 바랍니다.";

    public static final String ADD = "ADD";
    public static final String MOD = "MOD";
    public static final String DELETE = "DELETE";

    // 이미지 저장 메소드
    public static List<Map<String,String>> photoSave(ReviewData data) {

            List<Map<String, String>> photoList = new ArrayList<>();
            for (String item : data.getAttachedPhotoIds()) {
                Map<String, String> map = new HashMap<>();
                map.put("reviewId", data.getReviewId());
                map.put("userId", data.getUserId());
                map.put("attachedPhotoIds" , item);
                photoList.add(map);
            }
            return photoList;

    }





}
