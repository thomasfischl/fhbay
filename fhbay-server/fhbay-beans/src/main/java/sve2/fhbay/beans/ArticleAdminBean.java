package sve2.fhbay.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.ArticleState;
import sve2.fhbay.domain.Category;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.ArticleAdminLocal;
import sve2.fhbay.interfaces.ArticleAdminRemote;
import sve2.fhbay.interfaces.AuctionLocal;
import sve2.fhbay.interfaces.dao.ArticleDao;
import sve2.fhbay.interfaces.dao.CategoryDao;
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

  @EJB
  private CategoryDao categoryDao;

  public ArticleAdminBean() {
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
    article.setStartDate(Calendar.getInstance().getTime());
    article.setArticleState(ArticleState.OFFERED);
    article = articleDao.merge(article);

    auctionBean.addFinishAuctionTimer(article.getEndDate(), article.getId());
    return article.getId();
  }

  @Override
  public Category findCategoryById(Long id) throws IdNotFoundException {
    Category category = categoryDao.findById(id);
    if (category == null) {
      throw new IdNotFoundException();
    }
    return category;
  }

  @Override
  public List<Category> findAllRootCategories() {
    return categoryDao.findAllRootCategories();
  }

  @Override
  public Category findCategoryTree(Long categoryId) throws IdNotFoundException {
    Category result = categoryDao.findById(categoryId);

    if (result == null) {
      throw new IdNotFoundException();
    }

    // TODO use jpql query for better performance!!
    while (result.getParent() != null) {
      result = categoryDao.findById(result.getParent().getId());
    }
    return result;
  }

  @Override
  public Long saveCategory(Category category) {
    category = categoryDao.merge(category);
    return category.getId();
  }

  @Override
  public void assignArticleToCategory(Long articleId, Long categoryId) throws IdNotFoundException {
    Category category = findCategoryById(categoryId);
    Article article = findArticleById(articleId);

    Set<Category> categories = article.getCategories();
    if (!categories.contains(category)) {
      categories.add(category);
      articleDao.merge(article);
    }
  }

}
