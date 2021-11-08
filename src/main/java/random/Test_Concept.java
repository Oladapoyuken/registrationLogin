package random;


import org.apache.commons.text.RandomStringGenerator;

import java.util.List;

public class Test_Concept {


    public static void main(String[] args) {
        List<String> excludedPath = List.of("/api/login", "/api/register", "/api/confirm");

        boolean isExcluded = excludedPath.stream()
                .filter(path -> path.startsWith("/api"))
                .findAny().isPresent();

//        System.out.println(isExcluded);

        int num = ((int) (Math.random()*(9999-1000)))-1000;

        System.out.println(num);

    }
}
