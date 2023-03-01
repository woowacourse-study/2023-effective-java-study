package chap04.item18.code;

public class CustomSimpleArrayList extends ForwardingSimpleArrayList {
    private int addCount;

    public CustomSimpleArrayList(SimpleArrayList simpleArrayList) {
        super(simpleArrayList);
        addCount = 0;
    }

    @Override
    public void add(String value) {
        addCount++;
        super.add(value);
    }

    @Override
    public void addAll(String ... values) {
        addCount += values.length;
        super.addAll(values);
    }

    public int getAddCount() {
        return addCount;
    }
}
