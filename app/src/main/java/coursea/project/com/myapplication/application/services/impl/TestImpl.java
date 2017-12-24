package coursea.project.com.myapplication.application.services.impl;

import coursea.project.com.myapplication.application.services.TestService;
import coursea.project.com.myapplication.domain.model.Test;
import coursea.project.com.myapplication.infrastructure.mock.TestFactory;

/**
 * Created by chidra on 12/23/17.
 */

public class TestImpl implements TestService{
    @Override
    public Test getTest() {
        return TestFactory.createHistoryTest1();
    }
}
