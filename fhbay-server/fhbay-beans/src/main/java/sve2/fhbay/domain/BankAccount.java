package sve2.fhbay.domain;

import javax.persistence.Entity;

@Entity
public class BankAccount extends PaymentData {
  private static final long serialVersionUID = 1L;

  private String accountNo;
  private String bank;

  public BankAccount() {
  }

  public BankAccount(String owner, String accountNo, String bank) {
    super(owner);
    this.accountNo = accountNo;
    this.bank = bank;
  }

  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }

  public String getBank() {
    return bank;
  }

  public void setBank(String bank) {
    this.bank = bank;
  }

  @Override
  public String toString() {
    return String.format("BankAccount: owner=%s, accountNo=%s, bank=%s", getOwner(), accountNo, bank);
  }

}
