package LLD.VendingMachine.V1;

public enum Coin {
    FIVE(5), TEN(10), TWENTY(20);

    private final int value;

    private Coin(int value) {
        this.value = value;
    }

    public int get_value() {
        return this.value;
    }

}
