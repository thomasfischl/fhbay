package sve2.fhbay.web.rest;

import sve2.fhbay.domain.Article;

public class ArticleDTO {

  private Long id;

  public ArticleDTO(Article article) {
    id = article.getId();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
