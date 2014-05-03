package sve2.fhbay.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.Category;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.ArticleAdminLocal;
import sve2.fhbay.interfaces.ArticleAdminRemote;
import sve2.fhbay.interfaces.AuctionLocal;
import sve2.fhbay.interfaces.dao.ArticleDao;
import sve2.fhbay.interfaces.dao.CustomerDao;

/**
 * Session Bean implementation class ArticleAdminBean
 */
@Stateless
public class ArticleAdminBean implements ArticleAdminRemote, ArticleAdminLocal {

  @EJB
  private ArticleDao articleDao;

  @EJB
  private CustomerDao customerDao;

  @EJB
  private AuctionLocal auctionBean;

  /**
   * Default constructor.
   */
  public ArticleAdminBean() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public Article findArticleById(Long id) throws IdNotFoundException {
    Article article = articleDao.findById(id);
    if (article == null) {
      throw new IdNotFoundException();
    }
    return article;
  }

  @Override
  public List<Article> findAllMatchingArticles(Long categoryId, String pattern, boolean includeSubCategories) throws IdNotFoundException {

    // TODO load category from db

    Category category = new Category();
    category.setId(categoryId);

    List<Article> articles = new ArrayList<>();
    articles.addAll(findAllMatchingArticles(category, pattern, includeSubCategories));
    return articles;
  }

  private Set<Article> findAllMatchingArticles(Category category, String pattern, boolean includeSubCategories) throws IdNotFoundException {
    Set<Article> matichingArticles = new HashSet<>(articleDao.findByPatternAndCategory(category.getId(), pattern));

    if (includeSubCategories) {
      for (Category sc : category.getSubcategories()) {
        matichingArticles.addAll(findAllMatchingArticles(sc, pattern, includeSubCategories));
      }
    }

    return matichingArticles;
  }

  @Override
  public Long offerArticle(Article article, Long sellerId) throws IdNotFoundException {

    Customer seller = customerDao.findById(sellerId);
    if (seller == null) {
      throw new IdNotFoundException();
    }

    article.setSeller(seller);
    articleDao.persist(article);

    //
    // TODO prepare auction for processing new article
    //
    auctionBean.addFinishAuctionTimer(article.getEndDate(), article.getId());

    return article.getId();
  }

}
