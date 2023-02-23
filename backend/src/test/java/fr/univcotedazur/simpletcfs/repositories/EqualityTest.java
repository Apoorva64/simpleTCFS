package fr.univcotedazur.simpletcfs.repositories;

import fr.univcotedazur.simpletcfs.entities.Cookies;
import fr.univcotedazur.simpletcfs.entities.Customer;
import fr.univcotedazur.simpletcfs.entities.Item;
import fr.univcotedazur.simpletcfs.entities.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EqualityTest {

    Customer john;
    Order johnsOrder;

    @BeforeEach
    void setup() {
        john = new Customer("john", "1234567890");
        johnsOrder = new Order(john, new HashSet<>(Arrays.asList(new Item(Cookies.CHOCOLALALA,2))));
    }

    @Test
    void testCustomerEquals() {
        assertEquals(john, john);
        Customer otherJohn = new Customer("john", "1234567890");
        assertEquals(john, otherJohn);
        assertEquals(otherJohn, john);
    }

    @Test
    void testCustomerNotEquals() {
        assertEquals(john, john);
        Customer otherJohn = new Customer("johnn", "1234567890");
        assertNotEquals(john, otherJohn);
        assertNotEquals(otherJohn, john);
        Customer anotherJohn = new Customer("john", "1234567891");
        assertNotEquals(john, anotherJohn);
        assertNotEquals(anotherJohn, john);
    }

    @Test
    void testOrderEquals() {
        assertEquals(johnsOrder, johnsOrder);
        Order otherOrder = new Order(john, new HashSet<>(Arrays.asList(new Item(Cookies.CHOCOLALALA,2))));
        assertEquals(johnsOrder, otherOrder);
        assertEquals(otherOrder, johnsOrder);
    }

}
