import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("shop.xml"));

        ReadBlock load = new ReadBlock(doc.getElementsByTagName("load").item(0));
        ReadBlock save = new ReadBlock(doc.getElementsByTagName("save").item(0));
        ReadBlock log = new ReadBlock(doc.getElementsByTagName("log").item(0));

        Basket basket = null;
        File textFile = new File("basket.txt");
        File jsonFile = new File("basket.json");

        try {
            basket = Basket.loadFromTxtFile(textFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            basket = Basket.loadFromJson(jsonFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (basket == null) {
            String[] products = {"Хлеб", "Арбуз", "Молоко"};
            int[] prices = {100, 200, 300};
            basket = new Basket(products, prices);
        }
        ClientLog clientLog = new ClientLog();

        if (load.enabled) {
            if (load.format.equals("txt")) {
                try {
                    basket = Basket.loadFromTxtFile(new File(load.fileName));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (load.format.equals("json")) {
                try {
                    basket = Basket.loadFromJson(new File(load.fileName));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.println("Программа для сфорирования продуктовой корзины");
        System.out.println("Список возможных товаров для покупки");
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < basket.getProducts().length; i++) {
            System.out.println((i + 1) + " " + basket.getProducts()[i] + " " + basket.getPrices()[i] + " руб/шт");
        }
        while (true) {
            System.out.println("Выберите товар и количество или введите 'end' ");
            String inputString = scanner.nextLine();
            int productNumber = 0;
            int productCount = 0;
            if ("end".equals(inputString)) {
                System.out.println("Ваша корзина:");
                break;
            }
            String[] myPrice = inputString.split(" ");
            productNumber = Integer.parseInt(myPrice[0]) - 1;
            productCount = Integer.parseInt(myPrice[1]);

            clientLog.log(productNumber + 1, productCount);
            basket.addToCart(productNumber, productCount);

        }
        basket.saveTxt(textFile);
        basket.saveJson(jsonFile);
        clientLog.exportAsCSV(new File("log.csv"));

        if (save.enabled) {
            if (save.format.equals("txt")) {
                try {
                    basket.saveTxt(new File(save.fileName));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (save.format.equals("json")) {
                try {
                    basket.saveJson(new File(save.fileName));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        if (log.enabled) {
            try {
                clientLog.exportAsCSV(new File(log.fileName));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        basket.printCart();

    }
}

class ReadBlock {
    boolean enabled;
    String fileName;
    String format;

    public ReadBlock(Node node) {
        NodeList listNode = node.getChildNodes();

        for (int i = 0; i < listNode.getLength(); i++) {
            Node currentNode = listNode.item(i);
            if (Node.ELEMENT_NODE == currentNode.getNodeType()) {
                if (currentNode.getNodeName().equals("enabled")) {
                    enabled = Boolean.parseBoolean(currentNode.getTextContent());
                }
                if (currentNode.getNodeName().equals("fileName")) {
                    fileName = currentNode.getTextContent();
                } else if (currentNode.getNodeName().equals("format")) {
                    format = currentNode.getTextContent();
                }
            }
        }
    }
}