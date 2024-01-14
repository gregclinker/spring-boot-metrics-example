package com.essexboy;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.config.MeterFilter;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Getter
public class MetricsService {
    @Autowired
    MeterRegistry registry;
    private final Map<String, Map<String, Double>> fullMetricsMap = new HashMap<>();

    @PostConstruct
    public void init() {
        registry.config()
                .meterFilter(MeterFilter.denyUnless(new Predicate<Meter.Id>() {
                    @Override
                    public boolean test(Meter.Id id) {
                        // switch off all metrics except these
                        return List.of("pod_cpu_usage", "pod_memory_usage", "container_memory_usage", "memory_usage").contains(id.getName());
                    }
                }));
    }

    @Scheduled(fixedDelay = 2000)
    public void loadMetrics() {

        final HashMap<String, String> objectObjectHashMap = new HashMap<>();
        setMetric("pod_cpu_usage", "pod cpu usage in mCPU", Map.of("pod_name", "pod1"), getTestDouble());
        setMetric("pod_cpu_usage", "pod cpu usage in mCPU", Map.of("pod_name", "pod2"), getTestDouble());
        setMetric("pod_cpu_usage", "pod cpu usage in mCPU", Map.of("pod_name", "pod3"), getTestDouble());
        setMetric("pod_memory_usage", "pod memory usage in bytes", Map.of("pod_name", "pod1"), getTestDouble());
        setMetric("pod_memory_usage", "pod memory usage in bytes", Map.of("pod_name", "pod2"), getTestDouble());
        setMetric("pod_memory_usage", "pod memory usage in bytes", Map.of("pod_name", "pod3"), getTestDouble());
        setMetric("container_memory_usage", "container memory usage in bytes", Map.of("pod_name", "pod1", "container_name", "container1"), getTestDouble());
        setMetric("container_memory_usage", "container memory usage in bytes", Map.of("pod_name", "pod2", "container_name", "container2"), getTestDouble());
        setMetric("container_memory_usage", "container memory usage in bytes", Map.of("pod_name", "pod3", "container_name", "container3"), getTestDouble());
        setMetric("memory_usage", "pod or container memory usage in bytes", Map.of("type", "pod", "pod_name", "pod1", "container_name", "N/A"), getTestDouble());
        setMetric("memory_usage", "pod or container memory usage in bytes", Map.of("type", "pod", "pod_name", "pod2", "container_name", "N/A"), getTestDouble());
        setMetric("memory_usage", "pod or container memory usage in bytes", Map.of("type", "pod", "pod_name", "pod3", "container_name", "N/A"), getTestDouble());
        setMetric("memory_usage", "pod or container memory usage in bytes", Map.of("type", "container", "pod_name", "pod1", "container_name", "container1"), getTestDouble());
        setMetric("memory_usage", "pod or container memory usage in bytes", Map.of("type", "container", "pod_name", "pod2", "container_name", "container2"), getTestDouble());
        setMetric("memory_usage", "pod or container memory usage in bytes", Map.of("type", "container", "pod_name", "pod3", "container_name", "container3"), getTestDouble());
    }

    public void setMetric(String metricName, String help, Map<String, String> tagMap, Double value) {
        if (fullMetricsMap.get(metricName) == null) {
            fullMetricsMap.put(metricName, new HashMap<>());
        }
        final Map<String, Double> singleMetricMap = fullMetricsMap.get(metricName);
        // the key of the metric used to manage the map of metrics data
        final String metricsKey = tagMap.keySet().stream().map(k -> k + "_" + tagMap.get(k)).collect(Collectors.joining("_"));
        if (!singleMetricMap.containsKey(metricsKey)) {
            final List<Tag> tags = tagMap.keySet().stream().map(k -> Tag.of(k, tagMap.get(k))).collect(Collectors.toList());
            Gauge.builder(metricName, singleMetricMap, map -> map.get(metricsKey))
                    .tags(tags)
                    .description(help)
                    .register(registry);
        }
        singleMetricMap.put(metricsKey, value);
    }

    private double getTestDouble() {
        return BigDecimal.valueOf(Math.random() * (100000L)).setScale(0, RoundingMode.UP).doubleValue();
    }
}
