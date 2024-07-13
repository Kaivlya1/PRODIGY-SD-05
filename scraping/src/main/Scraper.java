package demo.src.main;

import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Scraper {

    public static void main(String[] args) {
        Properties prop = new Properties();

        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            // Load properties file
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the URL of the e-commerce website:");
        String url = scanner.nextLine();

        System.out.println("Enter the CSS selector for the product container:");
        String productSelector = scanner.nextLine();

        System.out.println("Enter the CSS selector for the product name:");
        String nameSelector = scanner.nextLine();

        System.out.println("Enter the CSS selector for the product price:");
        String priceSelector = scanner.nextLine();

        System.out.println("Enter the CSS selector for the product rating:");
        String ratingSelector = scanner.nextLine();

        List<String[]> data = new ArrayList<>();
        // Add CSV header
        data.add(new String[]{"Name", "Price", "Rating"});

        try {
            Document doc = Jsoup.connect(url).get();
            Elements products = doc.select(productSelector);

            for (Element product : products) {
                String name = product.select(nameSelector).text();
                String price = product.select(priceSelector).text();
                String rating = product.select(ratingSelector).text();
                data.add(new String[]{name, price, rating});
            }

            // Write data to CSV file
            try (CSVWriter writer = new CSVWriter(new FileWriter("products.csv"))) {
                writer.writeAll(data);
            }

            System.out.println("Data successfully scraped and saved to products.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}
