package t.one.java.pro.model;

public record Test(String name, TestResult result, Throwable exception) {

    @Override
    public String toString() {
        return "Test{" +
                "name='" + name + '\'' +
                ", result=" + result +
                (exception != null ? ", exception=" + exception : "") +
                '}';
    }
}

