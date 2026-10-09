package listener;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestLifecycleListener implements ITestListener {
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("START: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAILED: " + result.getMethod().getMethodName());
    }
}