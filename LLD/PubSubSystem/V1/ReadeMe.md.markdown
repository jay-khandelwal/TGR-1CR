# Pub-Sub System Design

A concise guide for designing a Java Publish-Subscribe (Pub-Sub) system, focusing on managing consumers and publishers to ensure loose coupling, thread safety, and SOLID compliance.

## System Overview
- **Message**: Immutable data (content, timestamp).
- **Topic**: Manages subscribers and message queue.
- **Consumer**: Processes messages (e.g., `ConsoleConsumer`).
- **Publisher**: Publishes messages (e.g., `ConcretePublisher`).
- **PubSubController**: Coordinates topics, subscriptions, and publishing.

## Design Choices: Consumer and Publisher Management

**Option 1: Tightly Coupled (Consumers/Publishers Know Controller)**
- **Description**: `Consumer` and `Publisher` hold `PubSubController` references. `Consumer` calls `controller.subscribe(this, topic)` via `subscribe()`, and `Publisher` calls `controller.publish(this, topic, message)` via `publish()`.
- **Cons**:
  - Tight coupling to `PubSubController`, violating **Dependency Inversion Principle (DIP)**.
  - Extra responsibilities for `Consumer` (subscription) and `Publisher` (publishing coordination), breaking **Single Responsibility Principle (SRP)**.
  - Hard to test due to controller dependency.
  - Hard to switch brokers; adds unnecessary abstraction.
- **Use**: Simple systems where convenience trumps maintainability.

**Option 2: Loosely Coupled (Controller-Managed)**
- **Description**: Client calls `controller.subscribe(consumer, topic)` and `controller.publish(publisher, topic, message)` directly. `Consumer` and `Publisher` are independent, with `Publisher` using `topic.broadcast`.
- **Pros**:
  - Loose coupling: No controller dependency, improving testability and reusability.
  - Adheres to **SRP**: `PubSubController` handles all coordination.
  - Thread-safe: Uses `ConcurrentHashMap` (topics) and `CopyOnWriteArrayList` (subscribers).
  - Simpler design: Fewer abstractions, easy to debug.
- **Cons**: Slightly verbose client code.
- **Use**: Default for scalable, maintainable systems.

**Option 3: Factory Pattern**
- **Description**: A factory creates `Consumer` and `Publisher` instances configured for a specific controller, encapsulating controller details.
- **Pros**: Controlled coupling, supports multiple controllers.
- **Cons**: Adds factory complexity.
- **Use**: Systems with multiple or dynamic controllers.

**Recommended**: Option 2 for simplicity, loose coupling, and thread safety. Use Option 3 for complex controller setups.

## Guiding Principles
1. **Centralize Coordination**: `PubSubController` manages state and actions.
2. **Minimize Coupling**: Avoid `PubSubController` in `Consumer`/`Publisher`.
3. **Thread Safety**: Use `ConcurrentHashMap` and `CopyOnWriteArrayList`.
4. **SOLID**:
   - **SRP**: One role per class.
   - **DIP**: Depend on interfaces.
   - **OCP**: Design for extensibility.
5. **Explicit Control**: Use `controller.subscribe/publish` for clarity.
6. **Testability**: Ensure dependency-free classes.

## Example Usage
```java
PubSubController controller = new PubSubController();
controller.createTopic("news");

Consumer sub1 = new ConsoleConsumer("sub1");
Publisher pub1 = new ConcretePublisher("pub1");

controller.subscribe(sub1, "news");
controller.publish(pub1, "news", new Message("Breaking News!"));
controller.unsubscribe(sub1, "news");
```

## Conclusion
Use `controller.subscribe(consumer, topic)` and `controller.publish(publisher, topic, message)` for a loosely coupled, thread-safe Pub-Sub system. Consider a factory pattern for multiple controllers. Refer to this guide for clean, scalable designs.