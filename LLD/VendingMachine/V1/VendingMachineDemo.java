package LLD.VendingMachine.V1;

public class VendingMachineDemo {
    
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();
        
        Item apple = new Item("Apple", 10);
        Item banana = new Item("Banana", 15);
        Item grapes = new Item("Grapes", 20);

        machine.addInventory(apple, 10);
        machine.addInventory(banana, 5);
        machine.addInventory(grapes, 8);

        System.out.println("Welcome to the Vending Machine! \n");

        machine.selectItem(apple, 2);
        machine.selectItem(banana, 1);
        machine.selectItem(grapes, 3);

        machine.proceedToPayment();

        machine.selectPaymentMode(PaymentMode.CASH);

        machine.addCoin(Coin.TWENTY);
        machine.addCoin(Coin.TWENTY);
        machine.addNote(Note.TEN);
        machine.addNote(Note.FIFTY);

        machine.dispatchItems();

        machine.printReceipt();

        System.out.println("\n\nThank you for using the Vending Machine!");
        
    }

}
