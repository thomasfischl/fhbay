package sve2.fhbay.interfaces.dao;

import javax.ejb.Local;

import sve2.fhbay.domain.Category;

@Local
public interface CategoryDao extends Dao<Category, Long> {
}
