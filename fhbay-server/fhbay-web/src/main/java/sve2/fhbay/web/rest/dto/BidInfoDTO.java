package sve2.fhbay.web.rest.dto;

public class BidInfoDTO {

  private Double minPrice;

  private int numberOfBids;

  private String endDate;

  private String articleState;

  public BidInfoDTO(Double minPrice, int numberOfBids, String endDate, String articleState) {
    super();
    this.minPrice = minPrice;
    this.numberOfBids = numberOfBids;
    this.endDate = endDate;
    this.articleState = articleState;
  }

  public BidInfoDTO() {
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

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getArticleState() {
    return articleState;
  }

  public void setArticleState(String articleState) {
    this.articleState = articleState;
  }

}
