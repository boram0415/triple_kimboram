package com.example.triple.domain;

import lombok.Data;

@Data
public class ReviewPoint {

    private int point;
    private String userId;
    private String reviewId;
    private String summary;
    private int dummyPoint;

}
