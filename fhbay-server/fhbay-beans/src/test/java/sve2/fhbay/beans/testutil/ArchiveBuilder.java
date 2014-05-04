package sve2.fhbay.beans.testutil;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class ArchiveBuilder {

  public static Archive<?> createJavaArchive(String packageName) {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class).addPackages(true, packageName).addAsResource("META-INF/persistence.xml")
        .addAsResource("META-INF/ejb-jar.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

    // add guava library to test package
    Archive<?> guaveDep = ShrinkWrap.createFromZipFile(JavaArchive.class, Maven.resolver().resolve("com.google.guava:guava:16.0").withoutTransitivity()
        .asSingleFile());

    archive = archive.merge(guaveDep);

    // System.out.println(archive.toString(true));
    return archive;
  }

}
