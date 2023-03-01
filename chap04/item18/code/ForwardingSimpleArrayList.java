package chap04.item18.code;

public class ForwardingSimpleArrayList extends SimpleArrayList{

    private SimpleArrayList simpleArrayList;

    public ForwardingSimpleArrayList(SimpleArrayList simpleArrayList) {
        this.simpleArrayList = simpleArrayList;
    }

    @Override
    public void add(String value) {
        simpleArrayList.add(value);
    }

    @Override
    public void addAll(String ... values) {
        simpleArrayList.addAll(values);
    }
}
