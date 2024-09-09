# FirstComeFirstServed
선착순 구매 E-commerce 플랫폼

## 🖥️프로젝트 소개
- 상품에 대한 선착순 구매 기능을 지원합니다.
- 재고 관리에 분산락을 적용해 데이터의 무결성을 보장했습니다.
- MSA 기반으로 각 서비스를 분리해 유연성과 확장성을 높였습니다.
  
### 🕰️개발 기간 및 인원
- 기간 : 24.08.07 ~ 24.09.03(1개월)
- 인원 : 1인(BE)
### ⚙️개발 환경
- Java 21
- Spring Boot 3.3.2
- Spring Security
- JWT
- Spring Cloud Netflix Eureka Server/Client
- Spring Cloud Gateway
- Spring Cloud OpeenFeign
- MySQL 8.0
- Redis
- zookeeper 3.9.2
- kafka 3.7.0
- docker
### 🔧아키텍쳐
![프로젝트 구성](https://github.com/user-attachments/assets/4a2119e9-a5ce-442f-b811-3b462aa38fb2)
### 🗃️ERD
![항해99프로젝트](https://github.com/user-attachments/assets/4f50654d-a5f2-43d1-aebb-9c52e02f7c90)
### 📜API 문서
- Postman
<br>

## 주요 기능
### 회원가입
- 이메일 인증(Gmail SMTP)
- 개인정보 암호화(AES256, BCryptPasswordEncoder)

### 로그인
- Spring Security
- JWT를 통한 인증 + 사용자의 고유번호를 풀어 header에 추가
- header에 추가된 고유번호를 사용하여 이후 요청에 최대한 유저 서비스에 접근하지 않도록 설계

### 상품
- 상품의 전체 리스트 제공
- 상품의 상세 페이지 제공
- 상품의 재고 캐싱(Redis)

### 주문
- 주문 시 재고 확인
- 주문 생성 시 결제 서버에 결제 요청
- 결제 성공, 실패에 따른 주문 처리
#### 흐름도
![주문 흐름도](https://github.com/user-attachments/assets/bdb5804b-9b5b-4ad0-99eb-86da308c580a)

### 그 외
- 마이페이지
- 장바구니
<br>

## 🔍트러블슈팅
### 재고 동시성 문제 해결
#### 상황
동시에 주문 및 결제 시 재고의 정합성이 보장되지 않는 상황이 발생했습니다.([상세보기](https://www.notion.so/1-114e1a8b341b4a498e0a8c57887d2d13))
#### 해결 방안
1. 분산락 적용([상세보기](https://www.notion.so/2-Redis-01677c4b466044138e20ea07bdf660f6))
- 한정된 재고(공유 자원)에 사용자들이 동시에 접근 시 재고의 정합성이 보장되지 않습니다.
- 주문 시 재고 확인이나 결제 성공 후 재고 감소하는 로직에 분산락을 적용하여 재고의 정합성을 보장하였습니다.

2. 예약 재고 추가([상세보기](https://www.notion.so/3-9763cb91d21b4453a12a6f7ead28b803))
- 재고 감소가 주문 시 진행되는 것이 아니라 결제 성공 시 진행되기 때문에 결제가 진행 중인 건에 대해 재고가 정확하게 반영되지 않습니다.
- 주문 시 확인하는 재고에 결제 중인 재고를 포함하기 위해서 예약 재고를 추가했습니다.
<br>

## 📈성능 개선
- JMeter를 통해 부하테스트 진행했습니다.
- JMeter, IntelliJ Profiler를 사용하여 개선이 필요한 부분과 수치를 확인했습니다.
- 동일 환경에서 진행했더라도 결과에 편차가 있었기 때문에 평균에 해당하는 케이스를 사용했습니다.
- 사용자가 로그인 -> 장바구니 담기 -> 주문 요청을 순차적으로 진행하는 상황을 가정하고 진행했습니다.
### 1차 부하테스트([상세보기](https://www.notion.so/1-2bc289254d3342eaa3859d9c1a945bde))
- Thread : 10000 / Ramp up : 100 으로 진행했습니다.
- DB에 인덱스를 추가하여 응답 시간을 개선했습니다.
- 로컬 환경에 비해 높은 부하로 인해 오류율과 응답 시간이 너무 높게 나타났습니다.
- <b>P50, P90, P95, P99 모든 구간에서 응답 시간이 대략적으로 13% 개선</b>

### 2차 부하테스트([상세보기](https://www.notion.so/2-492bb91a9cdd4616a821b6b083029b88))
- Thread : 10000 / Ramp up : 200 으로 진행했습니다.
- DB Connection Pool 설정 / DB 인덱스 추가 / 소스 코드 수정을 통해 성능 개선을 진행했습니다.

<br>

## 📂파일 구조
