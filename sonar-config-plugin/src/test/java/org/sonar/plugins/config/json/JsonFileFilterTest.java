package org.sonar.plugins.config.json;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class JsonFileFilterTest {

  @Test
  void build_wrapper_file_is_excluded() {
    JsonFileFilter filter = new JsonFileFilter();
    assertThat(filter.accept(inputFile("build_wrapper_output_directory/build-wrapper-dump.json"))).isFalse();
  }

  private DefaultInputFile inputFile(String file) {
    return new TestInputFileBuilder("test", file)
      .setCharset(StandardCharsets.UTF_8)
      .setLanguage("json")
      .setContents("foo")
      .build();
  }
}