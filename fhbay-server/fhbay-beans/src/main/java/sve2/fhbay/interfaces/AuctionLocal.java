package sve2.fhbay.interfaces;

import java.util.Date;

import javax.ejb.Local;

@Local
public interface AuctionLocal extends Auction {

  void addFinishAuctionTimer(Date expirationDate, Long articleId);

}
