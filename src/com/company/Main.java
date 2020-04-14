package com.company;

import java.sql.*;
import java.util.Scanner;

// (Auswertung) program1 Oster Aufgabe Mathias Angerer
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;
        try {
            String url = "jdbc:mysql://localhost:3306/program1?user=root";
            connection = DriverManager.getConnection(url);
            int choice = 1;
            while (choice != 0) {
                System.out.println("1. Wie viele Bestellungen gab es schon?");
                System.out.println("2. Wie viele Bestellungen gab es je Kunde?");
                System.out.println("3. Wie viele Bestellungen gab es je Ortschaft?");
                System.out.println("4. Was sind all meine Umsätze nach den Kriterien 1 - 3 (Gesamt, je Kunde, je Ortschaft)");
                System.out.println("5. Was wurde am häufigsten bestellt und wie oft?");
                System.out.println("6. In absteigender Reihenfolge, wie oft bestellt wurde\n");
                System.out.println("(zahl) eingeben oder 0");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        ordersTotal(connection);
                        break;
                    case 2:
                        ordersTotalCustomer(connection);
                        break;
                    case 3:
                        ordersTotalCity(connection);
                        break;
                    case 4:
                        ordersTotalCustomerCity(connection);
                        break;
                    case 5:
                        mostOrdered(connection);
                        break;
                    case 6:
                        mostOrderedDes(connection);
                        break;
                }
            }
        } catch (SQLException e) {
            throw new Error("connection problem", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void ordersTotal (Connection connection) {
        String query = "SELECT MAX(order_id) from `order`";
        try (Statement statementRead = connection.createStatement()) {
            ResultSet resultSet = statementRead.executeQuery(query);
            while (resultSet.next()) {
                int totalOrders = resultSet.getInt("MAX(order_id)");
                System.out.println("total orders: " + totalOrders + "\n");
            }
        } catch (SQLException e) {
            System.out.println("ordersTotal problem");
        }
    }

    public static void ordersTotalCustomer (Connection connection) {
        String query = "SELECT count(*), customer_id from `order` GROUP BY `customer_id`";
        try (Statement statementRead = connection.createStatement()) {
            ResultSet resultSet = statementRead.executeQuery(query);
            while (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id");
                int count = resultSet.getInt("count(*)");
                System.out.println("customer: " + customerId + " orders: " + count);
            }
        } catch (SQLException e) {
            System.out.println("ordersTotal problem");
        }
    }

    public static void ordersTotalCity (Connection connection) {
        String query = "SELECT count(*), customer_id from `order` GROUP BY `delivery_price`";
        try (Statement statementRead = connection.createStatement()) {
            ResultSet resultSet = statementRead.executeQuery(query);
            while (resultSet.next()) {
                int cityId = resultSet.getInt("customer_id");
                int count = resultSet.getInt("count(*)");
                System.out.println("city: " + cityId + " orders: " + count);
            }
        } catch (SQLException e) {
            System.out.println("ordersTotal problem");
        }
        System.out.println("\n");
    }

    public static void ordersTotalCustomerCity (Connection connection) {
        String query1 = "SELECT SUM(delivery_price + total_meal_price + extra_price) AS sum" +
                " from `order`";
        System.out.println("1.");
        try (Statement statementRead = connection.createStatement()) {
            ResultSet resultSet = statementRead.executeQuery(query1);
            while (resultSet.next()) {
                double sum = resultSet.getInt("sum");
                System.out.println("Gesamt: " + sum + "$");
            }
        } catch (SQLException e) {
            System.out.println("ordersTotal1 problem");
        }
        System.out.println("\n2.");
        String query2 = "SELECT SUM(delivery_price + total_meal_price + extra_price) AS sum, customer_id" +
                " from `order` GROUP BY `customer_id`";
        try (Statement statementRead = connection.createStatement()) {
            ResultSet resultSet = statementRead.executeQuery(query2);
            while (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id");
                double sum = resultSet.getInt("sum");
                System.out.println("Kundennummer: " + customerId + " sum: " + sum + "$");
            }
        } catch (SQLException e) {
            System.out.println("ordersTotal2 problem");
        }
        System.out.println("\n3.");
        String query3 = "SELECT SUM(delivery_price + total_meal_price + extra_price) AS sum, delivery_price" +
                " from `order` GROUP BY `delivery_price`";
        try (Statement statementRead = connection.createStatement()) {
            ResultSet resultSet = statementRead.executeQuery(query3);
            while (resultSet.next()) {
                int deliveryPrice = resultSet.getInt("delivery_price");
                double sum = resultSet.getInt("sum");
                System.out.println("Ortschaftsnummer: " + deliveryPrice + " sum: " + sum + "$");
            }
        } catch (SQLException e) {
            System.out.println("ordersTotal3 problem");
        }
        System.out.println("\n");
    }

    public static void mostOrdered (Connection connection) {
        String query3 = "SELECT count(*), meal FROM `meals_total` GROUP BY meal";
        try (Statement statementRead = connection.createStatement()) {
            ResultSet resultSet = statementRead.executeQuery(query3);
            resultSet.next();
            String meal = resultSet.getString("meal");
            int count = resultSet.getInt("count(*)");
            System.out.println(meal + ": (x" + count + ")");

        } catch (SQLException e) {
            System.out.println("mostOrdered problem");
        }
        System.out.println("\n");
    }

    public static void mostOrderedDes (Connection connection) {
        String query3 = "SELECT count(*), meal FROM `meals_total` GROUP BY meal";
        try (Statement statementRead = connection.createStatement()) {
            ResultSet resultSet = statementRead.executeQuery(query3);
            while (resultSet.next()) {
                String meal = resultSet.getString("meal");
                int count = resultSet.getInt("count(*)");
                System.out.println(meal + ": (x" + count + ")");
            }
        } catch (SQLException e) {
            System.out.println("mostOrderedDes problem");
        }
        System.out.println("\n");
    }
}
