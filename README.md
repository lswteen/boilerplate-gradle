# boilerplate-gradle
boilerplate-gradle java21 spring 3.5.8

```text
boilerplate-gradle/
├── settings.gradle           # 멀티모듈 설정
├── build.gradle             # root 공통 설정
├── common-events/           # 공통 이벤트 모듈
│   └── build.gradle
├── order-service/           # 주문 서비스
│   └── build.gradle
├── payment-service/         # 결제 서비스
│   └── build.gradle
├── delivery-service/        # 배달 서비스
│   └── build.gradle
└── kafka/
    └── docker-compose.yml
```

# SERVER 구성
## common-event 모듈 구조

```text
common-events/
└── src/main/java/com/farfarcoder/events/   # 이벤트 DTO 클래스 작성
    ├── OrderCreatedEvent.java
    ├── PaymentCompletedEvent.java
    ├── PaymentFailedEvent.java
    ├── DeliveryStartedEvent.java
    └── OrderStatus.java (Enum)    # 공통 상수 정의
```

## delivery-service

## order-service

## payment-service


## Gradle 주요 명령어 비교
```text
클린 빌드 ./gradlew clean
컴파일 ./gradlew compileJava
테스트 ./gradlew test
빌드 ./gradlew build
런 ./gradlew bootRun
JAR ./gradlew bootJar
테스트 ./gradlew bootTest
```