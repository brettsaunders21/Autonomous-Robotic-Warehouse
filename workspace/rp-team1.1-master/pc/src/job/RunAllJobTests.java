package job;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  JobInputTest.class,
  JobSelectionTest.class,
  TrainingAlgorithmTest.class
})

public class RunAllJobTests {
}
