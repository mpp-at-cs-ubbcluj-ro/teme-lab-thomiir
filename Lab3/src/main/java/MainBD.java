import domain.Car;
import repository.CarRepository;
import repository.CarsDBRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainBD {
    public static void main(String[] args) {

        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        CarRepository carRepo=new CarsDBRepository(props);
        carRepo.add(new Car("Tesla","Model S", 2019));
        System.out.println("Toate masinile din db");
        for(Car car:carRepo.findAll())
            System.out.println(car);
        carRepo.update(3, new Car("Tesla","Model S", 2020));
        System.out.println("Toate masinile din db");
        for(Car car:carRepo.findAll())
            System.out.println(car);

        System.out.println("Masinile dintre 2016 si 2019:");
        for (Car car : carRepo.findBetweenYears(2016,2019))
            System.out.println(car);
    }
}
