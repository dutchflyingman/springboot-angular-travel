package com.afkl.travel.exercise.service.transferobjects;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class MetricTransferObjectPerResponseCode {

      double totalTimePerRequest;
      double maxTimePerRequest;
      int totalNumberOfRequestWithResponseCode;
      HttpStatus httpStatus;
}
