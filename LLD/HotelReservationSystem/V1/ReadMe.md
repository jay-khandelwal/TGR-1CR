
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

---

## Summary of HMS Design Discussions

### 1. Initial Design: Centralized Logic
**Description:**  
A single HotelManagementService class handles all responsibilities—rooms, guests, bookings, payments, invoices, and staff.

**Features:**  
- Staff-oriented: Simulates workflows via programmatic method calls (no Scanner).  
- Supports multiple bookings with date-overlap checks.  
- In-memory storage using ArrayList.  
- Custom exceptions: e.g., RoomNotAvailableException, DateOverlapException.  

**Issues:**  
- **God Class:** Violates SRP by handling too many responsibilities.  
- **Poor Testability:** Tight coupling makes unit testing hard.  
- **Low Extensibility:** Adding features (e.g., discounts) requires modifying core logic (violates OCP).  
- **Limited Scalability:** Difficult to extend to database or distributed systems.  

**Trade-Offs:**  
- ✅ Simple and fast for demos.  
- ❌ Not maintainable or scalable for real-world use.  

**Interview Fit:** Basic; likely to be criticized for lack of modularity.

---

### 2. Improved Design: Modular Services + Facade
**Description:**  
Decomposed responsibilities into modular services, orchestrated by a HotelManagementFacade.

**Modular Services:**  
- RoomService, GuestService, BookingService, PaymentService, InvoiceService, StaffService.  

**Dependency Injection:**  
Services depend on each other via interfaces.

**Advantages:**  
- ✅ SRP/OCP compliant.  
- ✅ Easy to test (unit-test services independently).  
- ✅ Extensible (e.g., add DiscountService without modifying existing code).  
- ✅ Interview-friendly (shows grasp of design patterns like Facade and DI).  

**Trade-Offs:**  
- ❌ Slightly more complex.  
- ✅ Worthwhile for modularity and scalability.  

**Interview Fit:** Strong LLD design showcasing SOLID principles.

---

### 3. Raised Doubts & Resolutions

**Doubt 1: Tight Coupling in Services**  
- **Concern:** BookingService depends on concrete RoomService.  
- **Solution:** Introduce interfaces like RoomRepository, inject abstractions instead of implementations.  
- **Result:** Loose coupling, testable with mocks, DIP-compliant.  
- **Interview Note:** "Interfaces enable flexibility and testability via DIP."

**Doubt 2: Communication Between Services**  
- **Monolith:** Direct method calls between services (fast, simple).  
- **Microservices:** Use REST, gRPC, Kafka; handle service discovery via Kubernetes.  
- **Interview Note:** "Use direct calls for LLD; REST/gRPC or Kafka for scalable microservices."

**Doubt 3: Same vs. Separate Databases**  
- **Single DB:** Easier joins, consistent transactions; tight coupling.  
- **Separate DBs:** Loose coupling, tech flexibility; eventual consistency.  
- **Hybrid Option:** Isolate critical services (e.g., PaymentService for PCI).  
- **Interview Note:** "Single DB for LLD; separate DBs for microservices scalability."

**Doubt 4: Handling Relationships Without FKs**  
- **Single DB:** Use foreign keys (e.g., bookings.guest_id).  
- **Separate DBs:** Store IDs, use API calls, denormalize, or use event-driven sync (e.g., Kafka, Saga pattern).  
- **Interview Note:** "FKs in monoliths; IDs + events/APIs for distributed systems."

---

### 4. Design Approaches Compared

| Approach            | Description                          | Pros                                   | Cons                                   | Use Case                              |
|---------------------|--------------------------------------|---------------------------------------|---------------------------------------|---------------------------------------|
| Centralized         | One god class for all logic         | Simple, fast setup                    | Violates SRP/OCP, hard to test/scale  | Small demos, quick prototypes         |
| Modular + Facade    | Services with unified interface     | SOLID-compliant, scalable, testable   | Slightly more complex                 | LLD interviews, real systems          |
| Modular + Interfaces| Services depend on abstractions (DIP)| Highly testable, extensible, microservice-ready | Extra abstraction, more setup         | Advanced LLD, HLD transition          |

---

### 5. Trade-Offs Summary

| Aspect         | Centralized       | Modular Services | Interface-Based Design |
|----------------|-------------------|------------------|------------------------|
| Code Size      | Minimal           | Moderate         | Larger                 |
| Maintainability| Low               | High             | Very High              |
| Scalability    | Poor              | Good             | Excellent              |
| Testability    | Hard              | Easy (per service)| Very Easy (mock interfaces) |
| Extensibility  | Poor (OCP violation)| Good            | Excellent (DIP, OCP compliant) |

---

### 6. Key Interview Talking Points
- **Design Principles:** SRP (modular services), OCP (easy extension), DIP (interfaces).  
- **Architecture Fit:** Start with in-memory + single DB (LLD), scale to microservices.  
- **Extensibility Ideas:** Add REST APIs, DB integration, Kafka events, guest portal.  
- **Concurrency:** Handle in createBooking with synchronization or DB locks.  
- **Testing:** Use JUnit + mock interfaces for unit tests.  
- **Relationships:** Use foreign keys (monolith) or API/event sync (microservices).

---

### 7. Final Notes
- ✅ **LLD Fit:** Interface-based modular design is optimal—showcases SOLID, testability, extensibility.  
- 🔄 **HLD Transition-Ready:** Prepped for microservices, distributed DBs, event-driven architecture.  
- 🧪 **Robustness:** Exception handling and validations (e.g., overlapping bookings).  
- 🧍 **Staff-Focused:** Main method simulates front desk workflows.  
- 📅 **Multiple Bookings:** Supports overlapping date validation.
