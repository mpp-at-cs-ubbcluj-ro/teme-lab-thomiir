import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.ComputerRepairRequestRepository;
import repository.ComputerRepairedFormRepository;
import repository.file.ComputerRepairRequestFileRepository;
import repository.file.ComputerRepairedFormFileRepository;
import repository.jdbc.ComputerRepairRequestJdbcRepository;
import repository.jdbc.ComputerRepairedFormJdbcRepository;
import services.ComputerRepairServices;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RepairShopConfig {
    @Bean
    Properties getProps() {
        Properties props = new Properties();
        try {
            System.out.println("Searching bd.config in directory " +((new File(".").getAbsolutePath())));
            props.load(new FileReader("bd.config"));
        }
        catch (IOException e) {
            System.err.println("Configuration file bd.config not found" + e);
        }
        return props;
    }

    @Bean
    ComputerRepairRequestRepository requestsRepo(){
//        return new ComputerRepairRequestJdbcRepository(getProps());
        return new ComputerRepairRequestFileRepository("ComputerRequests.txt");
    }

    @Bean
    ComputerRepairedFormRepository formsRepo(){
//       return new ComputerRepairedFormJdbcRepository(getProps());
        return new ComputerRepairedFormFileRepository("RepairedForms.txt", requestsRepo());

    }

    @Bean
    ComputerRepairServices services(){
        return new ComputerRepairServices(requestsRepo(), formsRepo());
    }

}
