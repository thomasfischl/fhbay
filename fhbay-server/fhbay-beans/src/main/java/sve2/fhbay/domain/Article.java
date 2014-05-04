package sve2.fhbay.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
    @NamedQuery(name = "queryFindByPattern", query = "select distinct a from Article a where lower(a.name) like :pattern or lower(a.description) like :pattern"),
    @NamedQuery(name = "queryFindByPatternAndCategory", query = "select distinct a from Article a where (lower(a.name) like :pattern or lower(a.description) like :pattern) and :category member of a.categories") })
public class Article implements Serializable {

  private static final long serialVersionUID = -1L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(length = 300)
  private String description;

  @ManyToOne(fetch = FetchType.EAGER)
  private Customer seller;

  @ManyToOne(fetch = FetchType.EAGER)
  private Customer buyer;

  private double initialPrice;
  private double finalPrice;

  @Temporal(TemporalType.TIMESTAMP)
  private Date startDate;

  @Temporal(TemporalType.TIMESTAMP)
  private Date endDate;

  @Enumerated(EnumType.STRING)
  private ArticleState articleState = ArticleState.OFFERED;

  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  private Set<Category> categories = new HashSet<>();

  public Article() {
  }

  public Article(String name, String description, double initialPrice, Date startDate, Date endDate) {
    this.name = name;
    this.description = description;
    this.initialPrice = initialPrice;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Article(String name, String description, double initialPrice, Date startDate, Date endDate, Category category) {
    this(name, description, initialPrice, startDate, endDate);
    categories.add(category);
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

  public Customer getSeller() {
    return seller;
  }

  public void setSeller(Customer seller) {
    this.seller = seller;
  }

  public Customer getBuyer() {
    return buyer;
  }

  public void setBuyer(Customer buyer) {
    this.buyer = buyer;
  }

  public double getInitialPrice() {
    return initialPrice;
  }

  public void setInitialPrice(double initialPrice) {
    this.initialPrice = initialPrice;
  }

  public double getFinalPrice() {
    return finalPrice;
  }

  public void setFinalPrice(double finalPrice) {
    this.finalPrice = finalPrice;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
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

  public Set<Category> getCategories() {
    return categories;
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }

  @Override
  public String toString() {
    return String.format("Article (%d): name=%s, initialPrice=%.2f, finalPrice=%.2f", getId(), getName(), getInitialPrice(), getFinalPrice());
  }
}
