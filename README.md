# JPA 실습 - 순환 참조 문제 해결하기

JPA 실습을 위한 프로젝트입니다. 교실(ClassRoom)과 학생(Student) 엔티티를 사용하여 JPA의 다양한 기능을 연습합니다.

## 프로젝트 구조

```
src/main/java/com/example/jpapractice/
├── config/           # 설정 클래스
├── controller/       # REST API 컨트롤러
├── dto/             # 데이터 전송 객체
├── entity/          # JPA 엔티티
├── repository/      # JPA 리포지토리
└── service/         # 비즈니스 로직 서비스
```

## 주요 기능

### 엔티티 관계
- `ClassRoom`과 `Student`는 1:N 관계
- DTO 패턴을 사용하여 순환 참조 해결
  - `StudentDto`: 학생 정보와 필요한 교실 정보만 포함
  - `ClassRoomDto`: 교실 정보와 학생 목록 포함

### N+1 문제와 해결 방법

#### N+1 문제 발생 케이스
```java
// 1. 모든 반을 조회 (1번의 쿼리)
List<ClassRoom> classRooms = classRoomRepository.findAll();

// 2. 각 반의 학생들을 조회 (N번의 쿼리)
for (ClassRoom classRoom : classRooms) {
    List<Student> students = studentRepository.findByClassRoomId(classRoom.getId());
}
```

#### N+1 문제 해결 방법

1. **페치 조인 (Fetch Join)**
```java
@Query("SELECT DISTINCT s FROM Student s JOIN FETCH s.classRoom")
List<Student> findAllWithClassRoom();
```

2. **배치 사이즈 설정**
```properties
spring.jpa.properties.hibernate.default_batch_fetch_size=100
```

3. **엔티티 그래프 (Entity Graph)**
```java
@EntityGraph(attributePaths = {"classRoom"})
List<Student> findAll();
```

## 테스트 실행 방법

### N+1 문제 테스트
```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스만 실행
./gradlew test --tests "com.example.jpapractice.JpaNPlusOneTest"

# 특정 테스트 메서드만 실행
./gradlew test --tests "com.example.jpapractice.JpaNPlusOneTest.testNPlusOneProblem"
./gradlew test --tests "com.example.jpapractice.JpaNPlusOneTest.testNPlusOneSolution"
./gradlew test --tests "com.example.jpapractice.JpaNPlusOneTest.testNPlusOneSolutionWithBatchSize"
```

### 테스트 결과 확인
1. **N+1 문제 발생 케이스**
   - 콘솔에서 실행된 SQL 쿼리 수 확인
   - 각 반마다 추가 쿼리가 발생하는 것을 확인

2. **페치 조인 해결 케이스**
   - 단 한 번의 쿼리로 모든 데이터를 조회
   - 조인된 결과를 확인

3. **배치 사이즈 해결 케이스**
   - 배치 사이즈 설정에 따른 쿼리 최적화 확인
   - IN 절을 사용한 배치 조회 확인

### 테스트 로그 확인
```properties
# application.properties에 추가
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### API 엔드포인트

#### ClassRoom API
- `GET /api/classrooms` - 모든 교실 조회
- `GET /api/classrooms/{id}` - 특정 교실 조회
- `POST /api/classrooms` - 새 교실 생성
- `PUT /api/classrooms/{id}` - 교실 정보 수정
- `DELETE /api/classrooms/{id}` - 교실 삭제

#### Student API
- `GET /api/students` - 모든 학생 조회
- `GET /api/students/{id}` - 특정 학생 조회
- `POST /api/students` - 새 학생 생성
- `PUT /api/students/{id}` - 학생 정보 수정
- `DELETE /api/students/{id}` - 학생 삭제
- `GET /api/students/classroom/{classroomId}` - 특정 교실의 학생 목록 조회
- `GET /api/students/age/{age}` - 특정 나이의 학생 목록 조회

## 기술 스택

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- H2 Database
- Lombok
- Swagger (OpenAPI 3.0)

## 실행 방법

1. 프로젝트 클론
```bash
git clone [repository-url]
cd jpa-practice
```

2. 애플리케이션 실행
```bash
./gradlew bootRun
```

3. API 문서 확인
- Swagger UI: http://localhost:8080/swagger-ui.html

4. H2 Console
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: (비어있음)

## 주요 학습 포인트

1. JPA 엔티티 설계
   - 엔티티 간 관계 매핑
   - 단방향/양방향 관계 선택 기준
   - 순환 참조 해결

2. DTO 패턴 적용
   - 엔티티와 API 응답 분리
   - 필요한 데이터만 선택적 노출
   - API 버전 관리 용이성

3. Spring Data JPA 활용
   - 기본 CRUD 기능
   - 커스텀 쿼리 메서드
   - JPQL 쿼리
   - N+1 문제 해결

4. REST API 구현
   - RESTful API 설계
   - 컨트롤러 구현
   - 예외 처리
   - DTO 변환 로직

5. 성능 최적화
   - DTO를 통한 데이터 최적화
   - 필요한 필드만 조회
   - 쿼리 최적화
   - 지연 로딩 전략

## 순환 참조 해결 방법

1. **DTO 패턴 (현재 적용)**
   - 엔티티를 직접 노출하지 않고 DTO를 사용
   - 필요한 데이터만 선택적으로 포함
   - API 응답 구조를 명확하게 제어

2. **다른 해결 방법들**
   - MapStruct: DTO 변환 자동화
   - Projection: 필요한 필드만 선택적 조회
   - GraphQL: 클라이언트가 필요한 데이터만 요청
   - Response Entity Wrapper: 응답을 감싸는 래퍼 클래스 사용

## 실무 적용 팁

1. **단방향 관계 우선**
   - 양방향 관계는 필요한 경우에만 사용
   - 단방향으로 시작하고 필요시 양방향으로 전환
   - 연관관계의 주인을 명확히 설정

2. **성능 최적화**
   - 페치 조인 활용
   - 배치 사이즈 설정
   - 지연 로딩 전략 최적화
   - N+1 문제 해결 전략 수립

3. **유지보수성**
   - 명확한 네이밍 컨벤션
   - 적절한 주석과 문서화
   - 테스트 코드 작성
