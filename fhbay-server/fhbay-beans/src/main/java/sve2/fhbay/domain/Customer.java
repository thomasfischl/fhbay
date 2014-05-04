package sve2.fhbay.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;

@Entity
public class Customer implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;
  private String firstname;

  @Column(nullable = false)
  private String lastname;

  @Column(nullable = false)
  private String username;
  private String password;
  private String email;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private Address billingAddress;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @OrderColumn
  private List<Address> shippingAddresses = new ArrayList<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "PHONES")
  @MapKeyColumn
  private Map<String, Phone> phones = new HashMap<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private Set<PaymentData> paymentData = new HashSet<>();

  public Customer() {
  }

  public Customer(String firstname, String lastname, String username, String password, String email) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

  public List<Address> getShippingAddresses() {
    return shippingAddresses;
  }

  public void setShippingAddresses(List<Address> shippingAddresses) {
    this.shippingAddresses = shippingAddresses;
  }

  public void addShippingAddress(Address shippingAddress) {
    this.shippingAddresses.add(shippingAddress);
  }

  public Map<String, Phone> getPhones() {
    return phones;
  }

  public void setPhones(Map<String, Phone> phones) {
    this.phones = phones;
  }

  public void addPhone(String type, Phone phone) {
    this.phones.put(type, phone);
  }

  public Set<PaymentData> getPaymentData() {
    return paymentData;
  }

  public void setPaymentData(Set<PaymentData> paymentData) {
    this.paymentData = paymentData;
  }

  public void addPaymentData(PaymentData paymentData) {
    this.paymentData.add(paymentData);
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(String.format("%s, %s (%s), mailto:%s", lastname, firstname, username, email));

    if (billingAddress != null) {
      sb.append("; billingAddress: " + billingAddress);
    }

    return sb.toString();
  }

}
