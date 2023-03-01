package chap04.item18.test;

import chap04.item18.code.CustomSimpleArrayList;
import chap04.item18.code.SimpleArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomSimpleArrayListTest {

    @Test
    void addAllTest() {
        CustomSimpleArrayList customSimpleArrayList = new CustomSimpleArrayList(new SimpleArrayList());
        customSimpleArrayList.addAll("a", "b", "c", "d");

        System.out.println(customSimpleArrayList.getAddCount());
        Assertions.assertEquals(customSimpleArrayList.getAddCount(), 4);
    }
}
