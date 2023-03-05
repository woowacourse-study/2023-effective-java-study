#\[item2] 생성자에 매개변수가 많다면 빌더를 고려하라
## 들어가기
주제 : 한 클래스에 변수가 많다면 생성자를 어떻게 구현해야 하는지 생각한다.

우아한테크코스의 크루 정보를 담는 "크루" 클래스를 생각하자.

크루 클래스는 다음과 같은 필드를 갖는다.
- Nickname(닉네임) : 필수
- name(이름) : 필수
- address(주소) : 필수
- hobby(취미) : 선택
- projectName(프로젝트 이름) : 선택
- previousCompanyName(이전 직장) : 선택

이러한 필드 중 닉네임, 이름, 주소는 필수로 값을 가져야 하는 변수이다.

반면, 취미, 프로젝트 이름, 이전 직장은 빈 값을 가질 수 있다.

크루 클래스의 생성자를 세 가지 방법으로 만들 수 있다.
- 점층적 생성자 패턴
- 자바빈즈 패턴
- 빌더 패턴

## 점층적 생성자 패턴(telescoping constructor pattern)

필수 매개변수만 받는 생성자에 더하여 선택 매개변수를 갖는 생성자를 필요한 만큼 두는 방식이다.

```java
public class Crew {
    private final String nickName;      //닉네임   (필수)
    private final String name;          //이름     (필수)
    private final String address;       //주소     (필수)
    private final String hobby;         //취미
    private final String projectName;  //프로젝트명
    private final String previousCompanyName;  //이전 직장 이름

    public Crew(final String nickName, final String name, final String address) {
        this(nickName, name, address, "");
    }

    public Crew(final String nickName, final String name, final String address, final String hobby) {
        this(nickName, name, address, hobby, "");
    }

    public Crew(final String nickName, final String name, final String address, final String hobby, final String projectName) {
        this(nickName, name, address, hobby, projectName, "");
    }

    public Crew(final String nickName, final String name, final String address, final String hobby, final String projectName, final String previousCompanyName) {
        this.nickName = nickName;
        this.name = name;
        this.address = address;
        this.hobby = hobby;
        this.projectName = projectName;
        this.previousCompanyName = previousCompanyName;
    }
}
```

### 점층적 생성자 패턴을 피해야 하는 이유

- 매개변수 개수가 많아지면 코드를 작성하거나 읽기 어렵다.
- 찾기 어려운 버그로 이어질 수 있다.

크루 "후추"의 정보를 넣어보자.

```java
//정상 생성
Crew huchu1 = new Crew("후추", "찬민", "경기도 성남시", "누워있기", "", "키다리은행");
//잘못 생성
Crew huchu2 = new Crew("후추", "경기도 성남시", "", "찬민", "누워있기", "");
```
huchu1은 정상 생성된 객체이고, huchu2는 잘못 생성된 객체이다. huchu2는 각 상태에 대한 정보를 잘못 입력하였다.

하지만 타입이 같은 인자를 연달아 전달하는 나머지, 컴파일러는 이같은 실수를 알아채지 못한다.

결국 런타임에 엉뚱하게 동작할 것이다.

## 자바빈즈 패턴(JavaBeans pattern)
매개변수가 없는 생성자로 객체를 만들고, 세터(Setter) 메서드로 매개변수의 값을 설정하는 방식이다.


```java
public class Crew {
    private String nickName = "";
    private String name = "";
    private String address = "";
    private String hobby = "";
    private String projectName = "";
    private String previousCompanyName = "";

    public Crew() {}

    public void setNickName(final String nickName) {
        this.nickName = nickName;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setHobby(final String hobby) {
        this.hobby = hobby;
    }

    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    public void setPreviousCompanyName(final String previousCompanyName) {
        this.previousCompanyName = previousCompanyName;
    }
}
```

세터 메서드로 후추 크루를 생성해보자.

```java
Crew huchu = new Crew();
huchu.setNickName("후추");
huchu.setName("찬민");
huchu.setAddress("경기도 성남시");
huchu.setHobby("누워있기");
huchu.setPreviousCompanyName("키다리은행");
```


점층적 생성자 패턴과 달리, 읽기 쉽고 이해하기 쉽다.

또한 의도하는 인스턴스를 만들기 쉽다.

### 자바빈즈 패턴을 피해야 하는 이유
- 객체 하나를 만들기 위해 여러 메서드를 호출해야 하고, 객체가 완전히 생성되기 전까지 일관성(consistency)이 무너진 상태가 된다.
- 이러한 이유로 클래스를 불변으로 만들 수 없다.
- 스레드 안전성을 얻기 위해서는 추가 작업이 필요하다.

## 필더 패턴(Builder pattern)
필수 매개변수만으로 생성자를 호출해 빌더 객체를 얻고, 빌더 객체의 세터 메서드들로 선택 매개변수를 설정하고, build 메서드를 호출해 객체를 얻는 방법이다.

