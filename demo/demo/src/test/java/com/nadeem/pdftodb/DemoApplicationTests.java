package com.nadeem.pdftodb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nadeem.pdftodb.service.PDFParsingService;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private PDFParsingService pdfParsingService;

    @Test
    void contextLoads() {
        assertNotNull(pdfParsingService);
    }

}
