package sve2.fhbay.beans.dao;

import javax.ejb.Stateless;

import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.dao.CustomerDao;

@Stateless
public class CustomerDaoBean extends AbstractDaoBean<Customer, Long> implements CustomerDao {

}
