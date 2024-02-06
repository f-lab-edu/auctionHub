# auctionHub

시작가 천원!  누구나 참여할 수 있는 경매 어플리케이션.

### 사용기술 및 개발환경

- Spring Boot
- Java 17
- MySQL
- Mybatis
- Gradle
- Jenkins
- naver cloud platform
- IntelliJ

### 프로젝트 주요 관심사

- 기술을 사용할때 기술에 대한 차이를 비교하고 선택한 이유에 대해 기준을 정하도록 고민하였습니다.
- 결함없는 안정적인 서비스를 위해 코드 커버리지는 70%를 유지하였습니다.
- 클린코드를 위한 꾸준한 코드 리팩토링을 진행 중입니다.
- 객체지향적 개념을 이해하고 이를 코드에 녹여내어 의미있는 설계를 지향하도록 노력하였습니다.
- 대용량 트래픽 상황까지 고려한 기능을 구현하도록 노력하였습니다.

### 기술적 issue해결과정

Jenkins CI 구성하기

- https://systemdata.tistory.com/81

Validation 책임과 범위는 어떻게 가져가야할까?

- https://systemdata.tistory.com/82

프로젝트 기술 스택 선정

- Gradle vs Maven (https://github.com/Jammini/TIL/blob/master/spring/mavenvsgradle.md)

### WIKI

- [기능 리스트](https://github.com/f-lab-edu/auctionHub/wiki/Feature-List)
- [UseCase](https://github.com/f-lab-edu/auctionHub/wiki/Use-Case)
- [Project Convention](https://github.com/f-lab-edu/auctionHub/wiki/Project-Convention)

### Github-Flow 전략

![image](https://github.com/f-lab-edu/auctionHub/assets/59176149/99c62317-3283-4dac-b472-d7d6f8d30b12)

## Commit Message Convention

커밋 메세지는 다음과 같은 형식으로 작성한다.

[TYPE] COMMIT_MESSAGE

- [TYPE]
    - feature : 새로운 기능 추가
    - fix : 버그 수정
    - docs : 문서 업데이트
    - refactor : 코드의 리팩토링
    - test : 테스트코드 업데이트
    - env : 환경 구축
- COMMIT_MESSAGE
    - TYPE을 기입했다면, 그 이후에는 COMMIT_MESSAGE 부분에 해당 커밋에 대한 내용을 간략하게 기술

### Jenkins CI/CD

젠킨스 주소 : http://27.96.134.143:18080/

### DB ERD 구조

![image](https://github.com/f-lab-edu/auctionHub/assets/59176149/68f780dd-260c-4fe0-8501-24a0c79853a3)
