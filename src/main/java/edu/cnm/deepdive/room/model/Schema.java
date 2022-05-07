package edu.cnm.deepdive.room.model;

import com.google.gson.annotations.Expose;
import java.util.stream.Stream;

public class Schema implements Streamable {

  @Expose
  private Database database;

  public Database getDatabase() {
    return database;
  }

  public void setDatabase(Database database) {
    this.database = database;
  }

  @Override
  public Stream<String> stream() {
    return database.stream();
  }

}
