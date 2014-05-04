package sve2.fhbay.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.ArticleState;
import sve2.fhbay.domain.Bid;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.AuctionLocal;
import sve2.fhbay.interfaces.AuctionRemote;
import sve2.fhbay.interfaces.dao.ArticleDao;
import sve2.fhbay.interfaces.dao.BidDao;
import sve2.fhbay.interfaces.dao.CustomerDao;
import sve2.fhbay.interfaces.dto.BidInfo;

import com.google.common.base.Preconditions;

/**
 * Session Bean implementation class AuctionBean
 */
@Stateless
public class AuctionBean implements AuctionRemote, AuctionLocal {

  @Resource
  private TimerService timerService;

  @EJB
  private ArticleDao articleDao;

  @EJB
  private BidDao bidDao;

  @EJB
  private CustomerDao customerDao;

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

    ArrayList<Bid> sorted = sortBids(article);

    switch (sorted.size()) {
    case 0:
      // No bids
      article.setArticleState(ArticleState.UNSALEABE);
      break;
    case 1:
      // Only one bid
      Bid highest = sorted.get(0);
      article.setSuccessfulBid(highest);
      article.setBuyer(highest.getBidder());
      article.setFinalPrice(article.getInitialPrice());
      article.setArticleState(ArticleState.SOLD);
      break;
    default:
      // More bids. Use the bid with the highest price
      highest = sorted.get(sorted.size() - 1);
      Bid second = sorted.get(sorted.size() - 2);
      article.setFinalPrice(second.getAmount() + 1);
      article.setBuyer(highest.getBidder());
      article.setSuccessfulBid(highest);
      article.setArticleState(ArticleState.SOLD);
    }

    articleDao.merge(article);
    System.out.println("===> Auction for " + article.getName() + " finished.");
  }

  @Override
  public void addBid(long articleId, double amount, long customerId) throws IdNotFoundException {
    Article article = articleDao.findById(articleId);
    Preconditions.checkNotNull(article);

    Customer customer = customerDao.findById(customerId);
    Preconditions.checkNotNull(customer);

    if (article.getArticleState() != ArticleState.OFFERED) {
      throw new IllegalStateException("Wrong article state: " + article.getArticleState());
    }

    if (article.getEndDate().before(new Date())) {
      throw new IllegalStateException("Auction already closed");
    }

    // calculate current price
    double currentPrice = calculateCurrentPrice(article);
    if (amount < currentPrice) {
      throw new IllegalStateException("Bid too low!");
    }

    Bid bid = new Bid();
    bid.setAmount(amount);
    bid.setArticle(article);
    bid.setBidder(customer);
    bid.setCreatedDate(new Date());

    bidDao.merge(bid);
    article.addBid(bid);
    articleDao.merge(article);
  }

  @Override
  public BidInfo getBidInfo(long articleId) {
    Article article = articleDao.findById(articleId);
    Preconditions.checkNotNull(article);
    Set<Bid> bids = article.getBids();
    return new BidInfo(calculateCurrentPrice(article), bids.size(), article.getEndDate(), article.getArticleState());
  }

  private double calculateCurrentPrice(Article article) {
    double currentPrice = article.getInitialPrice();
    List<Bid> bids = sortBids(article);
    if (!bids.isEmpty()) {
      currentPrice = Math.max(bids.get(bids.size() - 1).getAmount(), currentPrice);
    }
    return currentPrice;
  }

  private ArrayList<Bid> sortBids(Article article) {
    ArrayList<Bid> sorted = new ArrayList<Bid>(article.getBids());
    Collections.sort(sorted, new Comparator<Bid>() {
      @Override
      public int compare(Bid o1, Bid o2) {
        return o1.getAmount().compareTo(o2.getAmount());
      }
    });
    return sorted;
  }
}
