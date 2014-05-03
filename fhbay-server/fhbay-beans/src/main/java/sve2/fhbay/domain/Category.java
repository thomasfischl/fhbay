package sve2.fhbay.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Category implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn
  private Set<Category> subcategories = new HashSet<>();

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
  private Category parent;

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
