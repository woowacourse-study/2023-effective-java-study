#\[item1] 생성자 대신 정적 팩터리 메서드를 고려하라
## 들어가기

생성자는 객체를 생성하기 위해 호출해야 하는 메서드를 의미한다.

생성자 대신 정적 팩터리 메서드를 활용할 수 있다.

그렇다면 정적 팩터리 메서드란 무엇인가?

java.lang 패키지에 있느 Boolean 클래스의 정적 메서드 valueOf() 살펴보자.

```java
public class Boolean {
    
    // ...
    
  public static final Boolean TRUE = new Boolean(true);
  public static final Boolean FALSE = new Boolean(false);

  public static Boolean valueOf(boolean b) {
        return (b ? TRUE : FALSE);
    }
    
    //...
}
```
이 메서드는 boolean 값을 인자로 받아 Boolean 객체를 반환한다. 이것이 정적 팩터리 메서드의 예시이다.

정적 팩터리 메서드는 `객체를 생성하는 역할을 하는 static 메서드`라고 이해할 수 있다.

정적 팩터리 메서드의 장점을 알아보자.
## 장점

### 1. 이름을 가질 수 있다.

일반적으로 생성자를 사용한다면 하나의 시그니처로는 하나의 생성자만 만들 수 있다.

Order 클래스가 있다고 가정하자. Order 클래스는 배달과 포장 주문을 구별한다.

```java
public class Order {

  private boolean takeout;
  private boolean delivery;
  private Product product;

}
```
만약 포장 주문에 대해서는 takeout이 true 값을 갖고, 배달 주문은 delivery가 true 값을 갖도록 하고 싶다면 어떻게 생성자를 만들어야 할까?

생성자를 만들어보자.

```java
public class Order {

  private boolean takeout;
  private boolean delivery;
  private Product product;

  public Order(final boolean takeout, final Product product) {
    this.takeout = takeout;
    this.product = product;
  }
  
  //오류가 난다.
  public Order(final boolean delivery, final Product product) { 
    this.delivery = delivery;
    this.product = product;
  }
}
```
위와 같이 생성자를 만든다면 컴파일이 되지 않는다. 한 클래스에 동일한 시그니처를 가진 생성자는 두 개 이상 존재할 수 없기 때문이다.

오류를 피하기 위해 입력 매개변수의 순서를 다르게 할 수 있을 것이다. 그러나 이는 좋은 방법이 아닌데, 이 생성자를 호출하는 입장에서 파라미터의 순서에 따라 어떤 객체가 생성되는지 알기 어렵기 때문이다.

이런 경우 정적 팩터리 메서드를 활용할 수 있다.

정적 팩터리 메서드는 이름을 가질 수 있으므로, 반환될 객체의 특성을 쉽게 묘사할 수 있다.

또한 서로 다른 이름을 짓는다면, 시그니처가 같은 여러 개의 생성자가 있는 효과를 누릴 수 있다.

```java
public class Order {

  private boolean delivery;
  private boolean takeout;
  private Product product;

  public static Order takeoutOrder(Product product) {
    Order order = new Order();
    order.takeout = true;
    order.product = product;
    return order;
  }

  public static Order deliveryOrder(Product product) {
    Order order = new Order();
    order.delivery = true;
    order.product = product;
    return order;
  }
}
```
takeoutOrder() 메서드를 호출하면 포장 Order 객체를 생성할 수 있고, deliveryOrder() 메서드를 호출하면 배달 Order 객체를 생성할 수 있게 된다.

### 2. 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.

```java
public class Boolean {

  // ...

  public static final Boolean TRUE = new Boolean(true);
  public static final Boolean FALSE = new Boolean(false);

  public static Boolean valueOf(boolean b) {
    return (b ? TRUE : FALSE);
  }

  //...
}
```
Boolean 클래스의 정적 팩터리 메서드인 valueOf 메서드는 객체를 직접 생성하지 않고 미리 생성해둔 객체를 반환한다.

이러한 방법으로 Boolean 객체를 TRUE, FALSE 두 개만을 인스턴스로 갖는 불변 객체로 사용할 수 있다.

`불변 객체`의 장점을 간단히 서술하자면 다음과 같다.
- 안정적인 객체로 신뢰도가 높다.
- 생성자, 접근 메서드에 대해 방어적 복사가 불필요하다.
- 멀티스레드 환경에서 동기화 처리없이 사용할 수 있다.

꼭 불변이 아니더라도, 자주 사용하면서 변하지 않을 객체를 미리 생성해두고 반환하는 방법을 사용할 수도 있다.

아래는 우아한테크코스 사다리 미션에서 사용된 코드 중 일부이다.

정적 팩터리 메서드 from은 사용자 입력을 받아 UserRequestedParticipants 객체를 반환한다.

