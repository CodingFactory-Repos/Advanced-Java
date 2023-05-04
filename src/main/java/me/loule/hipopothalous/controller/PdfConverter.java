package me.loule.hipopothalous.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Logger;

public class PdfConverter {

    private PdfConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static void createPdf(Double spent, Double earning) throws FileNotFoundException {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\lucas\\OneDrive\\Bureau\\PDF\\test.pdf"));
            document.open();

            //Create a table in the pdf
            PdfPTable table = new PdfPTable(2);

            table.addCell("Spent");
            table.addCell("Earning");

            //Add data to the table
            table.addCell(new Paragraph("- " +  spent.toString() + " €", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED)));
            table.addCell(new Paragraph("+ " + earning.toString() + " €", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.GREEN)));
            //Add the table to the pdf

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            Logger.getLogger(e.getMessage());
        }
    }
}
