package listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Retry implements IRetryAnalyzer {
    private static final int MAX_RETRY = 2;
    private final Map<String, Integer> attempts = new ConcurrentHashMap<>();

    @Override
    public boolean retry(ITestResult result) {
        String key = result.getMethod().getQualifiedName() + Arrays.toString(result.getParameters());
        int used = attempts.merge(key, 1, Integer::sum);
        return used <= MAX_RETRY;
    }
}