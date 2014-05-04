package sve2.fhbay.web.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.ArticleState;
import sve2.fhbay.interfaces.dto.BidInfo;
import sve2.fhbay.web.rest.dto.ArticleDTO;
import sve2.fhbay.web.rest.dto.BidInfoDTO;

public class Converter {

  private static SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");

  public static ArticleDTO mapToDto(Article article) {
    ArticleDTO dto = new ArticleDTO();

    dto.setArticleState(article.getArticleState().name());
    dto.setDescription(article.getDescription());
    dto.setEndDate(sdf.format(article.getEndDate()));
    dto.setId(article.getId());
    dto.setInitialPrice(article.getInitialPrice());
    dto.setName(article.getName());
    dto.setSellerId(article.getSeller().getId());
    dto.setStartDate(sdf.format(article.getStartDate()));

    return dto;
  }

  public static Article mapToDomain(ArticleDTO article) {
    Article dom = new Article();

    dom.setArticleState(ArticleState.valueOf(article.getArticleState()));
    dom.setDescription(article.getDescription());
    try {
      dom.setEndDate(sdf.parse(article.getEndDate()));
      dom.setStartDate(sdf.parse(article.getStartDate()));
    } catch (ParseException e) {
      throw new IllegalArgumentException(e);
    }
    dom.setId(article.getId());
    dom.setInitialPrice(article.getInitialPrice());
    dom.setName(article.getName());

    return dom;
  }

  public static BidInfoDTO mapToDto(BidInfo info) {
    BidInfoDTO dto = new BidInfoDTO();
    dto.setArticleState(info.getArticleState().name());
    dto.setEndDate(sdf.format(info.getEndDate()));
    dto.setMinPrice(info.getMinPrice());
    dto.setNumberOfBids(info.getNumberOfBids());
    return dto;
  }
}
