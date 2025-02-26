package com.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlacklistedIbanCheck implements PdfCheck {


  /*
   * Idea: Extract the text in lines or pages to know exactly where to find the IBAN.
   * Add Check for correct IBAN formatting
   */
  public List<String> checkForBlacklistedIban(String extractedText) throws IOException {
    List<String> blacklist = new ArrayList<>();
    List<String> blacklistedIbans = IbanBlacklistLoader.loadBlacklistedIbans();

    for (String iban : blacklistedIbans) {
      if (extractedText.toLowerCase().contains(iban.toLowerCase())
          || extractedText.toLowerCase().contains(iban.toLowerCase().replace(" ", ""))) {
        blacklist.add(iban);
      }
    }


    return blacklist;
  }

}
