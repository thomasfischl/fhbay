package sve2.fhbay.beans.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import sve2.fhbay.domain.Article;
import sve2.fhbay.interfaces.dao.ArticleDao;

@Stateless
public class ArticleDaoBean extends AbstractDaoBean<Article, Long> implements ArticleDao {

  @Override
  public List<Article> findByPatternAndCategory(Long categoryId, String pattern) {
    // variant 1
    // String queryString =
    // "select distinct a from Article a where lower(a.name) like :pattern or lower(a.description) like :pattern";
    // TODO sql = ... inner join (a.categories) cat ... cat.id = categoryId

    // EntityManager em = getEntityManager();
    // TypedQuery<Article> query = em.createQuery(queryString, Article.class);
    // query.setParameter("pattern", "%" + pattern.toLowerCase() + "%");
    // return query.getResultList();

    // varient 2
    EntityManager em = getEntityManager();
    TypedQuery<Article> query = em.createNamedQuery("queryFindByPatternAndCategory", Article.class);
    query.setParameter("pattern", "%" + pattern.toLowerCase() + "%");
    return query.getResultList();
  }

}
