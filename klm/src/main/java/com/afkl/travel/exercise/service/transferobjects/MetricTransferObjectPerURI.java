package com.afkl.travel.exercise.service.transferobjects;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class MetricTransferObjectPerURI {

    Double totalTimeOfAllRequest;
    Long countOfRequest;
    String value;
    String url;
    HttpStatus responseCode;
    String method;
    Double maximumResponseTime;

}
