# FirstComeFirstServed
미리 상품을 담아놓고 원하는 시점에 구매할 수 있는 기능을 제공합니다.
한정되어 있는 재고에 여러 사용자가 몰려도 재고에 문제가 생기지 않도록 했습니다.
MSA를 통해 유연하고 확장 가능한 서비스 운영이 가능합니다.
<br>

## 프로젝트 구성
![프로젝트 구성](https://github.com/user-attachments/assets/4a2119e9-a5ce-442f-b811-3b462aa38fb2)
<br>

## 사용 기술
- Java 21
- Spring Boot 3.3.2
- MySQL 8.0
- Spring Cloud Netflix Eureka Server/Client
- Spring Cloud Gateway
- Spring Cloud OpeenFeign
- zookeeper 3.9.2
- kafka 3.7.0
- redis

## 구현 기능
- API Gateway를 통한 라우팅 및 인증 기능 구현<br>
  - 클라이언트의 요청을 알맞은 서비스에 전달하여 부하를 분산
  - gateway에서 인증을 통해 적절하지 않은 요청 차단
- Redis를 이용한 재고 관리
  - 동시성 처리를 위한 분산락 사용
  - 데이터베이스 부하 감소
  - 빠른 읽기 성능을 통한 재고 검증 시간 단축
- OpenFeign을 통한 각 서비스간 통신
  
