package com.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IbanBlacklistLoader {

  // JSON, is more configurable
  // Alternative: Database (best solution), Txt/CSV file
  private static final String FILE_PATH = "/blacklisted_ibans.json";

  public static List<String> loadBlacklistedIbans() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();


    try (InputStream inputStream = IbanBlacklistLoader.class.getResourceAsStream(FILE_PATH)) {
      if (inputStream == null) {
        throw new IOException("blacklisted_ibans File not found" + FILE_PATH);
      }

      Map<String, List<String>> map = objectMapper.readValue(inputStream, new TypeReference<>() {});
      return map.getOrDefault("blacklisted_ibans", List.of());
    }
  }
}
