package com.oceane.dm.projet.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.oceane.dm.models.model.TwoFactorUser;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfService {

    public byte[] generatePdf(List<TwoFactorUser> users) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable table = new PdfPTable(3);
        addTableHeader(table);
        for (TwoFactorUser user : users) {
            addRows(table, user);
        }

        document.add(table);
        document.close();
        return outputStream.toByteArray();
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Id", "Nom", "Email")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, TwoFactorUser user) {
        table.addCell(String.valueOf(user.getId()));
        table.addCell(user.getLastName());
        table.addCell(user.getEmail());
    }
}

