package LLD.VendingMachine.V1.States;

import java.util.Map;

import LLD.VendingMachine.V1.Item;
import LLD.VendingMachine.V1.PaymentMode;
import LLD.VendingMachine.V1.VendingMachine;
import LLD.VendingMachine.V1.VendingMachineException;

public class ReturnReceiptState implements State {
    VendingMachine machine;

    public ReturnReceiptState(VendingMachine machine) {
        this.machine = machine;
    }
    
    @Override
    public void proceed_to_payment() throws VendingMachineException {
        this.machine.setState(this.machine.getSelectPaymentModeState());
    }

    @Override
    public void select_mode(PaymentMode mode) throws VendingMachineException {
        throw new VendingMachineException("Please select items before selecting payment mode.");
    }

    @Override
    public void add_cash(int amount) throws VendingMachineException {
        throw new VendingMachineException("Please select items before adding coin");
    }

    @Override
    public void upi_payment(int amount) throws VendingMachineException {
        throw new VendingMachineException("Please select items before making UPI payment.");
    }

    @Override
    public void dispatch_items() throws VendingMachineException {
        throw new VendingMachineException("Cannot dispatch items in this state.");
    }

    @Override
    public void return_receipt() throws VendingMachineException {
        System.out.println("\nReceipt:");
        for (Map.Entry<Item, Integer> entry : machine.getSelectedItems().entrySet()) {
            System.out.println("Item: " + entry.getKey().getName() + ", Quantity: " + entry.getValue());
        }
        machine.reset();
    }
}
