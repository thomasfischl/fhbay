package sve2.fhbay.web.rest;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.AuctionLocal;
import sve2.fhbay.interfaces.dto.BidInfo;
import sve2.fhbay.web.rest.dto.BidInfoDTO;

@Path("/bids")
public class BidRestService {

  @EJB
  private AuctionLocal auctionAdmin;

  @GET
  @Path("/{articleId}")
  @Produces(MediaType.APPLICATION_JSON)
  public BidInfoDTO getBidInfo(@PathParam("articleId") Long articleId) {
    try {
      BidInfo info = auctionAdmin.getBidInfo(articleId);
      return Converter.mapToDto(info);
    } catch (Exception e) {
      throw new WebApplicationException(Status.NOT_FOUND);
    }
  }

  @PUT
  @Path("/pushBid")
  public void pushBid(@QueryParam("articleId") long articleId, @QueryParam("amount") double amount, @QueryParam("customerId") long customerId) {
    try {
      auctionAdmin.addBid(articleId, amount, customerId);
    } catch (IdNotFoundException e) {
      throw new WebApplicationException(Status.NOT_FOUND);
    }
  }

}