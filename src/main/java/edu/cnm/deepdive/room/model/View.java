package edu.cnm.deepdive.room.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.stream.Stream;

public class View implements Streamable {

  private static final String PLACEHOLDER = "${VIEW_NAME}";

  @Expose
  @SerializedName("viewName")
  private String name;

  @Expose
  @SerializedName("createSql")
  private String ddl;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDdl() {
    return ddl;
  }

  public void setDdl(String ddl) {
    this.ddl = ddl;
  }

  @Override
  public Stream<String> stream() {
    return Stream
        .of(ddl)
        .map((sql) -> sql.replace(PLACEHOLDER, name));
  }

}
