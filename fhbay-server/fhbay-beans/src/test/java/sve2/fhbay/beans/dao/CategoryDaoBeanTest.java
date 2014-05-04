package sve2.fhbay.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import sve2.fhbay.beans.testutil.ArchiveBuilder;
import sve2.fhbay.domain.Category;
import sve2.fhbay.interfaces.dao.CategoryDao;

@RunWith(Arquillian.class)
@PersistenceTest
public class CategoryDaoBeanTest {

  @EJB
  private CategoryDao categoryDao;

  @PersistenceContext
  private EntityManager em;

  @Deployment
  public static Archive<?> createTestArchive() {
    return ArchiveBuilder.createJavaArchive("sve2.fhbay");
  }

  @Before
  public void setup() {
    assertNotNull(categoryDao);
    assertNotNull(em);
  }

  @Test
  public void testCRUD() {
    assertTrue(categoryDao.findAll().isEmpty());
    Category c1 = new Category("Elektronik");
    Category c2 = new Category("Computer", c1);
    Category c3 = new Category("Fehrnseher", c1);

    categoryDao.persist(c1);
    categoryDao.persist(c2);
    categoryDao.persist(c3);
    assertEquals(3, categoryDao.findAll().size());
    assertNotNull(c1.getId());
    assertNotNull(c2.getId());
    assertNotNull(c3.getId());
  }

  @Test
  public void testFindAllRootCategories() {
    assertTrue(categoryDao.findAll().isEmpty());
    Category c1 = new Category("Elektronik");
    Category c2 = new Category("Computer", c1);
    Category c3 = new Category("Fehrnseher", c1);

    categoryDao.persist(c2);
    categoryDao.persist(c3);
    assertEquals(3, categoryDao.findAll().size());
    categoryDao.flush();

    List<Category> roots = categoryDao.findAllRootCategories();
    assertEquals(1, roots.size());
    assertEquals(c1.getName(), roots.get(0).getName());

    assertEquals(2, roots.get(0).getSubcategories().size());
  }

  @Test
  public void testOrphanRemoval() {
    assertTrue(categoryDao.findAll().isEmpty());
    Category c1 = new Category("Elektronik");
    Category c2 = new Category("Computer", c1);
    Category c3 = new Category("Fehrnseher", c1);

    categoryDao.persist(c2);
    categoryDao.persist(c3);
    assertEquals(3, categoryDao.findAll().size());

    categoryDao.remove(c3);
    assertEquals(2, categoryDao.findAll().size());

    categoryDao.remove(c1);
    assertEquals(0, categoryDao.findAll().size());
  }

}