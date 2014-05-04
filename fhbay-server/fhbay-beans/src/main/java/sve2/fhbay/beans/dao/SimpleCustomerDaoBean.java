package sve2.fhbay.beans.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.dao.SimpleCustomerDao;
import sve2.util.JdbcUtil;

@Stateless
public class SimpleCustomerDaoBean implements SimpleCustomerDao {

  private DataSource dataSource;

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void persist(Customer customer) {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      // version 1 (comment out for version 2)
      // DataSource dataSource = getDataSource();
      conn = getDataSource().getConnection();
      stmt = conn.prepareStatement("INSERT INTO SimpleCustomer (firstname, lastname, username, password, email) VALUES (?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS);
      stmt.setString(1, customer.getFirstname());
      stmt.setString(2, customer.getLastname());
      stmt.setString(3, customer.getUsername());
      stmt.setString(4, customer.getPassword());
      stmt.setString(5, customer.getEmail());

      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected != 1) {
        throw new EJBException("Insert into table SimpleCustomer failed.");
      }

      customer.setId(JdbcUtil.<Long> getUniqueKey(stmt));
    } catch (SQLException e) {
      throw new EJBException(e);
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        throw new EJBException(e);
      }
    }
  }

  @Override
  public Customer findById(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Customer> findAll() {
    // TODO Auto-generated method stub
    return null;
  }

}
