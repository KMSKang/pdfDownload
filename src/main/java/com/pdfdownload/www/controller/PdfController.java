package com.pdfdownload.www.controller;

import com.lowagie.text.DocumentException;
import com.pdfdownload.www.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Controller
public class PdfController {

    private final PdfService service;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("rows", service.getList());
        return "index";
    }

    /**
     * 다운로드
     */
    @GetMapping("/download")
    public void downloadPDFResource(HttpServletResponse response) {
        try {
            Path file = Paths.get(service.generatePdf().getAbsolutePath());
            if (Files.exists(file)) {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition","attachment; filename=" + file.getFileName());
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } catch (DocumentException | IOException ex) {
            ex.printStackTrace();
        }
    }
}
