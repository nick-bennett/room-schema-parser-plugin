package edu.cnm.deepdive.room.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Entity implements Streamable {

  private static final String PLACEHOLDER = "${TABLE_NAME}";

  @Expose
  @SerializedName("tableName")
  private String name;

  @Expose
  @SerializedName("createSql")
  private String ddl;

  @Expose
  private List<Index> indices = new LinkedList<>();

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

  public List<Index> getIndices() {
    return indices;
  }

  public void setIndices(List<Index> indices) {
    this.indices = indices;
  }

  @Override
  public Stream<String> stream() {
    return Stream
        .concat(
            Stream.of(ddl),
            indices
                .stream()
                .flatMap(Index::stream)
        )
        .map((sql) -> sql.replace(PLACEHOLDER, name));
  }

}
