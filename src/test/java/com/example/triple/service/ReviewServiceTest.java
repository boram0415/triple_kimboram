package com.example.triple.service;

import com.example.triple.domain.ReviewData;
import com.example.triple.mapper.ReviewMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;


@SpringBootTest
public class ReviewServiceTest {

    @Autowired
    ReviewMapper reviewMapper;

    @Test
    public void earmPoint() throws Exception {
        ReviewData data = new ReviewData();

        String img[] = {"sdssds","sdsds"};
        data.setType("REVIEW");
        data.setAction("ADD");
        data.setReviewId("B-0627");
//        data.setAttachedPhotoIds(3);
        data.setUserId("boram1");
        data.setPlaceId("busan");
        data.setContent("환상적입니다 !");

        try {
            reviewMapper.writeReview(data);
        } catch (DuplicateKeyException sql) {
            System.out.println("중복키 ERROR !! ");
        }

//        Spliterator<String> img2  = Arrays.stream(data.getAttachedPhotoIds()).spliterator();
        System.out.println("실행됨 ? " ) ;

    }

}