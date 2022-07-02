package com.example.triple.domain;

import lombok.Data;
@Data
public class ReviewData {
    private int idx;
    private String type ;
    private String action;
    private String reviewId;
    private String [] attachedPhotoIds;
    private int imgLength ;
    private int firstVisited;
    private String content;
    private String userId;
    private String placeId;
    private int point;
    private int status;
}
