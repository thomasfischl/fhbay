package sve2.fhbay.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class CreditCard extends PaymentData {
  private static final long serialVersionUID = 1L;

  private String number;
  private Date expirationDate;

  public CreditCard() {
  }

  public CreditCard(String owner, String number, Date expirationDate) {
    super(owner);
    this.number = number;
    this.expirationDate = expirationDate;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Date getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }

  @Override
  public String toString() {
    return String.format("CreditCard: owner=%s, number=%s, expirationDate=%tm/%<tY", getOwner(), number, expirationDate);
  }
}
