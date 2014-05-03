package sve2.fhbay.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.SimpleCustomerAdminRemote;
import sve2.fhbay.interfaces.dao.SimpleCustomerDao;

@Stateless
// @Remote(SimpleCustomerAdminRemote.class)
public class SimpleCustomerAdminBean implements SimpleCustomerAdminRemote {

  @EJB
  private SimpleCustomerDao customerDao;

  @Override
  public Long saveCustomer(Customer customer) {
    System.out.println("SimpleCustomerAdminBean.saveCustomer(" + customer + ")");
    customerDao.persist(customer);
    return customer.getId();
  }

  @Override
  public Customer findCustomerById(Long id) {
    System.out.println("findCustomerById(" + id + ")");
    return customerDao.findById(id);
  }

  @Override
  public List<Customer> findAllCustomers() {
    System.out.println("findAllCustomers()");
    return customerDao.findAll();
  }

}
