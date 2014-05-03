package sve2.fhbay.beans;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import sve2.fhbay.domain.Article;
import sve2.fhbay.interfaces.AuctionLocal;
import sve2.fhbay.interfaces.AuctionRemote;
import sve2.fhbay.interfaces.dao.ArticleDao;

/**
 * Session Bean implementation class AuctionBean
 */
@Stateless
public class AuctionBean implements AuctionRemote, AuctionLocal {

  @Resource
  private TimerService timerService;

  @EJB
  private ArticleDao articleDao;

  @Override
  public void addFinishAuctionTimer(Date expirationDate, Long articleId) {
    timerService.createTimer(expirationDate, articleId);
  }

  @Timeout
  public void finishAuctionHandler(Timer timer) {
    Long articleId = (Long) timer.getInfo();
    Article article = articleDao.findById(articleId);
    if (article == null) {
      throw new EJBException("Article is delete in the meantime. id: " + articleId);
    }

    // TODO do something
    System.out.println("===> Auction for " + article.getName() + " finished.");
  }

}
