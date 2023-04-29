import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    private List<String[]> productsClientLog = new ArrayList<>();

    public ClientLog() {
        productsClientLog.add(new String[]{"productNum", "amount"});
    }

    public void log(int productNum, int amount) {

        productsClientLog.add(new String[]{String.valueOf(productNum), String.valueOf(amount)});

    }

    public void exportAsCSV(File txtFile) throws IOException {

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(txtFile, true))) {
            csvWriter.writeAll(productsClientLog);
        }
    }

}