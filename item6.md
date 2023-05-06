# \[item6] 불필요한 객체 생성을 피하라

## 들어가기

#### 주제 : 어떤 객체는 매우 비효율적으로 생성되고 사라진다. 객체를 효율적으로 생성하고 사용하자.

불필요한 혹은 비효율적인 객체 생성이란 무엇일까. 문자열에 관한 예시를 보자.

```java
String nickname1 = new String("후추"); //(A)

String nickname2 = "후추"; //(B)
```
(A) 코드는 실행될 때마다 String 인스턴스를 새로 만든다.

만약 (A) 코드가 반복문이나 빈번히 호출되는 메서드에 있다면 쓸데없는 String 인스턴스가 수없이 만들어질 것이다.

반면, (B) 코드는 반복 호출되더라도 새로운 인스턴스를 만들지 않는다. "후추"라는 String 값이 `String Constant Pool`에 저장되어 재사용되기 때문이다.

이처럼 같은 동작을 수행하더라도 객체가 불필요하게 생성되는 코드와 그렇지 않은 코드가 나뉜다.

`불필요한 객체를 생성하지 않는 방향`으로 코드를 작성하는 것이 바람직하다.

그렇다면 객체를 어떻게 효율적으로 사용할 수 있을까? 그것은 객체를 **재사용**하는 것에 있다.


## 🧂 객체를 재사용하자!

### 불변 클래스와 정적 팩터리 메서드

정적 팩터리 메서드는 객체를 생성하는 역할을 하는 static 메서드이다.

생성자는 호출될 때마다 새로운 객체를 만들지만,

정적 팩터리 메서드는 새로운 객체를 만들지 않을 수 있다.

java.lang의 Boolean 클래스를 보자.

Boolean 클래스는 Boolean(String) 생성자와 함께 정적 팩터리 메서드 Boolean.valueOf(String)를 제공한다.


```java
public class Boolean {
    
  //...
    
  public static final Boolean TRUE = new Boolean(true);
  public static final Boolean FALSE = new Boolean(false);
  
  @Deprecated(since="9")
  public Boolean(boolean value) {
      this.value = value;
  }
  
  @Deprecated(since="9")
  public Boolean(String s) {
      this(parseBoolean(s));
  }

  public static Boolean valueOf(String s) {
        return parseBoolean(s) ? TRUE : FALSE;
  }
    
  //...
}
```

Boolean 인스턴스를 얻고자 한다면, Boolean.valueOf(String) 팩터리 메서드를 사용하는 것이 좋다.

이 메서드는 미리 만들어둔 TRUE, FALSE 를 재사용하기 때문이다.

Boolean의 생성자와 정적 팩터리 메서드는 겉으로 보기에 동일한 기능을 수행한다.

하지만 내부를 살펴보면 정적 팩터리 메서드에서는 객체를 재사용함으로써 불필요한 객체 생성을 막는다.

프로그램을 개발할 때도 가능하다면 정적 팩터리 메서드를 통해 불필요한 객체 생성을 막자.


### 캐싱

캐싱이란 **캐시**라는 작업을 하는 행위를 의미한다.

