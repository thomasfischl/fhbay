package sve2.fhbay.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Phone implements Serializable {
  private static final long serialVersionUID = 1L;

  private String countryCode;

  private String number;

  public Phone() {
  }

  public Phone(String countryCode, String number) {
    this.countryCode = countryCode;
    this.number = number;
  }

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
