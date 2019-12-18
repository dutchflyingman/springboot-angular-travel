package com.afkl.travel.exercise.service.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetricTraficStatsTransferObject {


    @JsonProperty("Total number of requests processed")
    Long totalNumberOfRequest;

    @JsonProperty("Total number of requests resulted in a OK response")
    Long totalNumberof200Request;

    @JsonProperty("Total number of requests resulted in a 4xx response")
    Long totalNumberOf4xxRequest;

    @JsonProperty("Total number of requests resulted in a 5xx response")
    Long totalNumberOf5xxRequest;

    @JsonProperty("Average response time of all requests")
    Double averageResponseTime;

    @JsonProperty("Max response time of all requests")
    Double maxResponseTime;

}
