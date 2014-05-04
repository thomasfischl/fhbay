package sve2.fhbay.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.base.Objects;

@Entity
public class Bid {

  @Id
  @GeneratedValue
  private Long id;

  private Double amount;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Customer bidder;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private Article article;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  public Bid() {
  }

  public Bid(Double amount, Customer bidder, Article article, Date createdDate) {
    super();
    this.amount = amount;
    this.bidder = bidder;
    this.article = article;
    this.createdDate = createdDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Customer getBidder() {
    return bidder;
  }

  public void setBidder(Customer bidder) {
    this.bidder = bidder;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("id", id).add("amount", amount).add("bidder", bidder).add("article", article).add("createdDate", createdDate)
        .toString();
  }

}
