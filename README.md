# 11st Wiki!!!
## API 문서
- https://github.com/chasw0326/11st/wiki/API-%EB%AC%B8%EC%84%9C

## 테스트 내용
- https://github.com/chasw0326/11st/wiki/%ED%85%8C%EC%8A%A4%ED%8A%B8-%EB%82%B4%EC%9A%A9

## 테이블
- https://github.com/chasw0326/11st/wiki/%ED%85%8C%EC%9D%B4%EB%B8%94

## 설계 아이디어
- https://github.com/chasw0326/11st/wiki/%EC%84%A4%EA%B3%84-%EC%95%84%EC%9D%B4%EB%94%94%EC%96%B4

---

# 실행방법
1. redis실행
2. git클론
3. cd 클론받은 디렉토리 경로
4. 빌드
2. .jar실행

```text
redis-server
git clone
cd [디렉토리 경로]
./gradlew clean build
cd build/libs
java -jar _11st-0.0.1-SNAPSHOT.jar

```

# 사용기술
- Spring boot 2.6.7
- Java 11
- Redis
- JPA
- Mysql
- H2
- Jacoco
- SonarLint
- Junit5
- Mockito, BDD
- Java Docs
- WebTestClient

# 개선할 점(To do)
- mock을 통해 격리하여 진정한 단위테스트로 수정할 것
- 주문-재고에 대한 동시성 제어 추가할 것
    - 재고가 0 미만이 되는 상황이면 크리티컬 섹션을 만들고 비관적락으로 순차적으로 진행되게 하여 경쟁조건을 타파하면 어떨까?
