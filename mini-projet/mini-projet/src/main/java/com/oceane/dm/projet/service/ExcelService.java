package com.oceane.dm.projet.service;

import com.oceane.dm.models.model.TwoFactorUser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public byte[] generateExcel(List<TwoFactorUser> users) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Utilisateurs");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Id");
        headerRow.createCell(1).setCellValue("Nom");
        headerRow.createCell(2).setCellValue("prenom");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Role");

        int rowNum = 1;
        for (TwoFactorUser user : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getLastName());
            row.createCell(2).setCellValue(user.getFirstName());
            row.createCell(3).setCellValue(user.getEmail());
            row.createCell(4).setCellValue(user.getRole().toString());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}

