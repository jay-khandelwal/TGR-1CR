package LLD.PubSubSystem.V1.Approach_2_Loosely_Coupled;

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

interface Consumer {
    void onMessage(Topic topic, Message message);
}

class BasicConsumer implements Consumer {

    public void onMessage(Topic topic, Message message) {
        System.out.println("Topic: " + topic.getName() + " | Message: " + message.getData());
    }
}

interface Publisher {}

class BasicPublisher implements Publisher{}

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
        Publisher pub1 = new BasicPublisher();
        Consumer sub1 = new BasicConsumer();
        Consumer sub2 = new BasicConsumer();

        controller.subscribe(sub1, "jack");
        controller.subscribe(sub2, "jack");
        controller.publish(pub1, "jack", new Message("this is a message"));
    }

}
