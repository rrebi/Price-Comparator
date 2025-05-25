package com.rebeca.price_comparator.repository;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.rebeca.price_comparator.model.Discount;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.InputStreamReader;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

@Repository
public class CsvDiscountRepository implements DiscountRepository {

    private final List<Discount> discounts = new ArrayList<>();

    public CsvDiscountRepository() {
        loadMatchingFiles("data", ".*_discounts_\\d{4}-\\d{2}-\\d{2}\\.csv");
    }

    private void loadMatchingFiles(String folder, String regexPattern) {
        try {
            Path dir = new ClassPathResource(folder).getFile().toPath();
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir);

            Pattern pattern = Pattern.compile(regexPattern);

            for (Path path : stream) {
                String fileName = path.getFileName().toString();
                if (pattern.matcher(fileName).matches()) {
                    loadCsv(folder + "/" + fileName);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading discount CSV files: " + e.getMessage());
        }
    }

    private void loadCsv(String resourcePath) {
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(new ClassPathResource(resourcePath).getInputStream()))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            String fileName = resourcePath.substring(resourcePath.lastIndexOf("/") + 1); // e.g. lidl_discounts_2025-05-25.csv
            String[] parts = fileName.replace(".csv", "").split("_");

            if (parts.length < 3) {
                System.err.println("Invalid discount file name format: " + fileName);
                return;
            }

            String store = parts[0];
            // DO NOT parse parts[1] ("discounts")

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 9) continue;

                String productId = line[0];
                // Skip prod, name, brand, quantity
                LocalDate from = LocalDate.parse(line[6]);
                LocalDate to = LocalDate.parse(line[7]);
                int percentage = Integer.parseInt(line[8]);

                discounts.add(new Discount(productId, store, from, to, percentage));
            }

        } catch (Exception e) {
            System.err.println("Error loading file: " + resourcePath + " -> " + e.getMessage());
        }
    }


    @Override
    public List<Discount> getAllDiscounts() {
        return discounts;
    }
}
