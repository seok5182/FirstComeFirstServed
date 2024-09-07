# FirstComeFirstServed
선착순 구매 E-commerce 플랫폼

## 🖥️프로젝트 소개
- 사용자는 플랫폼을 통해 회원가입, 로그인, 로그아웃, 마이페이지 등의 기본적인 유저 관리 기능을 사용할 수 있습니다.
- 사용자는 회원가입 시 이메일 인증을 받아야 하며, 중복 이메일은 허용되지 않습니다.<br>
회원가입 시 이메일, 비밀번호, 이름, 주소는 필수 입력 요소이며, 개인정보는 모두 암호화하여 저장됩니다.
- 로그인 및 로그아웃은 JWT 토큰을 이용해 DB 부하를 최소화했습니다.
- 사용자는 등록되어 있는 상품들을 조회하고 각 상품의 상세 정보를 볼 수 있습니다.
- 사용자는 등록되어 있는 상품에 한하여 장바구니 담기를 진행할 수 있습니다.
- 마이페이지를 통해 장바구니에 담은 상품과 주문한 상품의 상태를 조회할 수 있습니다.
- 장바구니에서는 사용자가 담은 상품에 대한 정보를 보여줍니다.
- 사용자는 장바구니에 담긴 상품의 수량을 변경 / 삭제 / 주문을 진행할 수 있습니다.
- 주문 내역에서는 사용자가 주문한 상품에 대한 상태를 보여주고, 주문 취소 / 반품 기능을 제공합니다.
- 주문 후 일정 시간이 지남에 따라 주문 상품에 대한 상태를 Schedule을 통해 변경합니다.
- 동시에 진행되는 주문 상황에 대응하기 위해 재고에 대한 동시성 제어를 진행했습니다.
- 각 서비스(유저,상품,주문,결제)를 분리하여 유연하고 확장 가능한 서비스 운영이 가능합니다.
- API Gateway를 사용하여 사용자의 요청에 알맞은 서비스에 전달합니다.
  
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
- 재고 확인
- 재고가 충분하다면 주문 생성O(Redis 예약 재고 할당)<br>
재고가 충분하지 않다면 주문 생성X
- 주문이 생성되면 결제 진행(kafka)
- 결제 성공 시 재고 처리 및 주문 내역 업데이트(Redis, 상품 DB 재고 감소 / 결제 상태:성공 / 주문 상태:주문)(OpenFeign) <br>
결제 실패 시 주문 내역 상태 업데이트(Redis 예약 재고 해제 / 결제 상태:실패 / 주문 상태:취소)(kafka)
- 주문 취소, 반품 가능
- 스케줄러를 통한 주문 상태 주기적인 업데이트
- 재고 확인 및 재고 처리 단계에서는 동시성 제어를 위해 분산락 + 예약 재고 사용
  
### 그 외
- 마이페이지
- 장바구니
<br>

## 🔍트러블슈팅
- 재고 동시성 문제 해결
  - [재고 동시성 문제 발생](https://www.notion.so/1-114e1a8b341b4a498e0a8c57887d2d13)
  - [Redis 분산락 적용](https://www.notion.so/2-Redis-01677c4b466044138e20ea07bdf660f6)
  - [예약 재고 추가](https://www.notion.so/3-9763cb91d21b4453a12a6f7ead28b803)
<br>

## 📈성능 개선
- JMeter를 통해 부하테스트 진행
- JMeter, IntelliJ Profiler를 사용하여 개선이 필요한 부분 확인 및 수치 확인
- DB 조회 시 자주 사용하는 Column 인덱스에 추가
- DB Connection Pool 설정
- 소스 코드 수정
- [부하테스트1](https://www.notion.so/1-2bc289254d3342eaa3859d9c1a945bde)
- [부하테스트2](https://www.notion.so/2-492bb91a9cdd4616a821b6b083029b88)
<br>

## 📂파일 구조
