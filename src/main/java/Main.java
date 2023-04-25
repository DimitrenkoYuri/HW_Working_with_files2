import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    static Basket basket;
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        String[] products = {"Хлеб", "Арбуз", "Молоко"};
        int[] prices = {100, 200, 300};

        int[] amount = new int[products.length];
        File textFile = new File("basket.txt");
        if (textFile.exists()) {
            basket = Basket.loadFromTxtFile(textFile);
        } else {
            basket = new Basket(products, prices);
        }

        System.out.println("Список возможных товаров для покупки");
        for (int i = 0; i < products.length; i++) {
            System.out.println(i + 1 + "." + " " + products[i] + " " + prices[i] + " руб/шт");
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }
            String[] parts = input.split(" ");
            int productNumber = Integer.parseInt(parts[0]) - 1;
            int productCount = Integer.parseInt(parts[1]);
            basket.addToCart(productNumber, productCount);
            basket.saveTxt(textFile);
        }
        basket.printCart();
    }
}