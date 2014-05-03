package sve2.fhbay.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import sve2.fhbay.domain.Customer;
import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.CustomerAdminLocal;
import sve2.fhbay.interfaces.CustomerAdminRemote;
import sve2.fhbay.interfaces.dao.CustomerDao;

@Stateless
public class CustomerAdminBean implements CustomerAdminRemote, CustomerAdminLocal {

  @EJB
  private CustomerDao customerDao;

  @Override
  public Long saveCustomer(Customer customer) {
    System.out.println("CustomerAdminBean.saveCustomer(" + customer + ")");
    return customerDao.merge(customer).getId();
  }

  @Override
  public Customer findCustomerById(Long id) throws IdNotFoundException {
    System.out.println("CustomerAdminBean.findCustomerById(" + id + ")");
    Customer customer = customerDao.findById(id);
    if (customer == null) {
      throw new IdNotFoundException();
    }
    return customer;
  }

  @Override
  public List<Customer> findAllCustomers() {
    System.out.println("CustomerAdminBean.findAllCustomers()");
    return customerDao.findAll();
  }

}
