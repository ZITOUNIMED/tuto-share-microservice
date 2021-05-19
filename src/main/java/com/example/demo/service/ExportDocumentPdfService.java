package com.example.demo.service;

import com.example.demo.entity.Document;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ExportDocumentPdfService {
    byte[] exportpdf(Document appDocument) throws IOException;
}