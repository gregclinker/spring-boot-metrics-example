package com.essexboy;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.config.MeterFilter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;

@Service
public class MetricsService {
    @Autowired
    MeterRegistry registry;

    private Map<String, Map<String, Double>> fullMetricsMap = new HashMap<>();

    @PostConstruct
    public void init() {
        registry.config()
                .meterFilter(MeterFilter.denyUnless(new Predicate<Meter.Id>() {
                    @Override
                    public boolean test(Meter.Id id) {
                        return List.of("pod_cpu_usage", "pod_memory_usage").contains(id.getName());
                    }
                }));
    }

    @Scheduled(fixedDelay = 1000)
    public void loadMetrics() {
        setMetric("pod_cpu_usage", "pod_name", "pod1", getTestDouble());
        setMetric("pod_cpu_usage", "pod_name", "pod2", getTestDouble());
        setMetric("pod_cpu_usage", "pod_name", "pod3", getTestDouble());
        setMetric("pod_memory_usage", "pod_name", "pod1", getTestDouble());
        setMetric("pod_memory_usage", "pod_name", "pod2", getTestDouble());
        setMetric("pod_memory_usage", "pod_name", "pod3", getTestDouble());
    }

    public void setMetric(String metricName, String tagName, String tagValue, Double value) {
        if (fullMetricsMap.get(metricName) == null) {
            fullMetricsMap.put(metricName, new HashMap<>());
        }
        final Map<String, Double> singleMetricMap = fullMetricsMap.get(metricName);
        String metricsKey = tagName + "_" + tagValue;
        if (!singleMetricMap.containsKey(metricsKey)) {
            Tags tags = Tags.of(tagName, tagValue);
            Gauge.builder(metricName, singleMetricMap, map -> map.get(metricsKey))
                    .tags(tags)
                    .register(registry);
        }
        singleMetricMap.put(metricsKey, value);
    }

    private double getTestDouble() {
        return new BigDecimal(Math.random() * (100000L)).setScale(0, RoundingMode.UP).doubleValue();
    }
}
