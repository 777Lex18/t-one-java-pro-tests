package t.one.java.pro;

import t.one.java.pro.methods.MethodsStream;
import t.one.java.pro.model.Employee;

import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        MethodsStream stream = new MethodsStream();
        System.out.println("Result noNull = " + stream.noNull(Arrays.asList(null, "", "ABC", "ABC", "QQ")));
        System.out.println("Result uniqueLetters = " + stream.uniqueLetters(List.of("ABC", "CDE","EE")));
        System.out.println("Result longestWord = " + stream.longestWord(List.of("", "ABC", "CDEF","EE")));
        System.out.println("Result thirdLargestNumber = " + stream.thirdLargestNumber(List.of(5, 2, 10, 9, 4, 3, 10, 1, 13)));
        System.out.println("Result threeEmployees = " + stream.threeEmployees(List.of(
                new Employee("Петр",30,"Инженер"),
                new Employee("Вася",35,"Бух"),
                new Employee("Иван",40,"Менеджер"),
                new Employee("Саша",45,"Инженер"),
                new Employee("Вова",50,"Инженер"))));
        System.out.println("Result stream.middleAge = " + stream.middleAge(List.of(
                new Employee("Петр",30,"Инженер"),
                new Employee("Вася",35,"Инженер"),
                new Employee("Иван",40,"Инженер"),
                new Employee("Саша",45,"Инженер"),
                new Employee("Вова",50,"Инженер"))));
        System.out.println("Result getMap = " + stream.getMap("Мама мыла Окно, окно было довольно"));
        System.out.println("Result getListLongestWords = " + stream.getListLongestWords(List.of("Мама мыла Окно, окно было довольно",
                "кровать")));


    }
}