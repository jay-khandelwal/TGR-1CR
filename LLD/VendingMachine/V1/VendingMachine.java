package LLD.VendingMachine.V1;

import java.util.HashMap;
import java.util.Map;

import LLD.VendingMachine.V1.States.DispatchItemState;
import LLD.VendingMachine.V1.States.ProcessCashPaymentState;
import LLD.VendingMachine.V1.States.ProcessUPIPaymentState;
import LLD.VendingMachine.V1.States.ReturnReceiptState;
import LLD.VendingMachine.V1.States.SelectItemState;
import LLD.VendingMachine.V1.States.SelectPaymentModeState;
import LLD.VendingMachine.V1.States.State;

public class VendingMachine {

    private State currentState;
    private State selectItemsState;
    private State selectPaymentModeState;
    private State processCashPaymentState;
    private State processUPIPaymentState;
    private State dispatchItemsState;
    private State returnReceiptState;

    private int amountPayable = 0;
    private int amountAdded = 0;
    private Inventory inventory = new Inventory();
    private PaymentMode paymentMode;

    private Map<Item, Integer> selectedItems = new HashMap<>();

    public VendingMachine() {
        selectItemsState = new SelectItemState(this);
        selectPaymentModeState = new SelectPaymentModeState(this);
        processCashPaymentState = new ProcessCashPaymentState(this);
        processUPIPaymentState = new ProcessUPIPaymentState(this);
        dispatchItemsState = new DispatchItemState(this);
        returnReceiptState = new ReturnReceiptState(this);

        currentState = selectItemsState;
    }

    public void addInventory(Item item, int quantity) {
        inventory.addItem(item, quantity);
    }

    public boolean selectItem(Item item, int quantity) {
        if (inventory.isAvailable(item, quantity)) {
            int currentQty = selectedItems.getOrDefault(item, 0);
            currentQty += quantity;
            selectedItems.put(item, currentQty);
            updateAmountPayable(item.getPrice()*quantity);
            System.out.println("Selected " + quantity + " of " + item.getName() + " for " + item.getPrice() * quantity + " Rs" + " | Total cost: " + amountPayable + " Rs");
            return true;
        }
        return false;
    }

    public void proceedToPayment() {
        if (selectedItems.isEmpty()) {
            System.out.println("No items selected. Please select items first.");
            return;
        }
        currentState.proceed_to_payment();
    }

    public void selectPaymentMode(PaymentMode mode) {
        if (selectedItems.isEmpty()) {
            System.out.println("No items selected. Please select items first.");
            return;
        }
        currentState.select_mode(mode);
    }

    public void addCoin(Coin coin) {
        currentState.add_cash(coin.get_value());
    }

    public void addNote(Note note) {
        currentState.add_cash(note.get_value());
    }

    public void upiPayment(int amount) {
        if (amount != amountPayable) {
            System.out.println("Invalid UPI payment amount. Please pay the exact amount.");
            return;
        }
        currentState.upi_payment(amount);
    }

    public void dispatchItems() {
        currentState.dispatch_items();
    }

    public void printReceipt() {
        currentState.return_receipt();
    }

    public void returnChange(int amountToReturn) {
        System.out.println("\nReturning " + amountToReturn + " back to user.");
    }

    public void reset() {
        selectedItems.clear();
        amountPayable = 0;
        amountAdded = 0;
        paymentMode = null;
        currentState = selectItemsState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public State getSelectItemsState() {
        return selectItemsState;
    }

    public State getSelectPaymentModeState() {
        return selectPaymentModeState;
    }

    public State getProcessCashPaymentState() {
        return processCashPaymentState;
    }

    public State getProcessUPIPaymentState() {
        return processUPIPaymentState;
    }

    public State getDispatchItemsState() {
        return dispatchItemsState;
    }

    public State getReturnReceiptState() {
        return returnReceiptState;
    }

    public void setState(State state) {
        this.currentState = state;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getAmountAdded() {
        return amountAdded;
    }

    public void addAmount(int amountAdded) {
        this.amountAdded += amountAdded;
    }

    public int getAmountPayable() {
        return amountPayable;
    }

    public Map<Item, Integer> getSelectedItems() {
        return selectedItems;
    }

    public Inventory getInventory() {
        return inventory;
    }

    private void updateAmountPayable(int amountToAdd) {
        amountPayable += amountToAdd;
    }
}
