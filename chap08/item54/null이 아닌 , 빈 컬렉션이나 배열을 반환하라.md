# null이 아닌, 빈 컬렉션이나 배열을 반환하라

### null을 반환하지 마라
- 컬렉션이나 배열 같은 컨테이너(container)가 비었을 때 null을 반환하는 메서드를 사용하면 항상 방어 코드를 넣어줘야 한다. 클라이언트에서 방어 코드를 빼먹으면 오류가 발생할 수도 있다.
- 반환하는 쪽에서도 이 상황을 특별히 취급해줘야 해서 코드가 더 복잡해진다.

*예시 코드) 컬렉션이 비었으면 `null`을 반환한다. **(이렇게 작성하지 말 것!)***

```java
private final List<Cheese> cheesesInStock = ...;

/**
 * @return 매장 안의 모든 치즈 목록을 반환한다.
 *     단, 재고가 하나도 없다면 null을 반환한다.
 */
public List<Cheese> getCheeses() {
    if (cheeseInStock.isEmpty()) {
        return null;
    }
    return new ArrayList<>(CheeseInStock);
}
```

반환할 요소가 없다고 특별 취급할 이유는 없다. 만약 `null`을 반환한다면, 클라이언트는 이 `null` 상황을 처리하는 아래와 같은 코드를 추가로 작성해주어야 한다. <br/>

```java
List<Cheese> cheese = shop.getCheeses();
if (cheeses != null && cheeses.contains(Cheese.STILTON)) {
    System.out.println("좋았어, 바로 그거야!");
}
```

<hr/>

### 빈 컨테이너를 할당하는 데 비용이 드니 null을 반환하는 것이 낫지 않을까? 
- 이는 두 가지 측면에서 틀린 주장이다
  1. 성능 분석 결과 이 할당이 성능 저하의 주범이라고 확인되지 않는 한, 이 정도의 성능 차이는 신경 쓸 수준이 못 된다.
  2. 빈 컬렉션과 배열은 굳이 새로 할당하지 않고도 반환할 수 있다. 할당에 따른 성능이 떨어질 것이라고 판단된다면 불변 컬렉션이나 길이가 0인 배열을 반환하면 된다.

<br/>

*예시 코드) 빈 컬렉션, 길이가 0일 수도 있는 배열을 반환하는 올바른 방법*

```java
public List<Cheese> getCheeses() {
    return new ArrayList<>(chessesInStock);
}

public Cheese[] getCheeses() {
    return cheesesInstock.toArray(new Cheese[0]);
}
```

<br/>

*예시 코드) 최적화. 빈 컬렉션이나 배열을 매번 새로 할당하지 않고 반환하기*

```java
public List<Cheese> getCheeses() {
    if (cheesesInStock.isEmpty()) {
        return Collections.emptyList();
    }
    return new ArrayList<>(cheeseInStock);
}
```
```java
public static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];

public Cheese[] getCheeses() {
    return cheesesInStock.toArray(EMPTY_CHEESE_ARRAY);
}
```

<br/>

### 👀 참고 : 불변 컬렉션과 길이가 0인 배열
`Collections.emptyList()`, `Collections.emptySet()`, `Collections.emptyMap()` 등의 빈 컬렉션들과 `길이가 0인 배열`은 모두 불변이기 때문에 새로운 할당 없이 항상 같은 객체를 반환한다.  
