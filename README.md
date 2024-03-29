# 이펙티브 자바 스터디

### (LEVEL 1) - 23.02.20 ~ 23.04.14

<br>
<br>
<br>

🟦 발표 자료 경로

- 예시) 챕터 1 / 아이템 1 / 생성자 대신 정적 팩터리 메서드를 고려하라.
- `chap01/item01/생성자_대신_정적_팩터리_메서드를_고려하라.md`


🟦 발표 자료
- Markdown 파일


🟦 발표 자료 올리기
- https://github.com/woowacourse-study/2023-effective-java-study
- 발표 이전, fork -> PR (main <- 깃허브아이디)
- 발표 이후, main 브랜치에 Rebase Merging 한다.


🟦 발표 시간
- 10 - 20분


🟦 발표
- 주 2회
- 월요일 18시 2명
- 금요일 17시 3명


🟦 추가 규칙
- 새로운 예제를 작성한다.
- 상대방 PR에 리뷰를 진행한다.
- 월요일 발표자, 발표 이후 리뷰를 받아 금요일 발표 전까지 merge한다.
- 금요일 발표자, 발표 이후 리뷰를 받아 월요일 발표 전까지 merge한다.

<br>
<br>
<br>

### 🚀 스터디 크루원

| <img src="https://avatars.githubusercontent.com/u/52229930?v=4" alt="" width=200> | <img src="https://avatars.githubusercontent.com/u/79090478?v=4" alt="" width=200> | <img src="https://avatars.githubusercontent.com/u/116645747?v=4" alt="" width=200> | <img src="https://avatars.githubusercontent.com/u/106813090?v=4" alt="" width=200> | <img src="https://avatars.githubusercontent.com/u/82203978?v=4" alt="" width=200> |
|:---------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------:|
|                       [말랑](https://github.com/shin-mallang)                       |                       [루카](https://github.com/dooboocookie)                       |                         [썬샷](https://github.com/Ohjintaek)                         |                         [후추](https://github.com/Combi153)                          |                        [헤나](https://github.com/hyena0608)                         |

<br>
<br>
<br>

# 아이템

- [x]  [후추] item 1. 생성자 대신 정적 팩터리 메서드(static factory method)를 고려하라
- [x]  [후추] item 2. 생성자에 매개변수가 많다면 빌더를 고려하라
- [x]  [후추] item 3. private 생성자나 열거 타입으로 싱글턴임을 보증하라
- [x]  [루카] item 5. 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라
- [x]  [후추] item 6. 불필요한 객체 생성을 피하라


- [x]  [썬샷] item 10. equals는 일반 규약을 지켜 재정의하라
- [x]  [썬샷] item 11. equals를 재정의하려거든 hashCode도 재정의하라
- [x]  [루카] item 12. toString을 항상 재정의하라
- [x]  [말랑] item 14. Comparable을 구현할지 고려하라


- [x]  [말랑] item 17. 변경 가능성을 최소화하라
- [x]  [썬샷] item 18. 상속보다는 컴포지션을 사용하라
- [x]  [헤나] item 20. 추상 클래스보다는 인터페이스를 우선하라
- [x]  [말랑] item 21. 인터페이스는 구현하는 쪽을 생각해 설계하라
- [x]  [헤나] item 22. 인터페이스는 타입을 정의하는 용도로만 사용하라
- [x]  [헤나] item 23. 태그 달린 클래스보다는 클래스 계층구조를 활용하라
- [x]  [루카] item 24. 멤버 클래스는 되도록 static으로 만들라


- [x]  [헤나] item 26. Raw Type은 사용하지 말라
- [x]  [말랑] item 31. 한정적 와일드카드를 사용해 API 유연성을 높이라
- [x]  [헤나] item 32. 제네릭과 가변인수를 함께 쓸 때는 신중하라
- [x]  [말랑] item 33. 타입 안전 이종 컨테이너를 고려하라


- [x]  [후추] item 35. ordinal 메서드 대신 인스턴스 필드를 사용하라
- [x]  [썬샷] item 38. 확장할 수 있는 열거 타입이 필요하면 인터페이스를 사용하라
- [x]  [말랑] item 41. 정의하려는 것이 타입이라면 마커 인터페이스를 사용하라


- [x]  [루카] item 42. 익명 클래스보다는 람다를 사용하라
- [x]  [썬샷] item 44. 표준 함수형 인터페이스를 사용하라


- [x]  [헤나] item 49. 매개변수가 유효한지 검사하라
- [x]  [썬샷] item 54. null이 아닌, 빈 컬렉션이나 배열을 반환하라
- [x]  [후추] item 55. 옵셔널 반환은 신중이 하라


- [x]  [루카] item 63. 문자열 연결은 느리니 주의하라
- [x]  [말랑] item 64. 객체는 인터페이스를 사용해 참조하라
