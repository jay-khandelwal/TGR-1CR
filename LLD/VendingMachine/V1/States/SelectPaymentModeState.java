package LLD.VendingMachine.V1.States;

import LLD.VendingMachine.V1.PaymentMode;
import LLD.VendingMachine.V1.VendingMachine;
import LLD.VendingMachine.V1.VendingMachineException;

public class SelectPaymentModeState implements State {
    VendingMachine machine;

    public SelectPaymentModeState(VendingMachine machine) {
        this.machine = machine;
    }
    
    @Override
    public void proceed_to_payment() throws VendingMachineException {
        throw new VendingMachineException("Items already added. Select payment mode.");
    }

    @Override
    public void select_mode(PaymentMode mode) throws VendingMachineException {
        machine.setPaymentMode(mode);
        if (mode == PaymentMode.CASH) {
            System.out.println("\nChoosen cash payment. Please insert money.");
            machine.setState(machine.getProcessCashPaymentState());
        } else if (mode == PaymentMode.UPI) {
            System.out.println("Choosen UPI payment. Please make payment.");
            machine.setState(machine.getProcessUPIPaymentState());
        } else {
            throw new VendingMachineException("Invalid payment mode selected.");
        }
    }

    @Override
    public void add_cash(int amount) throws VendingMachineException {
        throw new VendingMachineException("Please select payment mode before adding coin.");
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
