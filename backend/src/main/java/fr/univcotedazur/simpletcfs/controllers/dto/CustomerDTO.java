package fr.univcotedazur.simpletcfs.controllers.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CustomerDTO {

    private Long id; // expected to be empty when POSTing the creation of Customer, and containing the Id when returned

    @NotBlank(message = "name should not be blank")
    private String name;

    @Pattern(regexp = "\\d{10}+", message = "credit card should be exactly 10 digits")
    private String creditCard;

    public CustomerDTO(Long id, String name, String creditCard) {
        this.id = id;
        this.name = name;
        this.creditCard = creditCard;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

}
