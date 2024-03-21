# auctionHub

시작가 천원!  누구나 참여할 수 있는 경매 어플리케이션.

---
### 🚩 서버 구조도


<img width="850" alt="image" src="https://github.com/f-lab-edu/auctionHub/assets/59176149/e9109f28-0923-4423-8e2e-35ba9ecd1815">

---
### 🚩 사용기술 및 개발환경

- Spring Boot 3.x
- Java 17
- MySQL
- Mybatis
- Gradle
- Jenkins
- naver cloud platform
- Junit5

---
### 🚩 프로젝트 주요 관심사

- 기술을 사용할 때 기술에 대한 차이를 비교하고 선택한 이유에 대해 기준을 정하도록 고민하였습니다.
- 결함없는 안정적인 서비스를 위해 코드 커버리지는 80%를 유지하였습니다.
- 클린코드를 위한 꾸준한 코드 리팩토링을 진행하였습니다.
- 객체지향적 개념을 이해하고 이를 코드에 녹여내어 의미있는 설계를 지향하였습니다.
- 단순한 기능 구현뿐 아니라 대용량 트래픽 상황까지 고려한 기능을 구현하였습니다.
- 문서화, 테스트코드의 작성을 높은 우선순위를 두었고, CI/CD를 구현하여 쉽게 협업이 가능한 프로젝트로 만들었습니다.

---
### 🚩 기술적 issue 해결 과정

- 대용량 트래픽 처리를 위한 경매 기능 최적화

    https://systemdata.tistory.com/93

- RabbitMQ 메세지큐 적용으로 성능 개선하기

    https://systemdata.tistory.com/92

- Redis 캐시 적용으로 성능 개선하기

    https://systemdata.tistory.com/91

- Validation 책임과 범위는 어떻게 가져가야할까?

    https://systemdata.tistory.com/82

- Scale Out 상황시 세션불일치 방지를 위한 Redis 선정 과정

    https://systemdata.tistory.com/87

- 테이블마다 id 칼럼을 PK로 사용한 이유는 무엇일까?

  https://systemdata.tistory.com/84

- Auditing을 고려한 테이블 설계 과정

  https://systemdata.tistory.com/88

- 젠킨스를 통한 CI/CD 구성하기 - CI 구축 과정

  https://systemdata.tistory.com/81

- 젠킨스를 통한 CI/CD 구성하기 - CD 구축 과정

  https://systemdata.tistory.com/83

- Maven이 아닌 Gradle 선정 과정
  
  https://github.com/Jammini/TIL/blob/master/spring/mavenvsgradle.md

---
### 🚩 경매 입찰 과정 구조도

<img width="761" alt="image" src="https://github.com/Jammini/TIL/assets/59176149/3d93d757-6ed0-41c9-aab1-d9fa2e2844d8">

---
### 🚩 DB ERD 구조

![image](https://github.com/f-lab-edu/auctionHub/assets/59176149/68f780dd-260c-4fe0-8501-24a0c79853a3)

---
### 🚩 WIKI

- [기능 리스트](https://github.com/f-lab-edu/auctionHub/wiki/Feature-List)
- [UseCase](https://github.com/f-lab-edu/auctionHub/wiki/Use-Case)
- [Project Convention](https://github.com/f-lab-edu/auctionHub/wiki/Project-Convention)
