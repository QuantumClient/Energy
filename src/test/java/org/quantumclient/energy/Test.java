package org.quantumclient.energy;

public class Test {

    private final EventBus eventBus;

    public static void main(String[] args) {
        Test test = new Test();
        test.test();
    }

    public Test() {
        eventBus = new EventBus();
        eventBus.register(this);
        eventBus.post(new TestEvent("Hi"));
    }

    @Subscribe
    public void onTest(TestEvent event) {
        System.out.println(event.data);
    }

    public void test() {
        eventBus.post(new TestEvent("Test from other method"));
    }

}