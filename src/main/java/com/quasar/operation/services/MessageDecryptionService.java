package com.quasar.operation.services;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface MessageDecryptionService {

    String decryptMessage(@NotNull List<List<String>> messages);
}
