package sve2.fhbay.interfaces.dto;

import java.util.Date;

import sve2.fhbay.domain.ArticleState;

import com.google.common.base.Objects;

public class BidInfo {

  private Double minPrice;

  private int numberOfBids;

  private Date endDate;

  private ArticleState articleState;

  public BidInfo(Double minPrice, int numberOfBids, Date endDate, ArticleState articleState) {
    super();
    this.minPrice = minPrice;
    this.numberOfBids = numberOfBids;
    this.endDate = endDate;
    this.articleState = articleState;
  }

  public Double getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(Double minPrice) {
    this.minPrice = minPrice;
  }

  public int getNumberOfBids() {
    return numberOfBids;
  }

  public void setNumberOfBids(int numberOfBids) {
    this.numberOfBids = numberOfBids;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public ArticleState getArticleState() {
    return articleState;
  }

  public void setArticleState(ArticleState articleState) {
    this.articleState = articleState;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("minPrice", minPrice).add("numberOfBids", numberOfBids).add("endDate", endDate).add("articleState", articleState)
        .toString();
  }

}
