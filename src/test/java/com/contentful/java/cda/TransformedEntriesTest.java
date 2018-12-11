package com.contentful.java.cda;

import com.contentful.java.cda.TransformQuery.ContentfulEntryModel;
import com.contentful.java.cda.TransformQuery.ContentfulField;
import com.contentful.java.cda.TransformQuery.ContentfulSystemField;
import com.contentful.java.cda.lib.Enqueue;

import org.junit.Test;

import java.util.Collection;

import static com.google.common.truth.Truth.assertThat;

public class TransformedEntriesTest extends BaseTest {
  @ContentfulEntryModel("cat")
  public static class Cat {
    @ContentfulField
    String name;

    @ContentfulField("bestFriend")
    Cat mate;

    @ContentfulSystemField
    String id;

    @ContentfulSystemField("revision")
    Object contentfulVersion;
  }

  @ContentfulEntryModel("cat")
  public static class RenamedCat {
    @ContentfulField("name")
    String m_pcName;
  }

  @Test
  @Enqueue("demo/entries_nyancat.json")
  public void fetchNyanCat() {
    final Cat nyancat = client
        .observeAndTransform(Cat.class)
        .one("nyancat")
        .blockingFirst();

    assertThat(nyancat).isNotNull();
    assertThat(nyancat.name).isEqualTo("Nyan Cat");
  }

  @Test
  @Enqueue("demo/entries.json")
  public void fetchCats() {
    final Collection<Cat> cats = client
        .observeAndTransform(Cat.class)
        .all()
        .blockingFirst();

    for (final Cat cat : cats) {
      assertThat(cat).isNotNull();
      assertThat(cat.name).isNotEmpty();
    }
  }

  @Test
  @Enqueue("demo/entries_nyancat.json")
  public void canRenameFields() {
    final RenamedCat nyancat = client
        .observeAndTransform(RenamedCat.class)
        .one("nyancat")
        .blockingFirst();

    assertThat(nyancat).isNotNull();
    assertThat(nyancat.m_pcName).isEqualTo("Nyan Cat");
  }
}
