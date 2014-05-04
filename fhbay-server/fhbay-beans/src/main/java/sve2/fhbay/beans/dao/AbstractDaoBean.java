package sve2.fhbay.beans.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import sve2.fhbay.interfaces.dao.Dao;

public class AbstractDaoBean<T, ID extends Serializable> implements Dao<T, ID> {

  @PersistenceContext
  private EntityManager em;

  private Class<T> entityType;

  @SuppressWarnings("unchecked")
  public AbstractDaoBean() {
    ParameterizedType thisClass = (ParameterizedType) this.getClass().getGenericSuperclass();
    this.entityType = (Class<T>) thisClass.getActualTypeArguments()[0];
  }

  protected EntityManager getEntityManager() {
    return em;
  }

  public Class<T> getEntityBeanType() {
    return entityType;
  }

  @Override
  public void persist(T entity) {
    em.persist(entity);
  }

  @Override
  public T merge(T entity) {
    return em.merge(entity);
  }

  @Override
  public T findById(ID id) {
    return em.find(entityType, id);
  }

  @Override
  public List<T> findAll() {
    TypedQuery<T> qry = em.createQuery(String.format("select entity from %s entity", entityType.getName()), entityType);
    return qry.getResultList();
  }

  @Override
  public void remove(T entity) {
    em.remove(entity);
  }
  
  @Override
  public void flush() {
    em.flush();
  }

}
