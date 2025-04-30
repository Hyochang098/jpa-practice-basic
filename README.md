# JPA 실습 - 순환 참조 문제 해결하기

## 현재 발생하는 문제

### 1. 순환 참조 오류
```json
{
    "timestamp": "2025-04-30T15:45:14.678+09:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Type definition error: [simple type, class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor]",
    "path": "/api/students/1"
}
```

### 2. Hibernate 프록시 직렬화 오류
```
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: 
No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor 
and no properties discovered to create BeanSerializer
```

## 해결해야 할 과제

### 1. 엔티티 수정
- `Student`와 `ClassRoom` 엔티티의 양방향 연관관계 설정
- 순환 참조를 방지하기 위한 Jackson 어노테이션 추가
- 지연 로딩 관련 설정 최적화

### 2. 테스트 케이스 작성
```java
@Test
void testCircularReference() {
    // 1. 반 생성
    ClassRoom classRoom = new ClassRoom();
    classRoom.setName("1반");
    classRoom.setCapacity(30);
    classRoom.setTeacherName("김선생");
    ClassRoom savedClassRoom = classRoomRepository.save(classRoom);

    // 2. 학생 생성
    Student student = new Student();
    student.setName("홍길동");
    student.setAge(15);
    student.setClassRoom(savedClassRoom);
    Student savedStudent = studentRepository.save(student);

    // 3. API 호출 테스트
    // GET /api/students/{id} 호출 시 순환 참조 오류 발생 확인
    // GET /api/classrooms/{id} 호출 시 순환 참조 오류 발생 확인
}
```

### 3. 해결 방안 고민
1. `@JsonIgnore` 사용
2. `@JsonManagedReference`와 `@JsonBackReference` 사용
3. DTO 패턴 적용
4. Jackson 설정 변경

## 학습 목표
1. JPA의 지연 로딩과 프록시 객체 이해
2. Jackson 직렬화 과정에서의 순환 참조 문제 이해
3. 다양한 해결 방안의 장단점 비교
4. REST API에서의 엔티티 직렬화 최적화

## 체크리스트
- [ ] 엔티티 수정
- [ ] 테스트 케이스 작성
- [ ] 해결 방안 구현
- [ ] API 테스트
- [ ] 문서화

## 참고 자료
- [Jackson 직렬화 문서](https://github.com/FasterXML/jackson-docs)
- [Hibernate 프록시 문서](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#associations)
- [Spring Data JPA 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)

이 브랜치에서 순환 참조 문제를 해결하면서 JPA와 Jackson의 동작 방식을 깊이 이해할 수 있습니다. 다양한 해결 방안을 시도해보고 각각의 장단점을 비교해보세요.
