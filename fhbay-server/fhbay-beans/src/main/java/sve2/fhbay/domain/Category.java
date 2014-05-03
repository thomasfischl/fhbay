package sve2.fhbay.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Category implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private Set<Category> subcategories = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<Category> getSubcategories() {
    return subcategories;
  }

  public void setSubcategories(Set<Category> subcategories) {
    this.subcategories = subcategories;
  }
}
