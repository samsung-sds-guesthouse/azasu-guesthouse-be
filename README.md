# Azasu Guesthouse Backend

아자스 게스트하우스 예약 관리 시스템의 백엔드 서버입니다.
게스트의 객실 조회/예약과 관리자의 객실·예약 관리 기능을 REST API로 제공합니다.

---

## 팀원 및 역할

| 역할 | 담당자 | 담당 모듈 |
|------|--------|-----------|
| 회원 (Member) | 백민규 프로 | 회원가입, 로그인/로그아웃, 계정 관리, 인증/보안 |
| 게스트 (Guest) | 신진건 프로 | 객실 검색/조회, 예약 생성/조회/취소 |
| 관리자 (Admin) | 심현우 프로 | 객실 등록/수정/관리, 예약 상태 관리 |

---

## 기술 스택

### Backend
![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_4.0.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis_4.0.1-000000?style=for-the-badge&logo=mybatis&logoColor=white)

### Database
![MySQL](https://img.shields.io/badge/MySQL_8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

### Build & Infra
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker_Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)

### API Docs & Auth
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Session](https://img.shields.io/badge/Session_Auth-FF6B6B?style=for-the-badge&logo=shield&logoColor=white)

---

## 주요 기능

### 회원 (Member) — 백민규 프로
- 회원가입 / 로그인 / 로그아웃
- 아이디 중복 확인
- 내 정보 조회 / 비밀번호 변경 / 회원 탈퇴
- 비밀번호 Salt 해싱 및 로그인 실패 횟수 추적

### 게스트 (Guest) — 신진건 프로
- 체크인·체크아웃 날짜 및 인원수 기반 객실 검색
- 객실 상세 조회 (예약 불가 날짜 포함)
- 비활성화 객실 조회 (로그인 필요)
- 예약 생성 / 목록 조회 (페이지네이션) / 예약 취소

### 관리자 (Admin) — 심현우 프로
- 객실 등록 / 목록 조회 / 상세 조회 / 정보 수정 / 활성화·비활성화
- 이미지 업로드 (최대 15MB, 파일 타입 검증)
- 전체 예약 목록 조회 (페이지네이션) / 예약 상태 변경

---

## 프로젝트 구조

```
src/main/java/com/samsung/azasuguesthouse/
├── admin/
│   ├── room/           # 관리자 객실 관리 (Controller, Service, Mapper, DTO)
│   └── reservation/    # 관리자 예약 관리
├── guest/              # 게스트 객실·예약 조회 및 예약
├── member/             # 회원 인증 및 계정 관리
├── entity/             # 도메인 엔티티 (member, room, reservation)
└── common/
    ├── auth/           # 세션 인증·관리자 인터셉터
    ├── cache/          # 예약일자·객실 인메모리 캐시
    ├── config/         # CORS, MVC, Multipart 설정
    ├── crypt/          # 비밀번호 해싱
    ├── exception/      # 전역 예외 처리
    ├── log/            # 요청 로깅 인터셉터
    └── response/       # 공통 응답 포맷

src/main/resources/
├── application.properties
├── config/secu.properties   # DB 접속 정보
├── logback-spring.xml
└── mapper/                  # MyBatis XML 매퍼
```

---

## API 엔드포인트

### 인증 (`/api/v1/auth`)
| Method | URL | 인증 | 설명 |
|--------|-----|------|------|
| POST | `/login` | - | 로그인 |
| POST | `/logout` | 필요 | 로그아웃 |
| POST | `/signup` | - | 회원가입 |
| GET | `/duplicate-id` | - | 아이디 중복 확인 |
| GET | `/my-info` | 필요 | 내 정보 조회 |
| POST | `/change-pw` | 필요 | 비밀번호 변경 |
| POST | `/withdraw` | 필요 | 회원 탈퇴 |

### 게스트 객실 (`/api/v1/rooms`)
| Method | URL | 인증 | 설명 |
|--------|-----|------|------|
| GET | `/rooms` | - | 객실 검색 (날짜, 인원 필터) |
| GET | `/rooms/{id}` | - | 객실 상세 조회 |
| GET | `/rooms/inactive/{id}` | 필요 | 비활성화 객실 조회 |

### 게스트 예약 (`/api/v1/reservations`)
| Method | URL | 인증 | 설명 |
|--------|-----|------|------|
| GET | `/reservations/me?page=X` | 필요 | 내 예약 목록 |
| POST | `/reservations` | 필요 | 예약 생성 |
| POST | `/reservations/{id}/delete` | 필요 | 예약 취소 |

### 관리자 객실 (`/api/v1/admin/rooms`)
| Method | URL | 권한 | 설명 |
|--------|-----|------|------|
| POST | `/admin/rooms` | ADMIN | 객실 등록 (이미지 포함) |
| GET | `/admin/rooms` | ADMIN | 전체 객실 목록 |
| GET | `/admin/rooms/{id}` | ADMIN | 객실 상세 조회 |
| POST | `/admin/rooms/{id}/modify` | ADMIN | 객실 정보 수정 |
| POST | `/admin/rooms/{id}/activation` | ADMIN | 활성화/비활성화 토글 |

### 관리자 예약 (`/api/v1/admin/reservations`)
| Method | URL | 권한 | 설명 |
|--------|-----|------|------|
| GET | `/admin/reservations?page=X` | ADMIN | 전체 예약 목록 |
| POST | `/admin/reservations/{id}/modify` | ADMIN | 예약 상태 변경 |

---

## 실행 방법

### 사전 요구사항
![Java](https://img.shields.io/badge/JDK_17+-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Docker](https://img.shields.io/badge/Docker_required-2496ED?style=flat-square&logo=docker&logoColor=white)

### 1. 데이터베이스 실행

```bash
docker-compose up -d
```

MySQL 8.0 컨테이너가 포트 `3307`로 실행됩니다.
데이터베이스: `guesthouse` / 사용자: `sds` / 비밀번호: `1234`

### 2. 애플리케이션 빌드 및 실행

```bash
# 빌드
./mvnw clean package

# 실행
./mvnw spring-boot:run

# 또는 JAR 직접 실행
java -jar target/azasuguesthouse-0.0.1-SNAPSHOT.jar
```

Windows 환경에서는 `mvnw.cmd`를 사용합니다.

### 3. 접속 정보
- API 서버: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Docs: `http://localhost:8080/v3/api-docs`

---

## 보안

- **세션 인증**: `SessionCheckInterceptor`로 보호된 엔드포인트에 로그인 세션 검증
- **관리자 권한**: `AdminCheckInterceptor`로 `/api/v1/admin/**` 접근 시 ADMIN 역할 검증
- **비밀번호**: Salt 기반 해싱 저장
- **XSS 방지**: 사용자 입력값 HTML 이스케이프 처리
- **이미지 검증**: Magic byte 기반 파일 타입 검증 (최대 15MB)

### CORS 허용 출처
- `http://localhost:5500`
- `http://127.0.0.1:5500`
- `http://localhost:63343`
- `http://127.0.0.1:63343`

---

## 데이터베이스 스키마

| 테이블 | 설명 |
|--------|------|
| `members` | 회원 정보 (login_id, password, name, phone, role, status 등) |
| `salt` | 비밀번호 Salt |
| `rooms` | 객실 정보 (room_name, capacity, price, description, policy, picture, status) |
| `reservations` | 예약 정보 (guest_id, room_id, check_in, check_out, guest_count, total_price, status) |
| `log_auth` | 인증 로그 |

**예약 상태:** `PENDING` → `CONFIRMED` / `CANCELLED`
**객실 상태:** `ACTIVE` / `INACTIVE`

---

## 캐싱

- **ReservationCache**: 객실별 예약 날짜를 `ConcurrentHashMap`에 캐싱하여 빠른 가용성 조회
- **RoomCache**: 객실 데이터 캐싱으로 DB 조회 최소화
- 첫 조회 시 Lazy Loading, Thread-safe Double-checked Locking 적용
