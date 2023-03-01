package chap04.item18.code;

public class SimpleArrayList {

    private String[] simpleArrayList;
    private int size;

    public SimpleArrayList() {
        simpleArrayList = new String[2];
        size = 0;
    }

    public void add(String value) {
        if (size == simpleArrayList.length) {
            makeListSizeDouble();
        }
        simpleArrayList[size] = value;
        size += 1;
    }

    public void addAll(String ... values) {
        for (String value : values) {
            add(value);
        }
    }

    private void makeListSizeDouble() {
        String[] simpleArrayList = new String[size * 2];
        for (int index = 0; index < size; index++) {
            simpleArrayList[index] = this.simpleArrayList[index];
        }
        this.simpleArrayList = simpleArrayList;
    }
}