```java
public class UserRequestedParticipants {

    public static final String ALL_PARTICIPANTS_COMMAND = "all";
    private static final UserRequestedParticipants ALL_PARTICIPANTS = new UserRequestedParticipants(ALL_PARTICIPANTS_COMMAND);

    private final String requestContent;

    private UserRequestedParticipants(final String requestContent) {
        this.requestContent = requestContent;
    }

    public static UserRequestedParticipants from(final String requestContent) { //정적 팩터리 메서드
        if (requestContent.equals(ALL_PARTICIPANTS_COMMAND)) {
            return ALL_PARTICIPANTS;
        }
        return new UserRequestedParticipants(requestContent);
    }

    public boolean isAllParticipants() {
        return this.equals(ALL_PARTICIPANTS);
    }

    public String getRequestContent() {
        return requestContent;
    }
}
```


이때 사용자의 입력이 "all"과 같다면 미리 생성해둔 `ALL_PARTICIPANTS`를 반환한다.

이러한 방법으로 인해 all을 상태로 갖는 모든 UserRequestedParticipants 객체는 오직 하나 뿐임이 보장되며 매번 새로운 객체를 생성할 필요가 없어진다.

위와 같이 정적 팩터리 메서드를 통해 인스턴스를 통제할 수 있다.

인스턴스 통제와 관련한 키워드와 자세한 내용은 다른 아이템에 자세히 나와있으니 더 궁금한 내용이 있다면 참고하자.
- 클래스를 싱글턴으로 만들 수 있다. -> 아이템 3
- 인스턴스화 불가로 만들 수 있다. -> 아이템 4
- 불변 값 클래스에서 동치인 인스턴스가 하나 뿐임을 보장할 수 있다. -> 아이템 17

### 3. 반환 타입의 하위 타입 객체를 반환할 수 있다. 또한 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.

```java
public interface HelloService {
    void hello();
    static HelloService of(String language) {
        if (language.equals("ko")) {
            return new KoreanHelloService();
        }
        return new EnglishHelloService();
    }
}

public class KoreanHelloService implements HelloService{

  @Override
  public void hello() {
    System.out.println("안녕");
  }
}

public class EnglishHelloService implements HelloService{
  @Override
  public void hello() {
    System.out.println("hello");
  }
}
```
HelloService 인터페이스 안에 정적 팩터리 메서드가 존재한다. 이 메서드는 매개변수에 따라 서로 다른 하위 구현체를 반환한다.

```java
public class Main {
    public static void main(String[] args) {
        HelloService helloServiceFromInterface = HelloService.of("ko");
        helloServiceFromInterface.hello(); //안녕하세요
    }
}
```

HelloService 를 사용하는 클라이언트 측에서는 입력하는 매개변수 값에 따라 원하는 하위 타입 객체를 생성 받을 수 있다.

실제로 클라이언트는 어떤 구현체를 사용하는지 알지 못하게 되고 알 필요도 없게 된다. 오직 전달할 매개변수만 신경쓰면 된다.

이는 코드에 굉장한 유연함을 가져다줄 수 있다.

하지만 정적 팩터리 메서드는 다음과 같은 단점을 갖는다.
## 단점

### 1. 상속을 하려면 public, protected 생성자가 필요하니, 정적 팩터리 메서드만 제공하면 하위클래스를 만들 수 없다.
정적 팩터리 메서드를 사용할 때, 클라이언트가 코드 작성자의 의도대로 객체를 생성하도록 하기 위해 생성자를 private 등으로 막아두는 것이 보통이다.

다만, 이러한 방법은 상속을 불가하게 만든다. 상속을 염두에 두고 있다면 정적 팩터리 메서드를 주의해서 사용해야 한다.

### 2. 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.
클라이언트가 객체를 생성할 때, new 키워드로 생성자를 호출하는 것은 지극히 자연스럽다.

그러나 생성자 없이, 정적 팩터리 메서드만 존재한다면 클라이언트가 이 메서드를 직접 찾아내야만 객체를 생성할 수 있다.

따라서 클라이언트가 정적 팩터리 메서드를 쉽게 찾을 수 있도록 안내할 방법을 마련해야 한다.

## 정적 팩터리 메서드 명명 방식
정적 팩터리 메서드는 다음과 같은 네이밍 컨벤션을 갖는다.
- from : 하나의 매개 변수를 받아서 객체를 생성하는 메서드
- of : 여러개의 매개 변수를 받아서 객체를 생성하는 메서드
- valueOf : from과 of의 자세한 버전
- getInstance | instance : 인스턴스를 생성하는 메서드. 같은 인스턴스임을 보장하지 않음.
- newInstance | create : 인스턴스를 생성하는 메서드. 매번 새로운 인스턴스를 생성해 반환.
- get[OtherType] : 다른 타입의 인스턴스를 생성. 같은 인스턴스임을 보장하지 않음.
- new[OtherType] : 다른 타입의 새로운 인스턴스를 생성. 매번 새로운 인스턴스를 생성해 반환.
