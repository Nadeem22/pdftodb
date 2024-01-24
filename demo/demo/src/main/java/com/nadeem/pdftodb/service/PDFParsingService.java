package com.nadeem.pdftodb.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nadeem.pdftodb.model.Person;
import com.nadeem.pdftodb.repository.PersonRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PDFParsingService {

    // Autowire your repository
    @Autowired
    private PersonRepository personRepository;

    public List<Person> parsePDF(InputStream pdfInputStream) throws IOException {
        List<Person> people = new ArrayList<>();

        try (PDDocument document = PDDocument.load(pdfInputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // Modify lines with regex to insert commas at appropriate places
            String modifiedText = insertCommas(text);

            String[] lines = modifiedText.split("\\r?\\n");

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (isLinePartOfTable(line)) {
                    Person person = parseLineToPerson(line);
                    if (person != null) {
                        people.add(person);
                    }
                }
            }
        }
        personRepository.saveAll(people);
        return people;
    }

    private String insertCommas(String text) {
        // Example regex to identify columns and insert commas
        // This is a simplistic example and may need adjustment
        String regex = "(\\b\\w+\\b) (\\b\\w+\\b) (\\b(?:[A-Za-z]+\\s?){1,2}\\b) (\\b[A-Z]{2}\\b) (\\b\\w+\\b)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        StringBuilder modifiedText = new StringBuilder();
        while (matcher.find()) {
            // Construct the line with commas
            modifiedText.append(matcher.group(1)).append(",")
                         .append(matcher.group(2)).append(",")
                         .append(matcher.group(3)).append(",")
                         .append(matcher.group(4)).append(",")
                         .append(matcher.group(5)).append("\n");
        }
        return modifiedText.toString();
    }

    private boolean isLinePartOfTable(String line) {
        // This is a very naive check. In a real scenario, you would check for more complex patterns
        // and perhaps use regular expressions or a state machine if the structure is very complex.
        return line.matches("[A-Z].*"); // Starts with a capital letter
    }

    private Person parseLineToPerson(String line) {
        // Splitting the line based on commas
        String[] parts = line.split(",");

        // Ensure there are enough parts to map to our Person object
        if (parts.length >= 5) {
            return new Person(
                parts[0].trim(), // First Name
                parts[1].trim(), // Last Name
                parts[2].trim(), // City
                parts[3].trim(), // Region
                parts[4].trim()  // Profession
            );
        }

        return null;
    }

}

