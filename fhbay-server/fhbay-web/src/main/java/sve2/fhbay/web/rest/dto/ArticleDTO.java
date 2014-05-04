package sve2.fhbay.web.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArticleDTO {

  private Long id;

  private String name;

  private String description;

  private Long sellerId;

  private double initialPrice;

  private String startDate;

  private String endDate;

  private String articleState;

  public ArticleDTO(Long id, String name, String description, Long sellerId, double initialPrice, String startDate, String endDate, String articleState) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.sellerId = sellerId;
    this.initialPrice = initialPrice;
    this.startDate = startDate;
    this.endDate = endDate;
    this.articleState = articleState;
  }

  public ArticleDTO() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getSellerId() {
    return sellerId;
  }

  public void setSellerId(Long sellerId) {
    this.sellerId = sellerId;
  }

  public double getInitialPrice() {
    return initialPrice;
  }

  public void setInitialPrice(double initialPrice) {
    this.initialPrice = initialPrice;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
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
