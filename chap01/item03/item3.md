## 들어가기

싱글턴(singleton)은 **오직 하나의 인스턴스만 생성할 수 있는 클래스**를 의미한다.

싱글턴 패턴은 소프트웨어 디자인 패턴 중 하나이다.

왜 이러한 패턴이 필요한지, 어떻게 구현할 수 있는지 알아보자.

## 실생활 예시

백화점 푸드코트는 보통 여러 음식점이 함께 있다.

![](https://velog.velcdn.com/images/153plane/post/8053e50b-978b-4a5b-a840-a3c4838c7adb/image.png)


손님은 음식을 주문하고 자리에 앉아 기다린다.

음식이 완성되면 커다란 모니터에 음식의 주문번호가 뜬다.

<img src="https://velog.velcdn.com/images/153plane/post/a7d23afd-2e32-463f-94e7-bef96b8d6f06/image.png" width="80%" height="80%" alt="">

그러면 손님은 음식을 가져온다.

음식이 완료되었다는 요청은 여러 식당에서 한다. 단, 모니터는 한 대만 존재한다.

식당 주인들이 각자 모니터를 생성해서 사용하는 일은 없다.

음식 주문에 대한 내용은 오직 한 모니터에서 나와야 한다.

![](https://velog.velcdn.com/images/153plane/post/653bca30-c6b1-49b9-9873-144c729d59de/image.png)


프로그램 안에서도 이러한 상황이 존재할 수 있다.

프로그램 내에서 어떤 객체가 단 한 개만 존재하고, 여러 객체들이 이 객체를 공유하며 사용하는 것이다.

이럴 때 싱글턴 패턴이 활용될 수 있다.

그렇다면 싱글턴 패턴은 어떻게 만들 수 있을까.

## private 생성자

### public static final 필드 방식

생성자를 private로 만들고, public static final 필드에서 이 생성자를 사용하는 방식이다.

```java
public class SingletonMonitor {
    public static final SingletonMonitor INSTANCE = new SingletonMonitor();

    private SingletonMonitor() {}

    public void showOrderNumber(final int orderNumber) {
        //... 주문번호 보여주기
    }
}
```

private 생성자는 public static final 필드인 `SingletonMonitor.INSTANCE` 를 초기화 할 때 딱 한번 호출된다.

다른 생성자가 없으므로 SingletonMonitor 객체가 다시 생성될 수 없고,

`SingletonMonitor.INSTANCE` 전체 시스템에서 딱 하나 존재하는 인스턴스임이 보장된다.

#### 특징

이러한 방식은 해당 클래스가 싱글턴임을 API에 명확하게 드러내는 것이 특징이다.

또한 가장 간결하게 싱글턴을 구현하는 방식이다.

### 정적 팩터리 메서드 방식
정적 팩터리 메서드 방식은 위와 마찬가지로 생성자를 private으로 만들지만,

인스턴스를 갖는 static final 필드를 private으로 두고 정적 팩터리 메서드에서 이를 반환한다.

```java
public class SingletonMonitor {
    private static final SingletonMonitor INSTANCE = new SingletonMonitor();

    private SingletonMonitor() {}

    public static SingletonMonitor getInstance() {
        return INSTANCE;
    }
    
    public void showOrderNumber(final int orderNumber) {
        //... 주문번호 보여주기
    }
}
```

`getInstance()`는 항상 같은 `SingletonMonitor.INSTANCE`를 반환하므로 새로운 인스턴스가 만들어지는 일이 없다.

#### 특징

이 방식은 첫번째 방식과 다르게 마음이 바뀐다면 API를 바꾸지 않고도 싱글턴이 아니게 만들 수 있다는 특징을 갖는다.

또한 원한다면 제네릭을 활용해 제네릭 싱글턴 팩터리로 만들 수 있다.(item30)([참고](https://jake-seo-dev.tistory.com/13))

한편 정적 팩터리 메서드를 함수형 인터페이스 중 하나인 `Supplier<T>`로 사용할 수도 있다.

## 싱글턴 깨뜨리기
`private 생성자`를 활용한 두 가지 방식은 아래와 같은 상황에서 새로운 인스턴스가 생성될 수 있다.

### reflection
```java
public class Restaurant {
    
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final SingletonMonitor singletonMonitor = SingletonMonitor.INSTANCE;

        final Constructor<SingletonMonitor> constructor = SingletonMonitor.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        final SingletonMonitor otherSingletonMonitor = constructor.newInstance();

        System.out.println(singletonMonitor ==  otherSingletonMonitor); //false
    }
}
```
Reflection API 는 클래스의 정보에 접근할 수 있게 해주는 API이다. ([참고](https://tecoble.techcourse.co.kr/post/2020-07-16-reflection-api/)) Reflection 을 사용하면 private로 선언된 생성자에 접근하여 강제로 호출시킬 수 있다. 따라서 Reflection으로 인해 싱글턴이 깨질 수 있다.

### 직렬화 역직렬화
```java
public class Restaurant {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final SingletonMonitor singletonMonitor = SingletonMonitor.INSTANCE;
        final SingletonMonitor otherSingletonMonitor;

        try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("SingletonMonitor.obj"))) {
            out.writeObject(singletonMonitor);
        }

        try (ObjectInput in = new ObjectInputStream(new FileInputStream("SingletonMonitor.obj"))) {
            otherSingletonMonitor = (SingletonMonitor) in.readObject();
        }

        System.out.println(singletonMonitor ==  otherSingletonMonitor); //false
    }
}
```

> **직렬화(Serialization)**는 컴퓨터 과학의 데이터 스토리지 문맥에서 **데이터 구조나 오브젝트 상태**를 동일하거나 **다른 컴퓨터 환경에 저장**하고 나중에 재구성할 수 있는 포맷으로 **변환**하는 과정이다.
반대로, 일련의 바이트로부터 데이터 구조를 **추출**하는 일은 **역직렬화**라고 한다. ([wiki](https://ko.wikipedia.org/wiki/직렬화))

위와 같은 방식으로 만든 싱글턴 클래스를 직렬화 한다면 역직렬화 할 때 새로운 인스턴스가 만들어진다.

싱글턴 특성을 유지하기 위해서는 단순히 Serializable을 구현한다고 선언하는 것뿐 아니라,

모든 인스턴스 필드를 transient로 선언하고, readResolve 메서드를 제공해야 한다.([참고](https://madplay.github.io/post/what-is-readresolve-method-and-writereplace-method))

## 열거 타입 선언

싱글턴을 만드는 또 하나의 방법은 열거 타입에 원소를 하나만 두는 방식이다.

```java
public enum SingletonMonitor {
    INSTANCE;

    public void showOrderNumber(final int orderNumber) {
        //... 주문번호 보여주기
    }
}
```

이러한 방식은 public 필드 방식과 비슷하다.

그러나 직렬화 상황이나 리플렉션 공격에서 싱글턴이 깨지는 일이 없다.

>대부분 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다. - Effective Java

## 추가로 공부해볼 키워드
- 멀티 스레드 환경에서 싱글턴 패턴
- 싱글턴 패턴이 OCP(개방 폐쇄 원칙)를 깨뜨리는 이유