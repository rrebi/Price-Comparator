package com.rebeca.price_comparator.repository;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.rebeca.price_comparator.model.PriceEntry;
import com.rebeca.price_comparator.model.Product;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.InputStreamReader;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

@Repository
public class CsvProductPriceRepository implements ProductPriceRepository {

    private final Map<String, Product> productsById = new HashMap<>();
    private final List<PriceEntry> priceEntries = new ArrayList<>();

    public CsvProductPriceRepository() {
        loadMatchingFiles("data", "^[a-z]+_\\d{4}-\\d{2}-\\d{2}\\.csv$");
    }

    private void loadMatchingFiles(String folder, String regexPattern) {
        try {
            Path dir = new ClassPathResource(folder).getFile().toPath();
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir);

            Pattern pattern = Pattern.compile(regexPattern);

            for (Path path : stream) {
                String fileName = path.getFileName().toString();
                if (fileName.contains("discounts")) continue;
                if (pattern.matcher(fileName).matches()) {
                    loadCsv(folder + "/" + fileName);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading price CSV files: " + e.getMessage());
        }
    }

    private void loadCsv(String resourcePath) {
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(new ClassPathResource(resourcePath).getInputStream()))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            String fileName = resourcePath.substring(resourcePath.lastIndexOf("/") + 1);
            String[] parts = fileName.replace(".csv", "").split("_");
            String store = parts[0];
            LocalDate date = LocalDate.parse(parts[1]);

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 8) continue;

                String productId = line[0];
                String name = line[1];
                String category = line[2];
                String brand = line[3];
                double quantity = Double.parseDouble(line[4]);
                String unit = line[5];
                double price = Double.parseDouble(line[6]);
                String currency = line[7];

                Product product = new Product(productId, name, category, brand, quantity, unit);
                productsById.putIfAbsent(productId, product);

                PriceEntry entry = new PriceEntry(productId, store, date, price, currency);
                priceEntries.add(entry);
            }

        } catch (Exception e) {
            System.err.println("Error loading file: " + resourcePath + " -> " + e.getMessage());
        }
    }

    @Override
    public List<PriceEntry> getAllPriceEntries() {
        return priceEntries;
    }

    @Override
    public Map<String, Product> getProductsById() {
        return productsById;
    }
}
