# Kafka & Spring Boot Monitoring

Kafka와 Spring Boot 애플리케이션의 메트릭을 Prometheus로 수집하고 Grafana로 시각화하는 통합 모니터링 환경입니다.

---

## 🧱 구성 요소

| 구성 요소              | 설명                                                           |
| ------------------ | ------------------------------------------------------------ |
| **Kafka**          | Bitnami Kafka 이미지 사용. Kafka Exporter와 JMX Exporter로 메트릭 수집   |
| **Zookeeper**      | Kafka 브로커 클러스터 관리를 위한 의존 구성요소                                |
| **Spring Boot**    | Actuator Prometheus endpoint로 메트릭 제공 및 대량 메시지 트래픽 테스트 API 포함 |
| **Prometheus**     | Kafka, JMX, Spring Boot 메트릭 수집                               |
| **Grafana**        | 수집된 메트릭 시각화. provisioning을 통해 대시보드 자동 로드                     |
| **JMX Exporter**   | Kafka JVM 및 브로커 내부 상태 메트릭 노출                                 |
| **Kafka Exporter** | Consumer group, lag, partition 상태 등 Kafka 관련 메트릭 수집          |

---

## 📊 Grafana 대시보드 구성

### Kafka Monitoring

| 대시보드                 | 설명                                                           |
| -------------------- | ------------------------------------------------------------ |
| kafka-dashboard.json | Kafka JMX 기반 브로커 메트릭 시각화 (메시지/바이트 in-out, GC, replication 등) |
| kafka-overview\.json | Kafka Exporter 기반 메트릭 시각화 (consumer group, lag, partition 등) |

### Spring Boot Monitoring

| 대시보드                       | 설명                                               |
| -------------------------- | ------------------------------------------------ |
| spring-boot-dashboard.json | JVM, HTTP 요청, GC, thread 메트릭 시각화 (Micrometer 기반) |

---

## 🧪 테스트 방법

컨트롤러 API를 통해 Kafka에 대량의 테스트 메시지를 비동기 병렬로 전송할 수 있습니다.

* 총 100,000건의 메시지를 10개의 스레드가 병렬로 처리
* Kafka Exporter와 Grafana 대시보드에서 실시간 consumer lag 및 처리량 확인 가능

---

## 🔍 간단한 문제 해결 요약

| 문제                         | 해결 방법                             |
| -------------------------- | --------------------------------- |
| kafka\_exporter에서 메트릭 안 나옴 | `--kafka.version` 명시로 해결          |
| topic 필터 동작 안 함            | `label_values(...)` 변수 정의 추가      |
| lag이 항상 0                  | consumer group에서 실제 consume 발생 필요 |
| Spring Boot 메트릭 누락         | actuator prometheus endpoint 활성화  |
| JMX 메트릭 누락                 | Prometheus job 및 agent jar 설정 확인  |

---

## 📁 디렉토리 구조 요약

```
grafana/
├── dashboards/
│   ├── kafka/
│   │   ├── kafka-dashboard.json
│   │   └── kafka-overview.json
│   └── spring-boot/
│       └── spring-boot-dashboard.json
├── provisioning/
│   ├── dashboards/
│   │   └── dashboards.yml
│   └── datasources/
│       └── datasource.yml
jmx/
├── kafka.yml
├── jmx_prometheus_javaagent-0.20.0.jar
prometheus/
src/
└── main/kotlin/com/github/minsoozz/monitoring
    ├── controller/TestController.kt
    └── consumer/KafkaMessageListener.kt
```

---

## 🚀 실행 방법

```bash
docker-compose up -d
```

### 서비스 주소

| 서비스             | 주소                                                                                                           |
| --------------- | ------------------------------------------------------------------------------------------------------------ |
| Prometheus      | [http://localhost:9090](http://localhost:9090)                                                               |
| Grafana         | [http://localhost:3000](http://localhost:3000) (`admin` / `admin`)                                           |
| Spring Boot 메트릭 | [http://host.docker.internal:8080/actuator/prometheus](http://host.docker.internal:8080/actuator/prometheus) |
| Kafka JMX 메트릭   | [http://localhost:7071/metrics](http://localhost:7071/metrics)                                               |
| 트래픽 테스트 API     | `POST http://localhost:8080/api/traffic` (JSON Body: Member 객체 포함)                                           |