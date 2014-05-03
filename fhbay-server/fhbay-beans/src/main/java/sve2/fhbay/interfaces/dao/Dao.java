package sve2.fhbay.interfaces.dao;

import java.io.Serializable;
import java.util.List;

public interface Dao<T, ID extends Serializable> {

  void persist(T entity);

  T merge(T entity);

  T findById(ID id);

  List<T> findAll();

}