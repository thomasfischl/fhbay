package sve2.fhbay.beans.dao;

import javax.ejb.Stateless;

import sve2.fhbay.domain.Category;
import sve2.fhbay.interfaces.dao.CategoryDao;

@Stateless
public class CategoryDaoBean extends AbstractDaoBean<Category, Long> implements CategoryDao {

}
