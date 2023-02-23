package fr.univcotedazur.simpletcfs.interfaces;

import fr.univcotedazur.simpletcfs.entities.OrderStatus;
import fr.univcotedazur.simpletcfs.exceptions.UnknownOrderId;

public interface Tracker {

    OrderStatus retrieveStatus(Long orderId) throws UnknownOrderId;

}