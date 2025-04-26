package LLD.VendingMachine.V1.States;

import LLD.VendingMachine.V1.PaymentMode;
import LLD.VendingMachine.V1.VendingMachine;
import LLD.VendingMachine.V1.VendingMachineException;

public class ProcessCashPaymentState implements State {
    VendingMachine machine;

    public ProcessCashPaymentState(VendingMachine machine) {
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
        machine.addAmount(amount);
        System.out.println("Adding cash: " + amount + " | Total amount added: " + machine.getAmountAdded());

        if (machine.getAmountAdded() < this.machine.getAmountPayable()) return;
        
        machine.setState(machine.getDispatchItemsState());
        if (machine.getAmountAdded() > this.machine.getAmountPayable()){
            machine.returnChange(machine.getAmountAdded() - this.machine.getAmountPayable());
        }
    }

    @Override
    public void upi_payment(int amount) throws VendingMachineException {
        throw new VendingMachineException("Please select payment mode before making UPI payment.");
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
