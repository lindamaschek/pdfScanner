package com.scanner;

import java.io.IOException;
import java.util.List;

public interface PdfCheck {

  List<String> checkForBlacklistedIban(String extractedText) throws IOException;

  // more validations possible ...

}
