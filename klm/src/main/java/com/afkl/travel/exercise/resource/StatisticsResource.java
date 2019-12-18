package com.afkl.travel.exercise.resource;

import com.afkl.travel.exercise.resource.resourcehelper.StatsResourceHelper;
import com.afkl.travel.exercise.service.transferobjects.MetricTraficStatsTransferObject;
import com.afkl.travel.exercise.service.transferobjects.MetricTransferObjectPerURI;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@Slf4j
@RequestMapping("/admin/actuator")
public class StatisticsResource {

    private static final Logger LOGGER= LoggerFactory.getLogger(StatisticsResource.class);

    private final MeterRegistry registry;
    private final StatsResourceHelper statsResourceHelper;
    public StatisticsResource(MeterRegistry registry, StatsResourceHelper statsResourceHelper) {
        this.registry = registry;
        this.statsResourceHelper = statsResourceHelper;
    }

    @GetMapping(value = "/metrics")
    public ResponseEntity<MetricTraficStatsTransferObject> getMetrics(Authentication authentication) {
        boolean hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        if(!hasUserRole) return ResponseEntity.status(FORBIDDEN).build();
        LOGGER.info("In Stats Traffic Resource to gather metric information for incoming request");
        List<MetricTransferObjectPerURI> metricListPerURL;
        List<Meter> metersList = new ArrayList<>(registry.get("http.server.requests").meters());
        metricListPerURL = metersList.stream().filter(Objects::nonNull)
                .map(this.statsResourceHelper::getMetricTransferObjectPerRequest).collect(Collectors.toList());
       log.debug("Metrics List For Each URI ,{}", metricListPerURL);
       MetricTraficStatsTransferObject metricTraficStatsTransferObject =statsResourceHelper.getAggregatedMetricResponse(metricListPerURL);
       log.info("Metric Summary of Traffic Information ,{}", metricTraficStatsTransferObject);
        return ResponseEntity.ok(metricTraficStatsTransferObject);
    }

}
