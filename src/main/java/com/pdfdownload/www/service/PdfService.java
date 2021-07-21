package com.pdfdownload.www.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.pdfdownload.www.dto.Dog;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFCreationListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PdfService {

    private static final String PDF_RESOURCES = "/static/";
    private final SpringTemplateEngine templateEngine;

    public File generatePdf() throws IOException, DocumentException {
        Context context = getContext(); // 매핑 값
        String html = loadAndFillTemplate(context); // html에 키 값 매핑
        return renderPdf(html);
    }

    private Context getContext() {
        Context context = new Context();
        context.setVariable("rows", getList());
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("index", context);
    }

    private File renderPdf(String html) throws IOException, DocumentException {
        File file = File.createTempFile("abc", ".pdf"); // students 파일명으로 pdf파일 생성
        OutputStream outputStream = new FileOutputStream(file); // 프로그램이 데이터를 보낼 때 (출력 스트림)
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20); // 문서의 dpi를 변경 (기본값)
        renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm()); // img, css, font 적용
        renderer.getFontResolver().addFont(new ClassPathResource(PDF_RESOURCES + "/font/malgun.ttf").getURL().toExternalForm(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.layout(); // ???
        renderer.createPDF(outputStream); // PDF 생성
        outputStream.close(); // 출력 스트림 닫힘
        file.deleteOnExit(); // JVM 종료할 때 파일을 삭제
        return file;
    }

    public List<Dog> getList() {
        List<Dog> rows = new ArrayList<>();
        for(int i=1; i<11; i++) {
            Dog dog = new Dog();
            dog.setId(i);
            dog.setImgPath("img/bichon.jpg");
            dog.setType("비숑");
            rows.add(dog);
        }
        return rows;
    }

}