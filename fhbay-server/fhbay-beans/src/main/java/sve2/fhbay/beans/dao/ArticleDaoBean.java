package sve2.fhbay.beans.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.Category;
import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.dao.ArticleDao;
import sve2.fhbay.interfaces.dao.CategoryDao;

@Stateless
public class ArticleDaoBean extends AbstractDaoBean<Article, Long> implements ArticleDao {

  @EJB
  private CategoryDao categoryDao;

  @Override
  public List<Article> findByPatternAndCategory(Long categoryId, String pattern) throws IdNotFoundException {
    EntityManager em = getEntityManager();

    TypedQuery<Article> query;
    if (categoryId == null) {
      query = em.createNamedQuery("queryFindByPattern", Article.class);
      query.setParameter("pattern", "%" + pattern.toLowerCase() + "%");
    } else {
      Category category = categoryDao.findById(categoryId);
      if (category == null) {
        throw new IdNotFoundException();
      }

      query = em.createNamedQuery("queryFindByPatternAndCategory", Article.class);
      query.setParameter("pattern", "%" + pattern.toLowerCase() + "%");
      query.setParameter("category", category);
    }
    return query.getResultList();
  }
}
