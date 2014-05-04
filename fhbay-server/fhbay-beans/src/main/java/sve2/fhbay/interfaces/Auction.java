package sve2.fhbay.interfaces;

import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.dto.BidInfo;

public interface Auction {

  void addBid(long articleId, double amount, long customerId) throws IdNotFoundException;

  BidInfo getBidInfo(long articleId);
}
