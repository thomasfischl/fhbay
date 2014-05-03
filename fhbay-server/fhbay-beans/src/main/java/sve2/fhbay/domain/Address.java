package sve2.fhbay.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Address implements Serializable {
  private static final long serialVersionUID = -1L;

  @Id
  @GeneratedValue
  private Long id;
  private String zipCode;
  private String city;
  private String street;

  public Address() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Address(String zipCode, String city, String street) {
    this.zipCode = zipCode;
    this.city = city;
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  @Override
  public String toString() {
    return zipCode + " " + city + ", " + street;
  }
}
