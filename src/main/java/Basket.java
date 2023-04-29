import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;


import java.io.*;

public class Basket {

    private String[] products;
    private int[] prices;
    private long[] productBasket;
    private int productNum;
    private int amount;

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        productBasket = new long[products.length];
    }

    public void addToCart(int productNum, int amount) {
        this.productNum = productNum;
        this.amount = amount;
        productBasket[productNum] += amount;
    }

    public void printCart() {
        int sumProduct = 0;
        int sumCount = 0;
        for (int i = 0; i < productBasket.length; i++) {
            if (productBasket[i] != 0) {
                System.out.println(products[i] + " кол-во " + productBasket[i] + " шт " + prices[i] + " руб/шт " + (productBasket[i] * prices[i]) + " руб в сумме");
                sumProduct += productBasket[i] * prices[i];
                sumCount += productBasket[i];
            }
        }
        System.out.println("Кол-во продуктов в корзине: " + sumCount + " шт");
        System.out.println("Итого: " + sumProduct + " руб");
    }

    public void saveTxt(File textFile) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (String s : getProducts())
                out.print(s + " ");
            out.print("\n");
            for (int i : getPrices())
                out.print(i + " ");
            out.print("\n");
            for (long e : getProductBasket())
                out.print(e + " ");
        }
    }

    static Basket loadFromTxtFile(File textFile) throws IOException {
        if (textFile.exists()) {
            try (BufferedReader in = new BufferedReader(new FileReader(textFile));) {

                String[] products = in.readLine().strip().split(" ");

                String[] pricesStr = in.readLine().strip().split(" ");
                int[] prices = new int[pricesStr.length];

                for (int i = 0; i < prices.length; i++) {
                    prices[i] = Integer.parseInt(pricesStr[i]);
                }

                Basket basket = new Basket(products, prices);

                String[] amountsStr = in.readLine().strip().split(" ");

                for (int i = 0; i < amountsStr.length; i++) {
                    basket.productBasket[i] = Integer.parseInt(amountsStr[i]);
                }
                return basket;
            }
        } else {
            String[] products = {"Хлеб", "Арбуз", "Молоко"};
            int[] prices = {100, 200, 300};
            Basket basket = new Basket(products, prices);
            return basket;
        }
    }

    public void saveJson(File jsonFile) throws IOException {
        try (FileWriter file = new FileWriter(jsonFile, false)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            file.write(gson.toJson(this));
        }
    }

    public static Basket loadFromJson(File jsonFile) throws IOException {
        if (jsonFile.exists()) {
            try (FileReader fileLoad = new FileReader(jsonFile)) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                return gson.fromJson(fileLoad, Basket.class);
            }
        } else {
            String[] products = {"Хлеб", "Арбуз", "Молоко"};
            int[] prices = {100, 200, 300};
            Basket basket = new Basket(products, prices);
            return basket;
        }
    }


    public String[] getProducts() {
        return products;
    }

    public int[] getPrices() {
        return prices;
    }

    public long[] getProductBasket() {
        return productBasket;
    }
}