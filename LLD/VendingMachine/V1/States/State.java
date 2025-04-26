package LLD.VendingMachine.V1.States;

import LLD.VendingMachine.V1.PaymentMode;
import LLD.VendingMachine.V1.VendingMachineException;

public interface State {

    void proceed_to_payment() throws VendingMachineException;
    void select_mode(PaymentMode mode) throws VendingMachineException;
    void add_cash(int amount) throws VendingMachineException;
    void upi_payment(int amount) throws VendingMachineException;
    void dispatch_items() throws VendingMachineException;
    void return_receipt() throws VendingMachineException;
    
}