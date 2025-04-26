package LLD.VendingMachine.V1.States;

import LLD.VendingMachine.V1.PaymentMode;
import LLD.VendingMachine.V1.VendingMachine;
import LLD.VendingMachine.V1.VendingMachineException;

public class SelectItemState implements State {
    VendingMachine machine;

    public SelectItemState(VendingMachine machine) {
        this.machine = machine;
    }
    
    @Override
    public void proceed_to_payment() throws VendingMachineException {
        System.out.println("\nProceeding to payment. Please select payment mode.");
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
        throw new VendingMachineException("Receipt cannot be returned in this state.");
    }
}
