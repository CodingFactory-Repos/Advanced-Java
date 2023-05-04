package me.loule.hipopothalous.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import me.loule.hipopothalous.model.Accounting;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.ArrayList;

public class PdfConverter {

    private PdfConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static void createPdf() throws FileNotFoundException {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\lucas\\OneDrive\\Bureau\\PDF\\test.pdf"));
            document.open();

            //Create a table in the pdf
            PdfPTable table = new PdfPTable(3);
            table.addCell("Type");
            table.addCell("Money");
            table.addCell("Date");
            List<Accounting> accounting = Accounting.getAllAccounting();
            accounting.stream().sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate())).forEach(accounting1 -> {
                table.addCell(accounting1.getType());
                if(Objects.equals(accounting1.getType(), "Spent"))
                    table.addCell(new Paragraph("- " +  accounting1.getMoney().toString() + " €", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED)));
                else
                    table.addCell(new Paragraph("+ " + accounting1.getMoney().toString() + " €", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.GREEN)));
                table.addCell(accounting1.getDate().toString());
            });

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            Logger.getLogger(e.getMessage());
        }
    }
}
