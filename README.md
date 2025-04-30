# JPA 실습 - 순환 참조 문제 해결하기

JPA 실습을 위한 프로젝트입니다. 교실(ClassRoom)과 학생(Student) 엔티티를 사용하여 JPA의 다양한 기능을 연습합니다.

## 프로젝트 구조

```
src/main/java/com/example/jpapractice/
├── config/           # 설정 클래스
├── controller/       # REST API 컨트롤러
├── entity/          # JPA 엔티티
├── repository/      # JPA 리포지토리
└── service/         # 비즈니스 로직 서비스
```

## 주요 기능

### 엔티티 관계
- `ClassRoom`과 `Student`는 1:N 관계
- `@JsonManagedReference`와 `@JsonBackReference`를 사용하여 순환 참조 해결
  - `ClassRoom` -> `Student` (정방향): `@JsonManagedReference`
  - `Student` -> `ClassRoom` (역방향): `@JsonBackReference`

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
   - 양방향 관계 설정
   - 순환 참조 해결

2. Spring Data JPA 활용
   - 기본 CRUD 기능
   - 커스텀 쿼리 메서드
   - JPQL 쿼리

3. REST API 구현
   - RESTful API 설계
   - 컨트롤러 구현
   - 예외 처리

4. 성능 최적화
   - 순환 참조 해결
   - JSON 직렬화 최적화
   - 쿼리 최적화
