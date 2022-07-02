package com.example.triple.__repository;

import com.example.triple.domain.ReviewData;
import com.example.triple.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class __DeleteReviewRepositoryImpl implements __ReviewRepository {

    private ReviewMapper reviewMapper;
    private ReviewData data ;

    @Autowired
    public __DeleteReviewRepositoryImpl(ReviewMapper reviewMapper){
        this.reviewMapper = reviewMapper;
    }
    @Override
    public String events(ReviewData data) {
        return null;
    }
}
