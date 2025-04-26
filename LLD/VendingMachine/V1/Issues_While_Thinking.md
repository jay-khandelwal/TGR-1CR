# Brainstorming: Vending Machine LLD

## ISSUES:

- Don't know about the state pattern
  - doing something related to state, but not state pattern, thinking of doing something weird
- For coins and notes, was thinking of making interfaces and classes like FiveRupeeCoin, TenRupeeCoin, TwentyRupeeNote, etc., instead of simply using an enum. 😂
- Confused were to put fetch recipt state, just after payment or after dispaching items

## thinking:

- Should we make only CASH payment or can also make UPI Payment
- In Cash:
    - shoult i accept Note
    - or also support coins
    
    - In case accepting both note and coin:
        - Should I make a common interface or base class for `Money`?
        - If Interface: 
            - shoult i write a methods like:
                - `get_value()`, or what
    - shoult i make a factory for making money
        - If yes, how would the factory decide whether to create a Coin or a Note?

    - Should i create separate interfaces for Coin and Note, and separate factory classes for both?

- when the user made the payment
    - shoud we first return the recipt or first dispatch items

- Can we select multiple items 

## Things decided:

- States: `[IDLE, CHOOSE ITEMS, SELECT PAYMENT MODE, MAKE PAYMENT, [REFUND EXTRA MONEY], DISPACH ITEM]`
- Only cash / UPI payment is allowed
