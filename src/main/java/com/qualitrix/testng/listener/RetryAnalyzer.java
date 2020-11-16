package com.qualitrix.testng.listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Created by Qualitrix on DD/MM/YYY.
 *
 * @author
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    int counter = 0;

    @Override
    public boolean retry(ITestResult result) {
        RetryCount annotation = result.getMethod().getConstructorOrMethod().getMethod()
            .getAnnotation(RetryCount.class);
        result.getTestContext().getSkippedTests().removeResult(result.getMethod());
        if ((annotation != null) && (counter < annotation.maxRetryCount())) {
            counter++;
            System.out.println("Retry #" + counter + " for test: "
                + result.getMethod().getMethodName() + ", on thread: "
                + Thread.currentThread().getName());
            return true;
        }
        return false;
    }
}
