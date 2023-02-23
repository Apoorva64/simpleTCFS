package fr.univcotedazur.simpletcfs.components;

import fr.univcotedazur.simpletcfs.entities.*;
import fr.univcotedazur.simpletcfs.interfaces.CustomerRegistration;
import fr.univcotedazur.simpletcfs.interfaces.OrderProcessing;
import fr.univcotedazur.simpletcfs.interfaces.Tracker;
import fr.univcotedazur.simpletcfs.repositories.CustomerRepository;
import fr.univcotedazur.simpletcfs.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class KitchenTest {

    @Autowired
    private CustomerRegistration registry;

    @Autowired
    private OrderProcessing processor;

    @Autowired
    private Tracker tracker;

    @Autowired
    private OrderRepository orderRepository;

    private Order inProgress;

    @BeforeEach
    public void setUpContext() throws Exception {
        Set<Item> items = new HashSet<>();
        items.add(new Item(Cookies.CHOCOLALALA, 3));
        items.add(new Item(Cookies.DARK_TEMPTATION, 2));
        Customer john = registry.register("john", "1234-896983");
        Order newOrder = new Order(john, items);
        john.add(inProgress);
        inProgress = orderRepository.save(newOrder);
    }

    @Test
    void processCommand() throws Exception {
        processor.process(inProgress);
        assertEquals(OrderStatus.IN_PROGRESS, tracker.retrieveStatus(inProgress.getId()));
    }

}