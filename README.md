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

### 관계 매핑 전략

#### 양방향 관계의 문제점
1. **성능 이슈**
   - N+1 문제 발생 가능성 증가
   - 불필요한 조인 쿼리 발생
   - 지연 로딩 시 프록시 객체 관리 복잡성

2. **유지보수 어려움**
   - 양쪽 엔티티의 상태 동기화 필요
   - 연관관계 편의 메서드 관리 복잡
   - 순환 참조 문제 발생

3. **쿼리 최적화 어려움**
   - 불필요한 조인 발생
   - 페치 전략 설정 복잡
   - 쿼리 튜닝 어려움

#### 단방향 관계의 장점
1. **성능 최적화**
   - 필요한 방향으로만 조회 가능
   - 불필요한 조인 제거
   - 쿼리 최적화 용이

2. **유지보수 용이**
   - 코드 복잡도 감소
   - 상태 관리 단순화
   - 순환 참조 문제 해결

3. **확장성**
   - 새로운 요구사항에 대한 유연한 대응
   - 마이크로서비스 아키텍처에 적합
   - API 응답 구조 단순화

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

3. **유지보수성**
   - 명확한 네이밍 컨벤션
   - 적절한 주석과 문서화
   - 테스트 코드 작성
