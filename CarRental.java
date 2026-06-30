import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarRental {

    // ==========================================
    // 1. CAR CLASS (Encapsulates car data)
    // ==========================================
    static class Car {
        private String carId;
        private String brand;
        private String model;
        private double basePricePerDay;
        private boolean isAvailable;

        public Car(String carId, String brand, String model, double basePricePerDay) {
            this.carId = carId;
            this.brand = brand;
            this.model = model;
            this.basePricePerDay = basePricePerDay;
            this.isAvailable = true;
        }

        public String getCarId() { return carId; }
        public String getBrand() { return brand; }
        public String getModel() { return model; }
        public boolean isAvailable() { return isAvailable; }

        public double calculatePrice(int rentalDays) {
            return basePricePerDay * rentalDays;
        }

        public void rent() {
            this.isAvailable = false;
        }

        public void returnCar() {
            this.isAvailable = true;
        }
    }

    // ==========================================
    // 2. CUSTOMER CLASS (Encapsulates user data)
    // ==========================================
    static class Customer {
        private String customerId;
        private String name;

        public Customer(String customerId, String name) {
            this.customerId = customerId;
            this.name = name;
        }

        public String getCustomerId() { return customerId; }
        public String getName() { return name; }
    }

    // ==========================================
    // 3. RENTAL CLASS (Represents the transaction)
    // ==========================================
    static class Rental {
        private Car car;
        private Customer customer;
        private int days;

        public Rental(Car car, Customer customer, int days) {
            this.car = car;
            this.customer = customer;
            this.days = days;
        }

        public Car getCar() { return car; }
        public Customer getCustomer() { return customer; }
        public int getDays() { return days; }
    }

    // ==========================================
    // 4. AGENCY CLASS (Manages state & operations)
    // ==========================================
    static class RentalAgency {
        private List<Car> cars = new ArrayList<>();
        private List<Customer> customers = new ArrayList<>();
        private List<Rental> rentals = new ArrayList<>();

        public void addCar(Car car) { cars.add(car); }
        public void addCustomer(Customer customer) { customers.add(customer); }

        public void displayAvailableCars() {
            System.out.println("\n--- Available Cars ---");
            boolean found = false;
            for (Car car : cars) {
                if (car.isAvailable()) {
                    System.out.printf("[%s] %s %s - $%.2f/day\n", 
                        car.getCarId(), car.getBrand(), car.getModel(), car.basePricePerDay);
                    found = true;
                }
            }
            if (!found) System.out.println("No cars available at the moment.");
        }

        public void rentCar(String carId, String customerId, int days) {
            Car car = findCar(carId);
            Customer customer = findCustomer(customerId);

            if (car == null || customer == null) {
                System.out.println("❌ Invalid Car ID or Customer ID.");
                return;
            }

            if (!car.isAvailable()) {
                System.out.println("❌ Sorry, this car is already rented.");
                return;
            }

            car.rent();
            rentals.add(new Rental(car, customer, days));
            System.out.println("\n✅ Rental Confirmed!");
            System.out.printf("Total Cost for %s: $%.2f\n", car.getModel(), car.calculatePrice(days));
        }

        public void returnCar(String carId) {
            Car car = findCar(carId);
            if (car == null) {
                System.out.println("❌ Car not found.");
                return;
            }

            if (car.isAvailable()) {
                System.out.println("❌ This car is already at the agency.");
                return;
            }

            car.returnCar();
            // Remove from active rentals tracking if necessary
            rentals.removeIf(rental -> rental.getCar().getCarId().equals(carId));
            System.out.printf("✅ %s %s has been successfully returned!\n", car.getBrand(), car.getModel());
        }

        private Car findCar(String carId) {
            for (Car c : cars) if (c.getCarId().equalsIgnoreCase(carId)) return c;
            return null;
        }

        private Customer findCustomer(String customerId) {
            for (Customer c : customers) if (c.getCustomerId().equalsIgnoreCase(customerId)) return c;
            return null;
        }
    }

    // ==========================================
    // 5. MAIN MENU INTERFACE
    // ==========================================
    public static void main(String[] args) {
        RentalAgency agency = new RentalAgency();
        Scanner scanner = new Scanner(System.in);

        // Seed Sample Data
        agency.addCar(new Car("C001", "Toyota", "Camry", 50.0));
        agency.addCar(new Car("C002", "Honda", "Civic", 45.0));
        agency.addCar(new Car("C003", "Tesla", "Model 3", 90.0));
        
        agency.addCustomer(new Customer("U001", "Alex"));
        agency.addCustomer(new Customer("U002", "Blake"));

        System.out.println("Welcome to the OOP Car Rental System!");

        while (true) {
            System.out.println("\n==============================");
            System.out.println("1. View Available Cars");
            System.out.println("2. Rent a Car");
            System.out.println("3. Return a Car");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear newline buffer

            switch (choice) {
                case 1:
                    agency.displayAvailableCars();
                    break;
                case 2:
                    System.out.print("Enter Customer ID (e.g., U001): ");
                    String custId = scanner.nextLine();
                    System.out.print("Enter Car ID (e.g., C001): ");
                    String carId = scanner.nextLine();
                    System.out.print("Enter Rental Duration (Days): ");
                    int days = scanner.nextInt();
                    agency.rentCar(carId, custId, days);
                    break;
                case 3:
                    System.out.print("Enter Car ID to Return: ");
                    String returnId = scanner.nextLine();
                    agency.returnCar(returnId);
                    break;
                case 4:
                    System.out.println("Thank you for choosing us. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
            }
        }
    }
}