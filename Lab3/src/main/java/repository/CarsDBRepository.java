package repository;

import domain.Car;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository{

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing repository.CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
 	logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from cars where cars.manufacturer = ?")) {
            preStmt.setString(1, manufacturerN);
            loadCars(cars, preStmt);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(cars);
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from cars where cars.year >= ? and cars.year <= ?")) {
            preStmt.setInt(1, min);
            preStmt.setInt(2, max);
            loadCars(cars, preStmt);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(cars);
        return cars;
    }

    private void loadCars(List<Car> cars, PreparedStatement preStmt) throws SQLException {
        try (ResultSet result = preStmt.executeQuery()) {
            while (result.next()) {
                int id = result.getInt("id");
                String manufacturer = result.getString("manufacturer");
                String model = result.getString("model");
                int year = result.getInt("year");
                Car car = new Car(manufacturer, model, year);
                car.setId(id);
                cars.add(car);
            }
        }
    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into cars (manufacturer, model, year) values (?,?,?)")) {
            preStmt.setString(1, elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3, elem.getYear());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
    }

    @Override
    public void update(Integer integer, Car elem) {
        logger.traceEntry("updating task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update cars set manufacturer=?, model=?, year=? where cars.id=?")) {
            preStmt.setString(1, elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3, elem.getYear());
            preStmt.setInt(4, integer);
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
    }

    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from cars")) {
            loadCars(cars, preStmt);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(cars);
        return cars;
    }
}
