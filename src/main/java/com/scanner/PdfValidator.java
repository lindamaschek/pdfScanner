package com.scanner;

import java.io.IOException;
import java.util.List;

// class to add more validators
public class PdfValidator {
  private final List<PdfCheck> pdfChecks;

  public PdfValidator(List<PdfCheck> pdfChecks) {
    this.pdfChecks = pdfChecks;
  }

  public boolean isValid(String extractedText) throws IOException {
    for (PdfCheck check : pdfChecks) {
      if (check.checkForBlacklistedIban(extractedText).isEmpty()) {
        return false;
      }
    }
    return true;
  }
}
