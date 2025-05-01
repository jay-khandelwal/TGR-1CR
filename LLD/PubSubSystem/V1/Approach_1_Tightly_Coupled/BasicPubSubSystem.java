package LLD.PubSubSystem.V1.Approach_1_Tightly_Coupled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Message {
    String data;

    public Message(String data) {
        this.data = data;
    }

    public String getData() {
        return this.data;
    }
}

class Topic {
    private String name;
    private List<Consumer> consumers;

    public Topic(String name) {
        this.name = name;
        this.consumers = new ArrayList<>();
    }

    public void broadcast(Message message) {
        for (Consumer c : consumers) {
            c.onMessage(this, message);
        }
    }

    public void subscribe(Consumer consumer) {
        consumers.add(consumer);
    }

    public String getName() {
        return this.name;
    }

}

abstract class Consumer {
    private PubSubController controller;

    public Consumer(PubSubController controller) {
        this.controller = controller;
    }

    abstract void onMessage(Topic topic, Message message);

    public void subsribe(String topic_name) {
        controller.subscribe(this, topic_name);
    }

    public PubSubController getController() {
        return this.controller;
    }
}

class ConsoleConsumer extends Consumer {
    public ConsoleConsumer(PubSubController controller) {
        super(controller);
    }

    public void onMessage(Topic topic, Message message) {
        System.out.println("Topic: " + topic.getName() + " | Message: " + message.getData());
    }
}

class Publisher {
    private PubSubController controller;

    public Publisher(PubSubController controller) {
        this.controller = controller;
    }

    public void publish(String topic_name, Message message) {
        controller.publish(this, topic_name, message);
    }
}

class PubSubController {
    private Map<String, Topic> topics;

    public PubSubController() {
        this.topics = new HashMap<>();
    }

    private Topic getOrCreateTopic(String topic_name) {
        topics.putIfAbsent(topic_name, new Topic(topic_name));
        return topics.get(topic_name);
    }

    public void createTopic(String topic_name) {
        topics.putIfAbsent(topic_name, new Topic(topic_name));
    }

    public void subscribe(Consumer consumer, String topic_name) {
        Topic topic = getOrCreateTopic(topic_name);
        topic.subscribe(consumer);
    }

    public void publish(Publisher producer, String topic_name, Message message) {
        Topic topic = getOrCreateTopic(topic_name);
        System.out.println("Publishing message..." + message.getData());
        topic.broadcast(message);
    }
}

public class BasicPubSubSystem  {

    public static void main(String[] args) {
        PubSubController controller = new PubSubController();
        Publisher pub1 = new Publisher(controller);
        Consumer sub1 = new ConsoleConsumer(controller);
        Consumer sub2 = new ConsoleConsumer(controller);

        sub1.subsribe("jack");
        sub2.subsribe("jack");
        pub1.publish("jack", new Message("this is a message"));
    }

}
