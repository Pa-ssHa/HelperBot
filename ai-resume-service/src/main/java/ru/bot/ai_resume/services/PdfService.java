package ru.bot.ai_resume.services;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.pdfbox.Loader.loadPDF;

@Service
public class PdfService {

    public String parsePdf(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] pdfBytes = inputStream.readAllBytes();

            try (RandomAccessRead randomAccessRead = new RandomAccessReadBuffer(pdfBytes)) {
                try (PDDocument document = loadPDF(randomAccessRead)) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    return stripper.getText(document);
                }
            }
        }
    }
}