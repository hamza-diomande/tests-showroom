package com.oceane.dm.document.controller;

import com.itextpdf.text.DocumentException;
import com.oceane.dm.document.model.User;
import com.oceane.dm.document.service.CsvService;
import com.oceane.dm.document.service.ExcelService;
import com.oceane.dm.document.service.PdfService;
import com.oceane.dm.document.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/genDoc")
public class DocumentController {

    @Autowired
    private CsvService csvService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private UserService userService;

    @GetMapping("/csv")
    public ResponseEntity<byte[]> downloadCsv() throws IOException {
        List<User> users = userService.getAllUsers();
        byte[] csvContent = csvService.generateCsv(users);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all-users.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvContent);
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadExcel() throws IOException {
        List<User> users = userService.getAllUsers();
        byte[] excelContent = excelService.generateExcel(users);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all-users.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelContent);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf() throws DocumentException, IOException {
        List<User> users = userService.getAllUsers();
        byte[] pdfContent = pdfService.generatePdf(users);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all-users.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }
}
