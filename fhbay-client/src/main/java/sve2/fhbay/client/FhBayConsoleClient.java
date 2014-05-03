package sve2.fhbay.client;

import java.util.Collection;
import java.util.Date;
import java.util.Map.Entry;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;

import sve2.fhbay.domain.Address;
import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.CreditCard;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.domain.PaymentData;
import sve2.fhbay.domain.Phone;
import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.ArticleAdminRemote;
import sve2.fhbay.interfaces.CustomerAdminRemote;
import sve2.util.DateUtil;
import sve2.util.JndiUtil;
import sve2.util.LoggingUtil;

public class FhBayConsoleClient {

  private static CustomerAdminRemote custAdmin;
  private static ArticleAdminRemote artAdmin;

  private static void testArticleAdmin() throws NamingException {
    Customer[] customers = custAdmin.findAllCustomers().toArray(new Customer[] {});
    if (customers.length <= 1) {
      return;
    }
    Long cust1Id = customers[0].getId();
    Long cust2Id = customers[1].getId();

    System.out.println("------------- saveArticle ----------------------");

    try {
      Date now = DateUtil.now();
      Article art1 = new Article("DDR2 ECC", "Neuwertiger Speicherbaustein", 100, now, DateUtil.addSeconds(now, 3));
      System.out.println("Article: " + art1.getId());
      Long art1Id = artAdmin.offerArticle(art1, cust1Id);

      Article art2 = new Article("Samsung T166", "Samsung Spinpoint T166, 500GB, SATA", 150.99, now, DateUtil.addSeconds(now, 4));
      artAdmin.offerArticle(art2, cust2Id);

      Article art3 = new Article("OCZ Agility 2 T166", "SSD mit neuartiger Flash-Technologie", 768.99, now, DateUtil.addSeconds(now, 5));
      artAdmin.offerArticle(art3, cust2Id);

      System.out.println("------------- findArticleById ------------------");
      System.out.printf("art1=%s\n", artAdmin.findArticleById(art1Id));

      System.out.println("------------- findAllMatchingArticles ----------");
      System.out.println("Articles matching \"neu\"");
      Collection<Article> matchingArticles = artAdmin.findAllMatchingArticles(null, "neu", true);
      if ((matchingArticles != null) && !matchingArticles.isEmpty()) {
        for (Article art : matchingArticles) {
          System.out.printf("%s - %s%n", art, art.getDescription());
        }
      } else {
        System.out.println("No matching artilces found.");
      }
    } catch (IdNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static void testCustomerAdmin() {
    try {
      System.out.println("--------------- save customer ---------------");
      Customer cust1 = new Customer("Jaquira", "Hummelbrunner", "jaqui", "pwd", "Jaquira.Hummelbrunner@fh-hagenberg.at");
      cust1.setBillingAddress(new Address("4232", "Hagenberg", "Hauptstraße 117"));
      cust1.addPhone("mobile", new Phone("+43", "(0) 555 333"));
      // cust1.addPhone(new Phone("mobile", "+43", "(0) 555 333"));
      cust1.addShippingAddress(new Address("5555", "Mostbusch", "Linzerstraße 15"));
      cust1.addShippingAddress(new Address("8050", "Königsbrunn", "Maisfeld 15"));
      cust1.addPaymentData(new CreditCard("Himmelbrunner", "010448812", DateUtil.getDate(2007, 07, 1)));

      Customer cust2 = new Customer("Maggi", "Weibold", "maggi", "wei", "Maggi.Weibold@fh-hagenberg.at");
      cust2.setBillingAddress(new Address("4020", "Linz", "Hauptstra�e 117"));

      System.out.println("--------------- saveOrUpdateCustomer ---------------");

      Long cust1Id = custAdmin.saveCustomer(cust1);
      @SuppressWarnings("unused")
      Long cust2Id = custAdmin.saveCustomer(cust2);

      System.out.println("--------------- addShippingAddress ---------------");
      cust1 = custAdmin.findCustomerById(cust1Id);
      cust1.setLastname("Hummelbrunner-Huber");
      cust1.addShippingAddress(new Address("1000", "Wien", "Haudumgasse 87a"));
      cust1.addShippingAddress(new Address("5000", "Salzburg", "Moritzwinkel 5"));
      custAdmin.saveCustomer(cust1);

      System.out.println("--------------- findAllCustomers ---------------");
      for (Customer c : custAdmin.findAllCustomers()) {
        System.out.println(c);

        if (!c.getPhones().isEmpty()) {
          System.out.println("  phone numbers:");
          // for (Phone phone : c.getPhones())
          // System.out.println("     " + phone);
          for (Entry<String, Phone> entry : c.getPhones().entrySet()) {
            System.out.println("     " + entry.getKey() + ": " + entry.getValue());
          }
        }
        if (!c.getShippingAddresses().isEmpty()) {
          System.out.println("  shipping addresses:");
          for (Address a : c.getShippingAddresses()) {
            System.out.println("     " + a);
          }
        }
        if (!c.getPaymentData().isEmpty()) {
          System.out.println("  payment data:");
          for (PaymentData pd : c.getPaymentData()) {
            System.out.println("     " + pd);
          }
        }
      }

      System.out.println("--------------- findCustomerById ---------------");
      System.out.println(custAdmin.findCustomerById(cust1.getId()));
    } catch (IdNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static void articleToMessage(MapMessage msg, Article article) throws JMSException {
    msg.setString("name", article.getName());
    msg.setString("description", article.getDescription());
    msg.setDouble("initialPrice", article.getInitialPrice());
    msg.setLong("startDate", article.getStartDate().getTime());
    msg.setLong("endDate", article.getStartDate().getTime());
  }

  // add user (ApplicationRealm):
  // username=jboss,
  // password=fhbay-mdb1,
  // group=guest
  private static void testArticleProcessor() throws NamingException {
    System.out.println("=============== testArticleProcessor ===============");

    String username = JndiUtil.getProperty(Context.SECURITY_PRINCIPAL);
    String password = JndiUtil.getProperty(Context.SECURITY_CREDENTIALS);

    ConnectionFactory factory = null;
    Queue fhBayQueue = null;
    Connection con = null;
    Session session = null;

    try {
      Customer[] customers = custAdmin.findAllCustomers().toArray(new Customer[] {});
      if (customers.length == 0) {
        return;
      }
      Long sellerId = customers[0].getId();

      factory = JndiUtil.getRemoteObject("jms/RemoteConnectionFactory");
      fhBayQueue = JndiUtil.getRemoteObject("jms/queue/FhBayQueue");
      con = factory.createConnection(username, password);
      session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);

      MessageProducer producer = session.createProducer(fhBayQueue);

      Date now = DateUtil.now();

      for (int i = 0; i < 30; i++) {
        MapMessage message = session.createMapMessage();
        message.setLong("sellerId", sellerId);
        articleToMessage(message, new Article("A" + i, "", 10, now, DateUtil.addSeconds(now, 5 + i)));
        producer.send(message);

        System.out.println("Send " + message.getString("name") + " to broker");
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeResource(session);
      closeResource(con);
    }
  }

  private static void closeResource(Session session) {
    if (session != null) {
      try {
        session.close();
      } catch (JMSException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static void closeResource(Connection con) {
    if (con != null) {
      try {
        con.close();
      } catch (JMSException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void main(String[] args) {

    LoggingUtil.initJdkLogging("logging.properties");

    try {
      custAdmin = JndiUtil.getRemoteObject("ejb:fhbay/fhbay-beans/CustomerAdminBean!sve2.fhbay.interfaces.CustomerAdminRemote");
      artAdmin = JndiUtil.getRemoteObject("ejb:fhbay/fhbay-beans/ArticleAdminBean!sve2.fhbay.interfaces.ArticleAdminRemote");

      System.out.println("=============== testCustomerAdmin ==================");
      testCustomerAdmin();
      System.out.println("=============== testArticleAdmin ==================");
      testArticleAdmin();
      System.out.println("=============== testArticleAdmin ==================");
      testArticleProcessor();
    } catch (NamingException e) {
      e.printStackTrace();
    }

  }

}
