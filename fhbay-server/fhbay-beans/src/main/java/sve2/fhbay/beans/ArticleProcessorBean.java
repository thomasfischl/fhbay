package sve2.fhbay.beans;

import java.util.Date;
import java.util.Random;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import sve2.fhbay.domain.Article;
import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.ArticleAdminLocal;

@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/FhBayQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "reconnectAttempts", propertyValue = "-1") })
public class ArticleProcessorBean implements MessageListener {

  private Random rand = new Random();

  @EJB
  private ArticleAdminLocal articleAdminLocal;

  @Override
  public void onMessage(Message message) {
    System.out.println("Received Message");
    if (message instanceof MapMessage) {
      MapMessage msg = (MapMessage) message;

      try {
        Long sellerId = msg.getLong("sellerId");
        // TODO Long categoryId = msg.getLong("categoryId");
        Article article = messageToArticle(msg);

        System.out.println("Started processing of article " + article.getName());
        Thread.sleep(5000 + rand.nextInt(8000));

        articleAdminLocal.offerArticle(article, sellerId);

        System.out.println("Finished processing of article " + article.getName() + "(" + article.getId() + ")");
      } catch (JMSException | IdNotFoundException | InterruptedException e) {
        throw new EJBException(e);
      }
    } else {
      throw new EJBException("Invalid message type: " + message.getClass());
    }

  }

  private Article messageToArticle(MapMessage msg) throws JMSException {
    Article article = new Article();
    article.setName(msg.getString("name"));
    article.setDescription(msg.getString("description"));
    article.setStartDate(new Date(msg.getLong("startDate")));
    article.setEndDate(new Date(msg.getLong("endDate")));
    article.setInitialPrice(msg.getDouble("initialPrice"));
    return article;
  }

}
