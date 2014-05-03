package sve2.fhbay.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Phone implements Serializable {
  private static final long serialVersionUID = 1L;

  // private long id;
  // private String type; // variant 1
  private String countryCode;
  private String number;

  public Phone() {
  }

  // variant 1
  // public Phone(String type, String countryCode, String number) {
  // this.type = type;
  // this.countryCode = countryCode;
  // this.number = number;
  // }

  // variant 2
  public Phone(String countryCode, String number) {
    this.countryCode = countryCode;
    this.number = number;
  }

  // public long getId() {
  // return id;
  // }
  //
  // public void setId(long id) {
  // this.id = id;
  // }

  // variant 1
  // public String getType() {
  // return type;
  // }
  //
  // public void setType(String type) {
  // this.type = type;
  // }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  @Override
  public String toString() {
    return countryCode + " " + number;
  }
}
