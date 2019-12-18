package com.afkl.travel.exercise.resource.resourcehelper;

import com.afkl.travel.exercise.service.transferobjects.MetricTraficStatsTransferObject;
import com.afkl.travel.exercise.service.transferobjects.MetricTransferObjectPerURI;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Statistic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Component
@Slf4j
public class StatsResourceHelper {
    private final static String statusTag = "status";
    private final static String uriTag = "uri";
    private final static String methodTagKey = "method";

    private final Predicate<MetricTransferObjectPerURI> is5xx = (value) -> value.getResponseCode().is5xxServerError();
    private final Predicate<MetricTransferObjectPerURI> is2xx = (value) -> value.getResponseCode().equals(HttpStatus.OK);
    private final Predicate<MetricTransferObjectPerURI> is4xx = (value) -> value.getResponseCode().is4xxClientError();
    private final Predicate<MetricTransferObjectPerURI> xxxx = (value) -> true;

    public MetricTraficStatsTransferObject getAggregatedMetricResponse(List<MetricTransferObjectPerURI> requestMetricsList) {

        Long sumOf5xxRequest = getTotalNumberOfRequestForStatusCode(requestMetricsList, is5xx);
        Long sumOfOkRequest = getTotalNumberOfRequestForStatusCode(requestMetricsList, is2xx);
        Long sumOf4xxRequest = getTotalNumberOfRequestForStatusCode(requestMetricsList, is4xx);
        Long totalNumberOfRequest = getTotalNumberOfRequestForStatusCode(requestMetricsList, xxxx);
        double maximumResponseTimeOfRequest = requestMetricsList.stream().filter(Objects::nonNull).mapToDouble
                (MetricTransferObjectPerURI::getMaximumResponseTime).max().orElse(Double.NaN);
        DoubleSummaryStatistics stats = requestMetricsList.stream().filter(Objects::nonNull).mapToDouble
                (MetricTransferObjectPerURI::getTotalTimeOfAllRequest).summaryStatistics();
        return MetricTraficStatsTransferObject.builder().totalNumberOf5xxRequest(sumOf5xxRequest)
                .maxResponseTime(maximumResponseTimeOfRequest)
                .averageResponseTime(stats.getAverage())
                .totalNumberof200Request(sumOfOkRequest)
                .totalNumberOf4xxRequest(sumOf4xxRequest)
                .totalNumberOfRequest(totalNumberOfRequest).build();
    }

    private Long getTotalNumberOfRequestForStatusCode(List<MetricTransferObjectPerURI> metricListPerURL,
                                                      Predicate<MetricTransferObjectPerURI> predicate) {
        return metricListPerURL.stream().filter(Objects::nonNull)
                .filter(predicate)
                .mapToLong(MetricTransferObjectPerURI::getCountOfRequest).sum();
    }


    public MetricTransferObjectPerURI getMetricTransferObjectPerRequest(Meter meter) {
        MetricTransferObjectPerURI metricTransferObject = new MetricTransferObjectPerURI();
        Iterable<Measurement> statsMeasure = meter.measure();
        metricTransferObject.setCountOfRequest(getValueForProvidedStat(statsMeasure, Statistic.COUNT).longValue());
        Meter.Id id = meter.getId();
        metricTransferObject.setTotalTimeOfAllRequest(getValueForProvidedStat(statsMeasure, Statistic.TOTAL_TIME));
        metricTransferObject.setMaximumResponseTime(getValueForProvidedStat(statsMeasure,Statistic.MAX));
        metricTransferObject.setResponseCode(HttpStatus.valueOf(Objects.requireNonNull(Integer.valueOf(Objects.requireNonNull(id.getTag(statusTag))))));
        metricTransferObject.setUrl(id.getTag(uriTag));
        metricTransferObject.setMethod(id.getTag(methodTagKey));
        return metricTransferObject;
    }

    private Double getValueForProvidedStat(Iterable<Measurement> measure, Statistic statistic) {
        List<Measurement> measurementList = StreamSupport.stream(measure.spliterator(), false)
                .collect(Collectors.toList());
        Optional<Measurement> value = measurementList.stream().filter(obj -> statistic.equals(obj.getStatistic()))
                .findFirst();
        return value.get().getValue();
    }
}

