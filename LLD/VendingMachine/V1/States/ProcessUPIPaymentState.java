package LLD.VendingMachine.V1.States;

import LLD.VendingMachine.V1.PaymentMode;
import LLD.VendingMachine.V1.VendingMachine;
import LLD.VendingMachine.V1.VendingMachineException;

public class ProcessUPIPaymentState implements State {
    VendingMachine machine;

    public ProcessUPIPaymentState(VendingMachine machine) {
        this.machine = machine;
    }
    
    @Override
    public void proceed_to_payment() throws VendingMachineException {
        throw new VendingMachineException("Items already added. Select payment mode.");
    }

    @Override
    public void select_mode(PaymentMode mode) throws VendingMachineException {
        throw new VendingMachineException("Payment mode already selected. Put the money to proceed.");
    }

    @Override
    public void add_cash(int amount) throws VendingMachineException {
        throw new VendingMachineException("Please select payment mode before making Cash payment.");
    }

    @Override
    public void upi_payment(int amount) throws VendingMachineException {
        // Instead of checking here that amountPayable is equal to amountAdded or not. I am doing it in VendingMachine class
        // Maybe we can move it here and raise exception from here if amountPayable != amountAdded.
        // bcz in case of UPI payment we must reveice all the payment all together
        System.out.println("UPI payment of amount: " + amount + " received.");
        machine.addAmount(amount);
        machine.setState(machine.getDispatchItemsState());
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
