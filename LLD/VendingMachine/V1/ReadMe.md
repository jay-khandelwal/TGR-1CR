

Going to use state struture pattern

STATE:
    - proceed_to_payment(): SELECT ITEMS
    - select_mode(Enum: MODE): SELECT PAYMENT MODE
    - add_coin() + add_note(): PROCESS CASH PAYMENT
    - upi_payment(): PROCESS UPI PAYMENT
    - dispatch_items(): DISPATCH ITEMS
    - print_receipt(): PRINT RECEIPT


States:
    - SELECT ITEMS
    - SELECT PAYMENT MODE
    - PROCESS CASH PAYMENT
    - PROCESS UPI PAYMENT
    - DISTATCH ITEMS
    - RETURN RECIPT