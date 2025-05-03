
- A building
- Has many floors
- Elevator helps moving from one floor to another
- Building contains multiple elevators
- Each elevator is connected with elevator controller system

- Person press button given on floor with UP/DOWM option [Request from outside]
    - Request goes to elevator controller system, which decide which elevtor will serve this request
- Person gets in Elevator
- Person press the floor, were he wants to go [Request from inside]
- 



Techinal:
- Lets assume each elevator as a seperate thread
- Elevator State = [IDLE, RUNNING, MAINTENANCE]
