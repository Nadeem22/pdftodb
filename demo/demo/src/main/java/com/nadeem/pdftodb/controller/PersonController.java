package com.nadeem.pdftodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nadeem.pdftodb.model.Person;
import com.nadeem.pdftodb.service.PDFParsingService;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PDFParsingService pdfParsingService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPDF(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("The file is empty.");
        }

        if (!file.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body("The file is not a PDF.");
        }

        try {
            List<Person> persons = pdfParsingService.parsePDF(file.getInputStream());

            if (persons.isEmpty()) {
                return ResponseEntity.badRequest().body("No persons data found in the PDF.");
            }

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(persons.get(0).getId())
                    .toUri();

            return ResponseEntity.created(location).body(persons);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to parse the PDF file: " + e.getMessage());
        }
    }
}

