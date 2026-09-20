
package com.skillbridge.skillbridgeai.controller;

import com.skillbridge.skillbridgeai.service.PdfService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://worthy-radiance-production-eb6f.up.railway.app"
})
@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping(
            value = "/extract",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String extractText(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        return pdfService.extractText(file);
    }
}

