package com.example.triple.__repository;

import com.example.triple.domain.ReviewData;
import com.example.triple.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class __ModReviewRepositoryImpl implements __ReviewRepository {

    ReviewMapper reviewMapper;

    @Autowired
    public __ModReviewRepositoryImpl(ReviewMapper reviewMapper){
        this.reviewMapper = reviewMapper;
    }
    @Override
    public String events(ReviewData data) {
//        reviewMapper.modifyReview(data);
        return null;

    }
}
