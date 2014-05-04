package sve2.fhbay.beans.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import sve2.fhbay.domain.Category;
import sve2.fhbay.interfaces.dao.CategoryDao;

@Stateless
public class CategoryDaoBean extends AbstractDaoBean<Category, Long> implements CategoryDao {

  @Override
  public List<Category> findAllRootCategories() {
    EntityManager em = getEntityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Category> query = cb.createQuery(Category.class);
    Root<Category> root = query.from(Category.class);
    query.where(root.get("parent").isNull());
    TypedQuery<Category> q = em.createQuery(query);
    return q.getResultList();
  }

  @Override
  public void remove(Category entity) {
    if (entity.getParent() != null) {
      entity.getParent().removeSubCategory(entity);
    }

    super.remove(entity);
  }

}
