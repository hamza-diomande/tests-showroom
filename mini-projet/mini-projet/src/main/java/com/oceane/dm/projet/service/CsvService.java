package com.oceane.dm.projet.service;


import com.oceane.dm.models.model.TwoFactorUser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Service
public class CsvService {

    public byte[] generateCsv(List<TwoFactorUser> users) throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Id", "Nom", "Email"));
        for (TwoFactorUser user : users) {
            csvPrinter.printRecord(user.getId(), user.getLastName(), user.getEmail());
        }
        csvPrinter.flush();
        return writer.toString().getBytes();
    }
}
