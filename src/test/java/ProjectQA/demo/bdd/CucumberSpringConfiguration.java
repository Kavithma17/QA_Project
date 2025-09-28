package ProjectQA.demo.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import ProjectQA.demo.DemoApplication;
import org.springframework.transaction.annotation.Transactional;

@CucumberContextConfiguration
@SpringBootTest(classes = DemoApplication.class)
@Transactional
public class CucumberSpringConfiguration {
}
