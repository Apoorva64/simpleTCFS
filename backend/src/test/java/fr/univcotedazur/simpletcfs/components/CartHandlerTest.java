package fr.univcotedazur.simpletcfs.components;

import fr.univcotedazur.simpletcfs.interfaces.CartModifier;
import fr.univcotedazur.simpletcfs.interfaces.CartProcessor;
import fr.univcotedazur.simpletcfs.interfaces.CustomerFinder;
import fr.univcotedazur.simpletcfs.interfaces.CustomerRegistration;
import fr.univcotedazur.simpletcfs.entities.Cookies;
import fr.univcotedazur.simpletcfs.entities.Customer;
import fr.univcotedazur.simpletcfs.entities.Item;
import fr.univcotedazur.simpletcfs.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.simpletcfs.exceptions.EmptyCartException;
import fr.univcotedazur.simpletcfs.exceptions.NegativeQuantityException;
import fr.univcotedazur.simpletcfs.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest // you can make test non transactional to be sure that transactions are properly handled in
        // controller methods (if you are actually testing controller methods!)
// @Transactional
// @Commit // default @Transactional is ROLLBACK (no need for the @AfterEach
class CartHandlerTest {

    @Autowired
    private CartModifier cartModifier;

    @Autowired
    private CartProcessor cartProcessor;

    @Autowired
    private CustomerRegistration customerRegistration;

    @Autowired
    private CustomerFinder customerFinder;

    @Autowired
    CustomerRepository customerRepository;

    private Customer john;

    @BeforeEach
    void setUp() throws AlreadyExistingCustomerException {
        john = customerRegistration.register("John", "1234567890");
    }

    @AfterEach
    public void cleaningUp()  {
        Optional<Customer> toDispose = customerRepository.findCustomerByName("John");
        if (toDispose.isPresent()) {
            customerRepository.delete(toDispose.get());
        }
        john = null;
    }

    @Test
    public void emptyCartByDefault() {
        assertEquals(0, cartProcessor.contents(john).size());
    }

    @Test
    public void addItems() throws NegativeQuantityException {
        cartModifier.update(john, new Item(Cookies.CHOCOLALALA, 2));
        cartModifier.update(john, new Item(Cookies.DARK_TEMPTATION, 3));
        Set<Item> oracle = Set.of(new Item(Cookies.CHOCOLALALA, 2), new Item(Cookies.DARK_TEMPTATION, 3));
        assertEquals(oracle, cartProcessor.contents(john));
    }

    @Test
    public void removeItems() throws NegativeQuantityException {
        cartModifier.update(john, new Item(Cookies.CHOCOLALALA, 2));
        cartModifier.update(john, new Item(Cookies.CHOCOLALALA, -2));
        assertEquals(0, cartProcessor.contents(john).size());
        cartModifier.update(john, new Item(Cookies.CHOCOLALALA, 6));
        cartModifier.update(john, new Item(Cookies.CHOCOLALALA, -5));
        Set<Item> oracle = Set.of(new Item(Cookies.CHOCOLALALA, 1));
        assertEquals(oracle, cartProcessor.contents(john));
    }

    @Test
    public void removeTooMuchItems() throws NegativeQuantityException {
        cartModifier.update(john, new Item(Cookies.CHOCOLALALA, 2));
        cartModifier.update(john, new Item(Cookies.DARK_TEMPTATION, 3));
        Assertions.assertThrows(NegativeQuantityException.class, () -> {
            cartModifier.update(john, new Item(Cookies.CHOCOLALALA, -3));
        });
        Set<Item> oracle = Set.of(new Item(Cookies.CHOCOLALALA, 2), new Item(Cookies.DARK_TEMPTATION, 3));
        assertEquals(oracle, cartProcessor.contents(john));
    }

    @Test
    public void modifyQuantities() throws NegativeQuantityException {
        cartModifier.update(john, new Item(Cookies.CHOCOLALALA, 2));
        cartModifier.update(john, new Item(Cookies.DARK_TEMPTATION, 3));
        cartModifier.update(john, new Item(Cookies.CHOCOLALALA, 3));
        Set<Item> oracle = Set.of(new Item(Cookies.CHOCOLALALA, 5), new Item(Cookies.DARK_TEMPTATION, 3));
        assertTrue(oracle.contains(new Item(Cookies.CHOCOLALALA, 5)));
        assertEquals(oracle, cartProcessor.contents(john));
    }

    @Test
    public void getTheRightPrice() throws NegativeQuantityException {
        cartModifier.update(john, new Item(Cookies.CHOCOLALALA, 2));
        cartModifier.update(john, new Item(Cookies.DARK_TEMPTATION, 3));
        cartModifier.update(john, new Item(Cookies.CHOCOLALALA, 3));
        assertEquals(12.20, cartProcessor.price(john), 0.01);
    }

    @Test
    public void cannotProcessEmptyCart() throws Exception {
        assertEquals(0, cartProcessor.contents(john).size());
        Assertions.assertThrows(EmptyCartException.class, () -> {
            cartProcessor.validate(john);
        });
    }

}