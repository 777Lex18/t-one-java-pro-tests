package t.one.java.pro.methods;

import t.one.java.pro.model.Employee;

import java.util.*;
import java.util.stream.Collectors;

public class MethodsStream {

    public List<String> noNull(List<String> list) {
        return list.stream()
                .filter(e -> e != null && !e.trim().isEmpty())
                .distinct()
                .toList();
    }
    public long uniqueLetters(List<String> list) {
        return list.stream()
                .flatMap(e -> e.chars().mapToObj(c -> (char) c))
                .map(Character::toLowerCase)
                .distinct()
                .count();
    }
    public String longestWord(List<String> list) {
        return list.stream()
                .max(Comparator.comparing(String::length)).orElse(null);
    }
    public Integer thirdLargestNumber(List<Integer> list) {
        return list.stream()
                .distinct()
                .sorted(Collections.reverseOrder())
                .skip(2)
                .findFirst().orElse(null);
    }
    public List<String> threeEmployees(List<Employee> list) {
        return list.stream()
                .filter(e -> "Инженер".equals(e.getPosition()))
                .sorted(Comparator.comparing(Employee::getAge).reversed())
                .limit(3)
                .map(Employee::getName)
                .toList();
    }
    public Double middleAge(List<Employee> list) {
        return list.stream()
                .filter(e -> "Инженер".equals(e.getPosition()))
                .mapToInt(Employee::getAge)
                .average().orElse(0.0);
    }
    public Map<Integer, List<String>> getMap(String str) {
        return Arrays.stream(str.split("[\\p{Punct}\\s]+"))
                .filter(e -> !e.isEmpty())
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.groupingBy(String::length));
    }
    public List<String> getListLongestWords(List<String> list) {
        return list.stream()
                .map(str -> Arrays.stream(str.split("[\\p{Punct}\\s]+"))
                .filter(s -> !s.isEmpty())
                .max(Comparator.comparing(String::length))
                .orElse("")
                )
                .collect(Collectors.toList());
    }
}

