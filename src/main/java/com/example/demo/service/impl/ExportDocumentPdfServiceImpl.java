package com.example.demo.service.impl;

import com.example.demo.entity.Document;
import com.example.demo.entity.Element;
import com.example.demo.service.ExportDocumentPdfService;
import com.example.demo.util.AppList;
import com.example.demo.util.ElementTypeEnum;
import com.google.gson.Gson;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportDocumentPdfServiceImpl implements ExportDocumentPdfService {
    @Override
    public byte[] exportpdf(Document appDocument) throws IOException {
        int biggestPage = getBiggestPage(appDocument.getElements());
        if(biggestPage>=0){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter stamper = new PdfWriter(byteArrayOutputStream);

            PdfDocument pdfDoc = new PdfDocument(stamper);

            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc, PageSize.A4, false);
            Text docTitle = new Text(appDocument.getName());
            docTitle.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));
            docTitle.setFontSize(21);
            docTitle.setFontColor(Color.BLUE);

            Paragraph docTitleParagraph = new Paragraph(docTitle);
            docTitleParagraph.setTextAlignment(TextAlignment.CENTER);
            document.add(docTitleParagraph);

            for (int page = 0; page<= biggestPage; page++){
                List<Element> pageElements = getSortedPageElements(appDocument.getElements(), page);
                if(pageElements != null){
                    pageElements.stream()
                            .forEach(elt -> {
                                ElementTypeEnum elementType = ElementTypeEnum.valueOf(elt.getType());
                                if(elt != null && elt.getText() != null){
                                    IBlockElement element = null;
                                    switch (elementType){
                                        case TEXT:
                                            element = getBlockElementForText(elt);
                                            break;
                                        case LIST:
                                            element = getBlockElementForList(elt);
                                            break;
                                        case ATTACHMENT:
                                            Image img = getBlockElementForAttachment(elt);
                                            document.add(img);
                                        case BIG_TITLE:
                                            try {
                                                element = getBlockElementForBigTitle(elt);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case MEDIUM_TITLE:
                                            try {
                                                element = getBlockElementForMediumTitle(elt);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case SMALL_TITLE:
                                            try {
                                                element = getBlockElementForSmallTitle(elt);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case SOURCE_CODE:
                                            if(elt.getText() != null){
                                                String[] list = elt.getText().split("\n");
                                                for(int i = 0; i<list.length; i++){
                                                    String linePrefix = getLinePrefix(i, list.length);
                                                    String str = linePrefix + "   " + list[i];
                                                    try {
                                                        document.add(getBlockElementForSourceCode(str));
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        default: break;
                                    }
                                    if(element != null){
                                        document.add(element);
                                    }
                                }
                            });
                }
            }

            int n = pdfDoc.getNumberOfPages();
            for (int i = 1; i <= n; i++) {
                document.showTextAligned(new Paragraph(String.format("page %s of %s", i, n)),
                        559, 806, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
            }

            document.close();
            return byteArrayOutputStream.toByteArray();
        }

        return null;
    }

    private String getLinePrefix(int i, int length) {
        StringBuilder sb = new StringBuilder("");
        sb.append(i + 1);
        int from = String.valueOf(i).length();
        int to = String.valueOf(length).length();
        for(int j = from; j<=to; j++){
            sb.append(' ');
        }
        sb.append('|');
        return sb.toString();
    }

    private IBlockElement getBlockElementForSourceCode(String str) throws IOException {
        Text text = new Text(str);
        text.setFont(PdfFontFactory.createFont(FontConstants.COURIER_OBLIQUE));
        text.setBackgroundColor(Color.LIGHT_GRAY);
        text.setFontSize(10);
        Paragraph p= new Paragraph (text);
        p.setMarginTop(1);
        p.setMarginBottom(1);
        return p;
    }

    private Text getBlockElementTitle(Element elt) throws IOException {
        Text title = new Text(elt.getText());
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        title.setFont(font);
        return title;
    }

    private IBlockElement getBlockElementForSmallTitle(Element elt) throws IOException {
        Text title = getBlockElementTitle(elt);
        title.setFontSize(13);
        return new Paragraph(title);
    }

    private IBlockElement getBlockElementForMediumTitle(Element elt) throws IOException {
        Text title = getBlockElementTitle(elt);
        title.setFontSize(15);
        return new Paragraph(title);
    }

    private IBlockElement getBlockElementForBigTitle(Element elt) throws IOException {
        Text title = getBlockElementTitle(elt);
        title.setFontSize(18);
        return new Paragraph(title);
    }

    private Image getBlockElementForAttachment(Element elt) {
        if(elt.getAttachment() != null){
            ImageData data = ImageDataFactory.create(elt.getAttachment().getData());
            return new Image(data);
        }
        return null;
    }

    private IBlockElement getBlockElementForList(Element elt) {
        Gson g = new Gson();
        AppList appList = g.fromJson(elt.getText(), AppList.class);
        if(appList != null && appList.getItems() != null){
            com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List();
            appList.getItems()
                    .stream()
                    .forEach(item -> {
                        list.add(item);
                    });
            return list;
        }

        return null;
    }

    private IBlockElement getBlockElementForText(Element element){
        return new Paragraph (element.getText());
    }

    private int getBiggestPage(List<Element> elements){
        if(elements != null && elements.size() > 0){
            return elements.parallelStream()
                    .mapToInt(element -> element.getPage())
                    .max()
                    .orElse(-1);
        }

        return -1;
    }

    private List<Element> getSortedPageElements(List<Element> elements, int page){
        if(elements != null && elements.size() > 0){
            return elements.stream()
                    .filter(elt -> elt.getPage() == page)
                    .sorted(Comparator.comparingInt(Element::getRow))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
