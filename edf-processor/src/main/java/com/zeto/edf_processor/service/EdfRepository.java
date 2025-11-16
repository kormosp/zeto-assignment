package com.zeto.edf_processor.service;

import com.zeto.edf_processor.model.EdfData;

import java.util.List;

public interface EdfRepository {

    void loadEdfs();

    List<EdfData> listEdfs();
}
