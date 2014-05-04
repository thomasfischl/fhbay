package sve2.fhbay.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import sve2.fhbay.beans.testutil.ArchiveBuilder;
import sve2.fhbay.domain.Article;
import sve2.fhbay.interfaces.dao.ArticleDao;

@RunWith(Arquillian.class)
@PersistenceTest
public class ArquillianPersistenceTest {

  @EJB
  private ArticleDao dao;

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
  public void testPersist1() {
    List<Article> list = dao.findAll();
    assertNotNull(list);
    assertEquals(0, list.size());

    em.persist(new Article("Lamp", "Useful description", 10.0, new Date(1), new Date(1)));

    list = dao.findAll();
    assertNotNull(list);
    assertEquals(1, list.size());
  }

  @Test
  public void testPersist2() {
    List<Article> list = dao.findAll();
    assertNotNull(list);
    assertEquals(0, list.size());

    em.persist(new Article("Lamp", "Useful description", 10.0, new Date(1), new Date(1)));

    list = dao.findAll();
    assertNotNull(list);
    assertEquals(1, list.size());
  }

  @Test(expected = TransactionRequiredException.class)
  @Transactional(TransactionMode.DISABLED)
  public void testTransactionRequiredForPersist() {
    em.persist(new Article());
  }
}