# spring-boot-metrics-example

## Create Promethesu Metrics 
```
# HELP pod_memory_usage
# TYPE pod_memory_usage gauge
pod_memory_usage{pod_name="pod2",} 6544.0
pod_memory_usage{pod_name="pod3",} 50235.0
pod_memory_usage{pod_name="pod1",} 73336.0
# HELP pod_cpu_usage
# TYPE pod_cpu_usage gauge
pod_cpu_usage{pod_name="pod2",} 36002.0
pod_cpu_usage{pod_name="pod3",} 6687.0
pod_cpu_usage{pod_name="pod1",} 61246.0
```
