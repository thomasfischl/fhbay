package sve2.fhbay.beans.dao;

import javax.ejb.Stateless;

import sve2.fhbay.domain.Bid;
import sve2.fhbay.interfaces.dao.BidDao;

@Stateless
public class BidDaoBean extends AbstractDaoBean<Bid, Long> implements BidDao {

}
