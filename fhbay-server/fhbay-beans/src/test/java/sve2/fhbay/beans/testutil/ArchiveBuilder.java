package sve2.fhbay.beans.testutil;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class ArchiveBuilder {

  public static Archive<?> createJavaArchive(String packageName) {
    Archive<?> archive = ShrinkWrap.create(JavaArchive.class).addPackages(true, packageName).addAsResource("META-INF/persistence.xml")
        .addAsResource("META-INF/ejb-jar.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    return archive;
  }

}
