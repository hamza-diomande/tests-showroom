package com.oceane.dm.document.service;

import com.oceane.dm.document.model.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Service
public class CsvService {

    public byte[] generateCsv(List<User> users) throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Id", "Nom", "Email"));
        for (User user : users) {
            csvPrinter.printRecord(user.getId(), user.getLastName(), user.getEmail());
        }
        csvPrinter.flush();
        return writer.toString().getBytes();
    }
}
