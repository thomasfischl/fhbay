package sve2.fhbay.interfaces;

import java.util.List;

import javax.ejb.Remote;

import sve2.fhbay.domain.Customer;

@Remote
public interface SimpleCustomerAdminRemote {
  Long saveCustomer(Customer customer);

  Customer findCustomerById(Long id);

  List<Customer> findAllCustomers();
}