> **캐시(cache)**는 컴퓨터 과학에서 **데이터나 값을 미리 복사해 놓는 임시 장소**를 가리킨다. 캐시는 캐시의 접근 시간에 비해 원래 데이터를 접근하는 시간이 오래 걸리는 경우나 값을 다시 계산하는 시간을 절약하고 싶은 경우에 사용한다. 캐시에 데이터를 미리 복사해 놓으면 계산이나 접근 시간없이 **더 빠른 속도로 데이터에 접근**할 수 있다. [wiki](https://ko.wikipedia.org/wiki/%EC%BA%90%EC%8B%9C)

생성 비용이 비싼 객체는 캐싱하여 재사용하는 것이 효율적이다.

다음 전화번호 객체의 생성과 검증에 대한 예시를 보자.

```java
public class PhoneNumber {
    private final String number;

    public PhoneNumber(final String number) {
        validateFormat(number);
        this.number = number;
    }

    private void validateFormat(final String number) {
        if (!number.matches("\\d{3}-\\d{4}-\\d{4}")) { //000-0000-0000 형식인지 확인
            throw new IllegalArgumentException("잘못된 형식입니다.");
        }
    }
}
```

PhoneNumber 클래스는 생성 시에 validateFormat() 메서드로 입력값을 검증한다.

이때 String.matches 로 문자열 형태가 정규표현식에 맞는지 확인한다.


>**String.matches()의 내부**
![](https://velog.velcdn.com/images/153plane/post/ed2c4654-fd91-4a16-a4e8-6a654470490a/image.png)


>**Pattern.matches()의 내부**
![](https://velog.velcdn.com/images/153plane/post/20848005-6de8-4074-a08e-617bf7d8587c/image.png)


String.matches() 메서드의 구현을 살펴보면 Pattern 인스턴스를 생성하는 것을 볼 수 있다.

즉, String.matches() 메서드를 호출할 때마다 Pattern 인스턴스가 만들어지는데, 이 Pattern 인스턴스는 한 번 쓰고 버려져서 GC의 대상이 된다.

Pattern 는 정규표현식에 해당하는 [유한 상태 머신(finite state machine)](https://ko.wikipedia.org/wiki/유한_상태_기계)을 만들기 때문에 인스턴스 생성 비용이 높다.

따라서 Pattern 객체를 캐싱해서 필요할 때마다 재사용한다면 성능을 개선할 수 있다.

```java
public class PhoneNumber {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");

    private final String number;

    public PhoneNumber(final String number) {
        validateFormat(number);
        this.number = number;
    }

    private void validateFormat(final String number) {
        if (!PHONE_NUMBER_PATTERN.matcher(number).matches()) {
            throw new IllegalArgumentException("잘못된 형식입니다.");
        }
    }
}
```

#### 테스트 해보기

실제로 성능이 개선되는지 확인해볼 필요가 있다. 간단한 테스트 코드를 작성해 확인해본 결과 캐싱 후 테스트를 더 빠르게 통과했다.

```java
public class PhoneNumberTest {

    @Test
    void 전화번호_테스트() {
        //test1
        final String number = "010-1234-5678";

        assertThatNoException().isThrownBy(() -> new PhoneNumber(number));

        //test2
        final String wrongNumber1 = "010-123-45678";

        assertThatThrownBy(() -> new PhoneNumber(wrongNumber1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 형식입니다.");

        //test3
        final String wrongNumber2 = "010-12345-678";

        assertThatThrownBy(() -> new PhoneNumber(wrongNumber2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 형식입니다.");
    }
}
```
>**캐싱 전**
![](https://velog.velcdn.com/images/153plane/post/8f337819-cebb-4542-9b1b-f0087d8f5834/image.png)


>**캐싱 후**
![](https://velog.velcdn.com/images/153plane/post/2ac86346-1c9b-40c7-bb1c-01e7a91785c3/image.png)

만일 validateFormat() 메서드가 보다 빈번히 호출된다면 성능 차이는 더 클 것으로 예상된다.

### Collection view (어댑터)

![](https://velog.velcdn.com/images/153plane/post/f2e9956f-5518-4d4e-b15e-662ba8945b33/image.png)

일상에서 어댑터는 다음과 같이 **서로 다른 기계 장치를 연결**해서 작동할 수 있도록 만들어 주는 도구다.

디자인 패턴 중 어댑터(view라고도 한다) 패턴은 이와 비슷한 특징을 갖고 있는데, **실제 작업은 뒷단 객체에게 위임하고, 자신은 제2의 인터페이스 역할**을 해주는 객체이다.

예를 들어 Map 인터페이스의 keySet() 메서드는 Map 객체 안의 키를 담은 Set 뷰를 반환한다.

>HashMap 의 keySet 메서드
![](https://velog.velcdn.com/images/153plane/post/5320cd8e-2117-42b5-a24c-0a2babee6448/image.png)

>HashMap 의 KetSet 클래스
![](https://velog.velcdn.com/images/153plane/post/b22ea327-d0ff-47d4-ac9a-ed9191ab74f9/image.png)


keySet() 메서드는 호출할 때마다 새로운 Set 인스턴스를 반환하지 않는다.

keySet() 메서드가 반환하는 Set이 모두 같은 Map 인스턴스를 대변하기 때문이다.

즉, Map 인스턴스와 KeySet 이 연결되어 있다.

Map에서 어떤 key를 지운다면, keySet의 Set 역시 해당 key를 포함하지 않게 된다. 반대의 경우도 같다.

정리하자면 가변 객체를 반환하는 상황이더라도 **정확한 의도**를 갖는다면, 반드시 새로운 객체를 생성(방어적 복사)해서 반환할 필요가 없다. 객체를 재사용하자.

### 오토박싱을 주의하자

오토박싱은 기본 타입(primitive type)과 박싱된 기본 타입(wrapper class)을 섞어 쓸 때 자동으로 상호 변환해주는 기술이다.

```java
// boxing
int num = 10;
Integer wrappedNum = new Integer(num);

// unboxing
Integer wrappedNum = new Integer(10);
int num = wrappedNum.intValue();

// auto boxing
int num = 10;
Integer wrappedNum = num;

// auto unboxing
Integer wrappedNum = new Integer(10);
int num = wrappedNum;
```

불필요하게 오토박싱이 일어나는 상황을 보자.

```java
private static long sum() {
	Long sum = 0L;
    for (long i = 0; i <= Integer.MAX_VALUE; i++) {
    	sum += i;
    }
    return sum;   
}
```
long 타입인 i가 Long 타입인 sum에 더해질 때마다 오토박싱이 일어난다.

즉, 불필요한 Long 인스턴스가 지속적으로 생성되고 사라진다.

이는 성능면에서 매우 비효율적인 방식이다.

불필요한 객체 생성을 하지 않아야 한다.

값을 연산할 때는 가급적 기본 타입을 사용하자.

또한 wrapper 클래스를 사용할 경우 의도하지 않은 오토박싱이 숨어들지 않도록 주의하자.

## 오해 정정
### 객체 생성을 피하라는 것이 아니다.
프로그램의 명확성, 간결성, 기능을 위한 객체 생성은 필요하고 좋은 일이다.

### 단순히 객체 생성을 피하고자 객체 풀(pool)을 만들지 말자.
일반적으로 객체 풀은 읽기 어려운 코드를 만들고 메모리도 효율적으로 사용하지 못하게 만들 수 있다.

### 방어적 복사를 하지 말라는 것이 아니다.

아이템 50 방어적 복사에 해당하는 내용은 새로운 객체를 만들어야 하는 상황에서 기존 객체를 재사용하지 말라고 전달한다.

방어적 복사가 필요한 상황에서 객체를 생성하는 것과 객체를 재사용하는 것의 비용을 예상해보자.

객체를 생성하는 것은 단순히 코드 형태와 성능에만 영향을 주지만, 방어적 복사에 실패하면 버그와 보안 문제를 야기할 수 있다.

## 참고
https://stackoverflow.com/questions/18902484/what-is-a-view-of-a-collection
https://gyoogle.dev/blog/computer-language/Java/Auto%20Boxing%20&%20Unboxing.html