```java
public class Crew {
    private final String nickName;
    private final String name;
    private final String address;
    private final String hobby;
    private final String projectName;
    private final String previousCompanyName;

   public static class Builder {
       //필수 매개변수
       private final String nickName;
       private final String name;
       private final String address;
       // 선택 매개변수 - 기본값으로 초기화
       private String hobby = "";
       private String projectName = "";
       private String previousCompanyName = "";

       public Builder(final String nickName, final String name, final String address) {
           this.nickName = nickName;
           this.name = name;
           this.address = address;
       }

       public Builder hobby(String value) {
           hobby = value;
           return this;
       }

       public Builder projectName(String value) {
           projectName = value;
           return this;
       }

       public Builder previousCompanyName(String value) {
           previousCompanyName = value;
           return this;
       }

       public Crew build() {
           return new Crew(this);
       }
   }

    public Crew(Builder builder) {
       nickName = builder.nickName;
       name = builder.name;
       address = builder.address;
       hobby = builder.hobby;
       projectName = builder.projectName;
       previousCompanyName = builder.previousCompanyName;
    }
}
```

```java
Crew huchu = new Crew.Builder("후추", "찬민", "경기도 성남시")
                .hobby("누워있기")
                .previousCompanyName("키다리은행")
                .build();
```

### 빌더 패턴의 장점
- 코드를 읽고 이해하기 쉽다.
- 클라이언트가 헷갈리지 않고 코드를 작성할 수 있다.
- 클래스를 불변으로 만들 수 있다.

## 계층적 설계에서 빌더 패턴

상위, 하위 등 계층을 갖는 클래스마다 관련 빌더를 멤버로 정의하는 방법이다.

피자 클래스로 예시를 들어보자.

피자를 추상 클래스로 만들고, 추상 빌더를 갖게 한다. 피자의 구체 클래스는 구체 빌더를 갖게 하자.

```java
public class Pizza {
    public enum Topping {
        HAM, MUSHROOM, ONION, PEPPER, SAUSAGE
    }
    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract Pizza build();

        protected abstract T self();
    }

    public Pizza(final Builder<?> builder) {
        this.toppings = builder.toppings.clone();
    }
}
```

*재귀적 타입 한정 `<T extends Builder<T>>`
- 추상 메서드 self와 함께 활용된다.
- 하위 클래스에서 형변환하지 않고 메서드 연쇄를 지원하는 방법.

피자의 하위 클래스를 두 개 만들자.

하나는 일반적인 뉴욕 피자이고, 다른 하나는 칼초네 피자이다.

<table>
  <tr>
    <td><img alt="" src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c2/New_York_Pizza_Slices.png/300px-New_York_Pizza_Slices.png"/></td><td><img alt="" src="https://velog.velcdn.com/images/153plane/post/a05e95a9-0313-4a25-af18-df5bed7aa39c/image.png" /></td>
  <tr>
</table>

```java
public class NyPizza extends Pizza {
    public enum Size {
        SMALL, MEDIUM, LARGE
    }
    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(final Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public NyPizza(final Builder builder) {
        super(builder);
        this.size = builder.size;
    }
}
```

```java
public class Calzone extends Pizza {
    private final boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sauceInside = false;

        public Builder sauceInside() {
            sauceInside = true;
            return this;
        }

        @Override
        public Calzone build() {
            return new Calzone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public Calzone(Builder builder) {
        super(builder);
        this.sauceInside = builder.sauceInside;
    }
}
```

각 하위 클래스의 빌더는 build 메서드에서 구체 하위 클래스를 반환한다.

하위 클래스의 메서드가 상위 클래스의 메서드가 정의한 반환 타입이 아닌, 그 하위 타입을 반환하는 기능을 공변 반환 타이핑(covariant return typing)이라고 한다.

>[말랑](https://ttl-blog.tistory.com/1212)이의 블로그
![](https://velog.velcdn.com/images/153plane/post/da58a92b-e457-4deb-b716-707f6e58eff9/image.png)

이러한 기능으로 클라이언트가 형변환에 신경 쓰지 않고 빌더를 사용할 수 있다.

```java
NyPizza pizza = new NyPizza.Builder(SMALL).addTopping(SAUSAGE).addTopping(ONION).build();
Calzone calzone = new Calzone.Builder().addTopping(HAM).sauceInside().build();
```

추가적인 장점
- 가변인수 매개변수를 여러 개 사용할 수 있다.
- 혹은 메서드를 여러 번 호출하도록 하고, 각 호출 때 넘겨진 매개변수들을 하나의 필드로 모을 수 있다.


### 빌더 패턴의 단점
- 객체를 만들기 위해 빌더부터 만들어야 한다.
- 매개변수가 4개 이상은 되어야 값어치를 한다.

### 결론
생성자에 매개변수가 많다면 빌더 패턴을 선택하는 게 좋다!
매개변수 중 다수가 필수가 아니거나, 같은 타입이라면 빌더 패턴이 더욱 빛을 발한다.
빌더 패턴은 점층적 생성자보다 코드를 읽고 쓰기 간결하면서, 자바 빈즈보다 안전하다.
