package chap04.item18.code;

public class Main {
    public static void main(String[] args) {
        CustomSimpleArrayList customSimpleArrayList = new CustomSimpleArrayList(new SimpleArrayList());

        customSimpleArrayList.addAll("a", "b", "c", "d");
        System.out.println(customSimpleArrayList.getAddCount());
        System.out.println();

        WrapperCallbackTest wrapperCallbackTest = new WrapperCallbackTest();
        wrapperCallbackTest.run();
    }
}
