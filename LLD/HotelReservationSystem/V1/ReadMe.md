
## Different Approaches Discussed

### Approach 1: Centralized (HotelManagementService)
**Description:** All logic in one class.  
**Pros:** Simple, quick to code.  
**Cons:** Violates SRP, OCP; hard to scale, test, or extend.  
**Use Case:** Small demos, early prototypes.  

---

### Approach 2: Modular Services with Facade
**Description:** Separate services with HotelManagementFacade.  
**Pros:** Adheres to SRP, OCP; scalable, testable, extensible.  
**Cons:** More classes, slightly complex.  
**Use Case:** LLD interviews, production-ready systems.  

---

### Approach 3: Interface-Based (Latest)
**Description:** Modular services with interfaces (RoomRepository, etc.).  
**Pros:** Loose coupling (DIP), highly testable, ready for microservices.  
**Cons:** More abstractions, increased complexity.  
**Use Case:** Advanced LLD, transition to HLD, real-world systems.  
