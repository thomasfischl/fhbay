package sve2.fhbay.interfaces;

import java.util.List;

import sve2.fhbay.domain.Customer;
import sve2.fhbay.exceptions.IdNotFoundException;

public interface CustomerAdmin {

  Long saveCustomer(Customer customer);

  Customer findCustomerById(Long id) throws IdNotFoundException;

  List<Customer> findAllCustomers();

}