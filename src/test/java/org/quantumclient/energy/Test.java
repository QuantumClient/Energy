package org.quantumclient.energy;

public class Test {

    private static EventBus eventBus;

    public static void main(String[] args) {
        eventBus = new EventBus();
        for (int i = 0; i < 250; i++) {
            Test test = new Test();
            eventBus.register(test);
        }

        long total = 0;
        for (int b = 0; b < 20; b++) {
            long startTime = System.nanoTime();
            TestEvent event = new TestEvent("Hello");
            for (int i = 0; i < 2000; i++) {
                eventBus.post(event);
            }
            long endTime = System.nanoTime();
            long time = (endTime - startTime) / 1000000;
            System.out.println("Execution time in milliseconds: " + time);
            total += time;
        }

        System.out.println("Execution time average in milliseconds: " + total / 20);

    }

    @Subscribe()
    public void onTest(TestEvent event) {
        if (10 > event.data.length()) {
            System.nanoTime();
        } else {
            int i = 10;
            i = event.data.getBytes().length;
        }
    }

    public void test() {
        eventBus.post(new TestEvent("Test from other method"));
    }

}