package com.example.triple.controller;

import com.example.triple.domain.ReviewData;
import com.example.triple.service.ReviewService;
import com.example.triple.util.ReviewUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Spliterator;

@Slf4j
@RestController
public class ReviewController {


    ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 포인트 적립
    @PostMapping("/events")
    public String earnPoint(@RequestBody ReviewData data){
        return reviewService.events(data);
    }

    // 포인트 조회
    @GetMapping("/events/pointCheck")
    public String pointCheck(@RequestParam String userId){
        return reviewService.pointCheck(userId);
    }

}
