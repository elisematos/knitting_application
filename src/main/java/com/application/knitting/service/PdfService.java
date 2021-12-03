package com.application.knitting.service;

import com.application.knitting.model.Material;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PdfService {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 28,
            Font.BOLD);
    private static final Font SUB_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);
    private static final Font SMALL_NORMAL = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);

    public void makeDocument(String fileName,
                             String title,
                             String description,
                             Integer stitches,
                             Integer rows,
                             String instructions,
                             List<Material> materialList,
                             String yarn
    )
            throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("assets/pdf/" + fileName + ".pdf"));
        document.open();
        addMetaData(document);
        addTitlePage(document, title);
        addContent(document, description);
        addMaterialList(document, materialList);
        addTensionSection(document, stitches, rows, yarn);
        addInstructions(document, instructions);
        document.close();
    }

    private static void addMetaData(Document document) {
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Elise MATOS");
        document.addCreator("Elilse MATOS");
        document.addCreationDate();
    }

    private static void addTitlePage(Document document, String title) throws DocumentException {
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph(title, TITLE_FONT));
        addEmptyLine(preface, 1);
        document.add(preface);
        document.add(new LineSeparator(0.5f, 100, null, 0, -5));
    }

    private static void addContent(Document document, String description) throws DocumentException {
        Paragraph content = new Paragraph();
        addEmptyLine(content, 2);
        content.add(new Paragraph(description, SUB_FONT));
        addEmptyLine(content, 2);
        document.add(content);
    }

    private static void addMaterialList(Document document, List<Material> materialList) throws DocumentException {
        Paragraph materials = new Paragraph();
        materials.add(new Paragraph("Fournitures", SUB_FONT));
        addEmptyLine(materials, 1);
        com.itextpdf.text.List list = new com.itextpdf.text.List();
        list.setListSymbol("- ");
        materialList.forEach(m -> list.add(new ListItem(m.getName(), SMALL_NORMAL)));
        materials.add(list);
        addEmptyLine(materials, 2);
        document.add(materials);
    }

    private static void addTensionSection(Document document, Integer stitches, Integer rows, String yarn)
            throws DocumentException {
        Paragraph tension = new Paragraph();
        tension.add(new Paragraph("Echantillon", SUB_FONT));
        addEmptyLine(tension, 1);
        tension.add(new Paragraph("Carré de 10 cm = " + stitches + " mailles en largeur et " + rows +
                " rangs en hauteur avec " + yarn + " ou laine de catégorie équivalente.", SMALL_NORMAL));
        addEmptyLine(tension, 2);
        document.add(tension);
    }

    private void addInstructions(Document document, String instructions) throws DocumentException {
        Paragraph instruction = new Paragraph();
        instruction.add(new Paragraph("Instructions", SUB_FONT));
        addEmptyLine(instruction, 1);
        instruction.add(new Paragraph(instructions, SMALL_NORMAL));
        addEmptyLine(instruction, 2);
        document.add(instruction);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
