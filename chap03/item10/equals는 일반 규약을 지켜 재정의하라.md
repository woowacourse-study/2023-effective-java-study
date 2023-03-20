# equals는 일반 규약을 지켜 재정의하라

<br>

## equals를 재정의할 필요가 없는 경우
1. 각 인스턴스가 본질적으로 고유하다.
2. 인스턴스의 '논리적 동치성'을 검사할 일이 없다. 
3. 상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어맞는다.
4. 클래스가 private이거나 package-private이고 equals 메서드를 호출할 일이 없다.

<br>

## equals를 재정의해야 할 때는 언제일까?
- **객체 식별성**(두 객체가 물리적으로 같은가)을 확인하는 것이 아니라 **논리적 동치성**을 확인해야 하는데, equals가 논리적 동치성을 비교하도록 재정의되지 않았을 때
- 주로 값 클래스들이 이 경우에 해당한다. (ex. Integer, String)

<br>

## Equals 메서드를 재정의할 때의 규약
### 1. 반사성 (Reflexivity)
null이 아닌 모든 참조 값 x에 대해, `x.equals(x)는 true다`.
### 2. 대칭성 (Symmetry)
null이 아닌 모든 참조 값 x, y에 대해, `x.equals(y)가 true`면 `y.equals(x)도 true`다.
### 3. 추이성 (Transitivity)
null이 아닌 모든 참조 값 x, y, z에 대해, `x.equals(y)가 true`고 `y.equals(z)도 true`면 `x.equals(z)도 true`다.
### 4. 일관성(Consistency)
null이 아닌 모든 참조 값 x, y에 대해, `x.equals(y)`를 반복해서 호출하면 항상 `true`를 반환하거나 항상 `false`를 반환해야 한다.
### 5. null-아님
null이 아닌 모든 참조 값 x에 대해, `x.equals(null)은 false`다.

<br>

## 반사성 (Reflexivity)
- 객체는 자기 자신과 같아야 한다.
- 반사성이 충족된다면 `equals`로 비교하는 동치성뿐만 아니라 `==`으로 비교하는 동등성까지 만족시킬 수 있다.

<br>

## 대칭성 (Symmetry)
- 두 객체는 서로에 대한 동치 여부에 대해 똑같이 답해야 한다.
```java
public class Palindrome {
    private final String s;

    public Palindrome(final String s) {
        this.s = Objects.requireNonNull(validate(s));
    }

    private String validate(final String s) {
        for (int i = 0; i < s.length() / 2; i++) {
            if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {
                return null;
            }
        }
        return s;
    }

    // 대칭성 위배!!
    @Override
    public boolean equals(Object o) {
        if (o instanceof Palindrome) {
            return s.equalsIgnoreCase(
                    ((Palindrome) o).s);
        }
        if (o instanceof String) { // Palindrome 클래스 혼자서만 String과 동치성을 비교할 수 있다는 것을 알고 있다.
            return s.equalsIgnoreCase((String) o);
        }
        return false;
    }
}
```
```java
Palindrome cis = new Palindrome("abcdedcba");
String s = "abcdedcba";

System.out.println(cis.equals(s));  // true
System.out.println(s.equals(cis));  // false
```

- 이 문제를 해결하려면 Palindrome의 equals를 String과 연동하겠다는 꿈을 버려야 한다. (String을 바꿀 수 없기 때문에)
- 따라서 Palindrome의 `equals`는 다음과 같이 구현되어야 한다.
```java
@Override
public boolean equals(Object o) {
    return o instanceof Palindrome &&
        ((Palindrome) o).s.equalsIgnoreCase(s);
}
```

<br>

## 추이성 (Transitivity)
- 첫 번째 객체와 두 번째 객체가 같고, 두 번째 객체와 세 번째 객체가 같다면, 첫 번째 객체와 세 번째 객체도 같아야 한다는 뜻이다.
- 당연한 말인 것 같지만 프로그래밍에서는 충분히 추이성을 충족하지 못 하는 경우가 발생할 수 있다.
```java
public class Point {
    private final int x;
    private final int y;

    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) {
            return false;
        }
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
}
```
```java
public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(final int x, final int y, final Color color) {
        super(x, y);
        this.color = color;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint)) { // 비교하는 객체가 ColorPoint의 인스턴스가 아니라면 false를 반환한다. 대칭성 위배!!
            return false;
        }
        return super.equals(o) && ((ColorPoint) o).color == color;
    }
    
                                    ↓
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) { // o가 Point의 인스턴스가 아닐 때
            return false;
        }
        if (!(o instanceof ColorPoint)) { // o가 Point의 인스턴스지만 ColorPoint의 인스턴스는 아닐 때
            return o.equals(this);
        }
        return super.equals(o) && ((ColorPoint) o).color == color;
    }
}
```
- 위 코드의 `ColorPoint`에 존재하는 두 개의 `equals` 메서드 중 첫 번째는 대칭성을 위반한 경우이고, 두 번째는 대칭성은 만족하지만 추이성을 위반한 경우이다.
- 두 번째 `equals`를 사용해서 추이성에 대한 검증을 해 보면 아래와 같은 결과가 나타난다.
```java
ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
Point p2 = new Point(1, 2); 
ColorPoint p3 = new ColorPoint(1, 2, Color.GREEN);
 
System.out.println(p1.equals(p2)); // true. p1과 p2는 서로 같은 위치를 가지므로 같다.
System.out.println(p2.equals(p3)); // true. p2와 p3는 서로 같은 위치를 가지므로 같다.
System.out.println(p1.equals(p3)); // false. p2와 p3는 서로 다른 색깔을 가지므로 다르다.
```

