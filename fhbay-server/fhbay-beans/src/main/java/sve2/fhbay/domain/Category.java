package sve2.fhbay.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.google.common.base.Preconditions;

@Entity
public class Category implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false)
  private Set<Category> subcategories = new HashSet<>();

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false, cascade = CascadeType.ALL)
  private Category parent;

  public Category() {
  }

  public Category(String name, Category parent) {
    this.name = name;
    parent.addSubCategory(this);
  }

  public Category(String name) {
    this.name = name;
  }

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Category getParent() {
    return parent;
  }

  public void setParent(Category parent) {
    this.parent = parent;
  }

  public void addSubCategory(Category subcategory) {
    Preconditions.checkNotNull(subcategory, "subcategory can't be null");
    if (subcategory.getParent() != null) {
      subcategory.getParent().getSubcategories().remove(subcategory);
    }
    subcategory.setParent(this);
    subcategories.add(subcategory);
  }

  public void removeSubCategory(Category subcategory) {
    Preconditions.checkNotNull(subcategory, "subcategory can't be null");
    subcategory.setParent(null);
    subcategories.remove(subcategory);
  }

}
