package sve2.fhbay.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
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
import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.Category;
import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.dao.ArticleDao;
import sve2.fhbay.interfaces.dao.CategoryDao;

@RunWith(Arquillian.class)
@PersistenceTest
public class ArticleDaoBeanTest {

  @EJB
  private ArticleDao dao;

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
    assertNotNull(dao);
    assertNotNull(em);
  }

  @Test
  public void testCRUD() {
    assertTrue(dao.findAll().isEmpty());
    Article a1 = new Article("Car", "Very useful description", 10.0, new Date(1), new Date(1));
    Article a2 = new Article("Very nice new computer", "...", 10.0, new Date(1), new Date(1));

    dao.persist(a1);
    dao.persist(a2);
    assertEquals(2, dao.findAll().size());
    assertNotNull(a1.getId());
    assertNotNull(a2.getId());

    assertEquals(a1.getName(), dao.findById(a1.getId()).getName());
    assertEquals(a2.getName(), dao.findById(a2.getId()).getName());

    dao.remove(a1);
    assertEquals(1, dao.findAll().size());
    assertNull(dao.findById(a1.getId()));
  }

  @Test
  public void testFindByPattern() throws IdNotFoundException {
    assertTrue(dao.findAll().isEmpty());
    Article a1 = new Article("Car", "Very useful description", 10.0, new Date(1), new Date(1));
    Article a2 = new Article("Very nice new computer", "...", 10.0, new Date(1), new Date(1));

    dao.persist(a1);
    dao.persist(a2);

    assertEquals(2, dao.findAll().size());
    assertNotNull(a1.getId());
    assertNotNull(a2.getId());

    assertEquals(2, dao.findByPatternAndCategory(null, "Very").size());
    assertEquals(2, dao.findByPatternAndCategory(null, "very").size());
    assertEquals(2, dao.findByPatternAndCategory(null, "VERY").size());
    assertEquals(0, dao.findByPatternAndCategory(null, "abc").size());
    assertEquals(1, dao.findByPatternAndCategory(null, "car").size());

  }

  @Test
  public void testFindByPatternAndCategory() throws IdNotFoundException {
    assertTrue(dao.findAll().isEmpty());

    Category cat = new Category("C1");
    Article a1 = new Article("Car", "Very useful description", 10.0, new Date(1), new Date(1), cat);
    Article a2 = new Article("Very nice new computer", "...", 10.0, new Date(1), new Date(1), cat);
    Article a3 = new Article("Very cool smartphone", "...", 10.0, new Date(1), new Date(1));

    categoryDao.persist(cat);
    dao.persist(a1);
    dao.persist(a2);
    dao.persist(a3);

    assertEquals(3, dao.findAll().size());
    assertNotNull(a1.getId());
    assertNotNull(a2.getId());
    assertNotNull(a3.getId());
    assertNotNull(cat.getId());

    assertEquals(3, dao.findByPatternAndCategory(null, "Very").size());
    assertEquals(3, dao.findByPatternAndCategory(null, "very").size());
    assertEquals(3, dao.findByPatternAndCategory(null, "VERY").size());
    assertEquals(0, dao.findByPatternAndCategory(null, "abc").size());
    assertEquals(1, dao.findByPatternAndCategory(null, "car").size());
    assertEquals(1, dao.findByPatternAndCategory(null, "smartphone").size());

    List<Article> list = dao.findByPatternAndCategory(cat.getId(), "Very");
    for (Article article : list) {
      System.out.println(article);
    }
    assertEquals(2, dao.findByPatternAndCategory(cat.getId(), "Very").size());
    assertEquals(2, dao.findByPatternAndCategory(cat.getId(), "very").size());
    assertEquals(2, dao.findByPatternAndCategory(cat.getId(), "VERY").size());
    assertEquals(0, dao.findByPatternAndCategory(cat.getId(), "abc").size());
    assertEquals(1, dao.findByPatternAndCategory(cat.getId(), "car").size());
    assertEquals(0, dao.findByPatternAndCategory(cat.getId(), "smartphone").size());

  }
}