- 사실 이 현상은 모든 객체 지향 언어의 동치관계에서 나타나는 근본적인 문제이다.
- **구체 클래스를 확장해 새로운 값을 추가하면서 equals 규약을 만족시킬 방법은 존재하지 않는다.**

<br>

### 구체 클래스를 확장한 경우의 equals 재정의
#### 1. `equlas`안의 `instanceof`검사를 `getClass`검사로 바꾸면 같은 클래스끼리만 비교할 수 있지 않을까?
```java
@Override public boolean equals(Object o) {
    if (o == null || o.getClass != getClass()) {
        return false;
    }
    Point p = (Point) o;
    return p.x == x && p.y == y;
}
```
- 위의 코드는 같은 `Point` 클래스 객체와 비교할 때만 true를 반환하기 때문에 올바른 것처럼 보이지만 실제로 활용할 수는 없다.
- `Point`의 하위 클래스는 정의상 여전히 `Point`이므로 어디서든 `Point`로 활용될 수 있어야 하지만 이 방식에서는 그렇지 못한다.
- 즉 **리스코프 치환원칙**을 만족하지 못한다.
- 따라서 `getClass` 대신 `instanceof`를 사용하여 `equals`를 재정의해주는 편이 좋다.

😉 `리스코프 치환원칙` : 상위 타입의 객체를 하위 타입의 객체로 치환해도 상위 타입을 사용하는 프로그램은 정상적으로 동작해야 한다. <br>
😱 IntelliJ의 equals 자동완성 기능은 `getClass`를 사용할 수 있기 때문에 주의해야 한다.

<br>

```java
private static final Set<Point> unitCircle = Set.of(new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1));

public static boolean onUnitCircle(Point p) {
    return unitCircle.contains(p);
}
```
```java
public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(final int x, final int y, final Color color) {
        super(x, y);
        this.color = Objects.requireNonNull(color);
    }
}
```
```java
System.out.println(onUnitCircle(new ColorPoint(0, 1, Color.RED))); // false
```
- 😅 다만 `hashCode`까지 잘 재정의해주어야 `instanceof`로 구현했을 때 true가 나온다.

<br>
   
#### 2. 괜찮은 우회 방법으로 "상속 대신 조합을 사용하라"는 이펙티브 자바-아이템 18을 활용해 볼 수 있다.
```java
public class ColorPoint {
    private final Point point;
    private final Color color;

    public ColorPoint(final int x, final int y, final Color color) {
        point = new Point(x,y);
        this.color = Objects.requireNonNull(color);
    }
    
    // Point를 비교하고 싶다면 ColorPoint에서 Point만 얻어와서 비교할 수 있다.
    public Point getPoint() {
        return Point;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint)) {
            return false;
        }
        ColorPoint cp = (Colorpoint) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }
}
```

<br>

## 일관성 (Consistency)
- 두 객체가 같다면 (어느 하나 혹은 두 객체 모두가 수정되지 않는 한) 앞으로도 영원히 같아야 한다는 뜻이다.
- 특히 불변 객체는 한 번 다르면 끝까지 달라야 하고 한 번 같으면 끝까지 같아야 한다.

<br>

## null 아님
- 의도하지 않음에도 `equals`를 호출하면서 null을 사용할 때 true나 false를 반환하는 상황은 거의 발생하지 않지만 `NullPointException`을 던지는 코드는 흔하다. 
- 수많은 코드가 명시적으로 null을 검사하지만 묵시적으로 null을 검사하는 것으로 충분하다.
```java
// 명시적 null 검사
@Override
public boolean equals(Object o) {
    if (o == null) {
        return false;
    }
}
```
```java
// 묵시적 null 검사
@Override
public boolean equals(Object o) {
    if (!(o instanceof MyType)) {
        return false;
    }
    MyType myType = (MyType) o;
    ... //이하 생략
}
```

<br>

## 양질의 equals 메서드 구현 방법
### 1. `==` 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다.
### 2. `instanceof` 연산자로 입력이 올바른 타입인지 확인한다.
### 3. 입력을 올바른 타입으로 형변환한다.
### 4. 입력 객체가 자기 자신의 대응되는 '핵심' 필드들이 모두 일치하는지 하나씩 검사한다.

<br>

```java
@Override
public boolean equals(Object o) {
    if (o == this) { // 1. 자기 자신의 참조인지 확인한다.
        return true;
    }
    
    if (!(o instanceof Point)) { // 2. instanceof 연산자로 입력이 올바른 타입인지 확인한다.
        return false;
    }
    
    Point p = (Point) o; // 3. 입력을 올바른 타입으로 형변환한다.
    
    return p.x == x && p.y == y; // 4. 핵심 필드들이 일치하는지 확인한다.
}
```

<br>

## 참고 사항
### 🖤 float와 double을 제외한 기본 타입 필드는 `==`으로 비교하고, 참조 타입 필드는 `equals`메서드로, float와 double은 정적 메서드인 `compare`로 비교한다.
### 🖤 null을 정상 값으로 취급하는 참조 타입 필드를 비교할 땐 `Objects.equals`를 활용할 수 있다.
### 🖤 `equals`를 다 구현했다면 `대칭성`, `추이성`, `일관성`에 대해 테스트를 작성하여 확인해보자.
### 🖤 `equals`를 재정의할 땐 `hashCode`로 반드시 재정의하자. (아이템 11)
### 🖤 Object 외의 타입을 매개변수로 받는 equals 메서드는 선언하지 말자.
```java
// 재정의가 아닌 다중정의이다.
public boolean equals(Point p) {
    ...
}
```
```java
@Override // 문법에 맞지 않아 컴파일 되지 않음
public boolean equals(Point p) {
    ...    
}
```
### 🖤 [구글 AutoValue 프레임워크](https://plugins.jetbrains.com/plugin/8091-autovalue-plugin)를 활용해볼 수 있다.
