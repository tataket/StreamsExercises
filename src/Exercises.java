import javax.swing.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Exercises {
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    public Exercises() {
        this.productRepository = new ProductRepository();
        this.orderRepository = new OrderRepository();
    }

    public static void main(String[] args) {
        Exercises exercises = new Exercises();
        System.out.println("1 " + exercises.ex1());
        System.out.println("2 " + exercises.ex2());
        System.out.println("3 " + exercises.ex3());
        System.out.println("4 " + exercises.ex4());
        System.out.println("5 " + exercises.ex5());
        System.out.println("6 " + exercises.ex6());
        System.out.println("7 " + exercises.ex7());
        System.out.println("8 " + exercises.ex8());
        System.out.println("9 " + exercises.ex9());
        System.out.println("10 " + exercises.ex10());
        System.out.println("11 " + exercises.ex11());
        System.out.println("12 " + exercises.ex12());
        System.out.println("13 " + exercises.ex13());
        System.out.println("14 " + exercises.ex14());
        System.out.println("15 " + exercises.ex15());
    }

    public List<Product> ex1() {
        // Obtain a list of products belonging to category “Books” with price > 100
        return productRepository.getAll()
                .stream()
                .filter(product -> product.getCategory().equalsIgnoreCase("Books")
                        && product.getPrice() > 100)
                .toList();

    }

    public List<Order> ex2() {
        // Obtain a list of orders with products belonging to category “Baby”
        return orderRepository.getAll()
                .stream()
                .filter(order -> order.getProducts()
                        .stream()
                        .anyMatch(order1 -> order1.getCategory().equalsIgnoreCase("Baby")))
                .toList();

    }

    public List<Product> ex3() {
        // Obtain a list of products with category = “Toys” and then apply 10% discount
        return productRepository.getAll()
                .stream()
                .filter(product -> product.getCategory().equalsIgnoreCase("Toys"))
                .map(product -> {
                    product.setPrice(product.getPrice() - product.getPrice() * 0.1);
                    return product;
                })
                .toList();

    }

    public List<Product> ex4() {
        // Obtain a list of products ordered by customers of tier 2 and order date between 01-Feb-2021 and 01-Apr-2021
        return orderRepository.getAll()
                .stream()
                .filter(order -> order.getCustomer().getTier() == 2
                        && order.getOrderDate().isAfter(LocalDate.of(2021, 2, 1))
                        && order.getOrderDate().isBefore(LocalDate.of(2021, 4, 1)))
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .toList();
    }

    public Optional<Product> ex5() {
        // Obtain the cheapest product from the “Books” category
        return productRepository.getAll()
                .stream()
                .filter(product -> product.getCategory().equalsIgnoreCase("Books"))
                .min(Comparator.comparingDouble(Product::getPrice));

    }

    public List<Order> ex6() {
        // Obtain the 3 most recently placed orders
        // HELPFUL TIP: the method limit() might be useful here ;)
        return orderRepository.getAll()
                .stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(3)
                .collect(Collectors.toList());

    }

    public List<Product> ex7() {
        // Obtain the list of orders which were ordered on 15-Mar-2021,
        // log each order to the console and then return their product list
        // HELPFUL TIP: the methods peek() and flatMap() might be useful here ;)
        return orderRepository.getAll()
                .stream()
                .filter(order -> order.getOrderDate().isBefore(LocalDate.of(2021, 3, 15)))
                .peek(order -> System.out.println(order.getOrderDate()))
                .flatMap(order -> order.getProducts().stream())
                .toList();

    }

    public Double ex8() {
        // Calculate total sum of all orders placed in Feb 2021
        return orderRepository.getAll()
                .stream()
                .filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 1, 31))
                        && order.getOrderDate().isBefore(LocalDate.of(2021, 3, 1)))
                .flatMap(order -> order.getProducts().stream())
                .map(Product::getPrice)
                .reduce(0.0, Double::sum);
    }

    public Double ex9() {
        // Calculate average order payment placed on 14-Mar-2021
        return orderRepository.getAll()
                .stream()
                .filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021, 3, 14)))
                .flatMap(order -> order.getProducts().stream())
                .map(Product::getPrice)
                .collect(Collectors.averagingDouble(e -> e));
    }

    public DoubleSummaryStatistics ex10() {
        // Obtain a collection of statistic figures (i.e. sum, average, max, min, count)
        // for all products of category “Books”
        // HELPFUL TIP: the methods mapToDouble() and summaryStatistics() might be useful here ;)
        return productRepository.getAll()
                .stream()
                .filter(product -> product.getCategory().equalsIgnoreCase("Books"))
                .mapToDouble(Product::getPrice)
                .summaryStatistics();
    }

    public Map<Long, Integer> ex11() {
        // Obtain a map with order id as key and each order’s product count as value
        // HELPFUL TIP: the method Collectors.toMap() might be useful here
        return orderRepository.getAll()
                .stream()
                .collect(Collectors.toMap(Order::getId,
                        order -> order.getProducts().size()));
    }

    public Map<Customer, List<Order>> ex12() {
        // Obtain a map with orders grouped by customer
        // HELPFUL TIP: the method Collectors.groupingBy() might be useful here ;)
        return orderRepository.getAll()
                .stream()
                .collect(Collectors.groupingBy(Order::getCustomer))
                ;
    }

    public Map<Order, Double> ex13() {
        // Obtain a map with order as key and the order's products total sum as value
        // HELPFUL TIP: the method Collectors.toMap() might be useful here ;)
        return orderRepository.getAll()
                .stream()
                .collect(Collectors.toMap(o -> o, order -> order.getProducts()
                        .stream()
                        .mapToDouble(Product::getPrice)
                        .sum()));
    }

    public Map<String, List<String>> ex14() {
        // Obtain a map with list of product names by category
        // HELPFUL TIP: the method Collectors.groupingBy() might be useful here ;)
        return productRepository.getAll()
                .stream()
                .collect(Collectors.groupingBy(Product::getCategory,
                        Collectors.mapping(Product::getName,
                                Collectors.toList())));

    }

    public Map<String, Optional<Product>> ex15() {
        // Obtain the most expensive product by category
        // HELPFUL TIP: the method Collectors.groupingBy() might be useful here ;)
        return productRepository.getAll()
                .stream()
                .collect(Collectors.groupingBy(Product::getCategory,
                        Collectors.maxBy(Comparator.comparingDouble(Product::getPrice))))
                ;
    }
}
