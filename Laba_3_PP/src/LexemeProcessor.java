import java.io.*;
import java.util.*;
import java.util.regex.*;

public class LexemeProcessor {
    public static void main(String[] args) {
        try {
            // Чтение строк из файла
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line1 = reader.readLine();
            String line2 = reader.readLine();
            reader.close();

            // Разделение лексем на основе разделителей из второй строки
            String[] lexemes = splitLexemes(line1, line2);
            List<String> dateTimeList = new ArrayList<>();
            List<Integer> decimalNumbers = new ArrayList<>();

            // Поиск целых чисел и времени в лексемах
            for (String lexeme : lexemes) {
                if (isInteger(lexeme)) {
                    decimalNumbers.add(Integer.parseInt(lexeme));
                } else if (isValidTime(lexeme)) {
                    dateTimeList.add(lexeme);
                }
            }

            // Сортировка времени в лексикографическом порядке
            Collections.sort(dateTimeList, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });

            // Добавление случайного числа в список целых чисел
            Random random = new Random();
            if (!decimalNumbers.isEmpty()) {
                int randomNum = random.nextInt(100); // Случайное число от 0 до 99
                decimalNumbers.add(randomNum);
            }

            // Удаление самой короткой лексемы, начинающейся с цифры
            String shortestLexeme = findShortestStartingWithDigit(lexemes);
            if (shortestLexeme != null) {
                line1 = line1.replace(shortestLexeme, "").trim();
            }

            // Запись результатов в файл
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write("Оригинальная строка: " + line1 + "\n");
            writer.write("Лексемы: " + Arrays.toString(lexemes) + "\n");
            writer.write("Целые числа: " + decimalNumbers + "\n");
            writer.write("Времена: " + dateTimeList + "\n");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для разделения строки на лексемы на основе разделителей
    private static String[] splitLexemes(String input, String delimiters) {
        if (delimiters.length() == 1) {
            return input.split(String.valueOf(delimiters.charAt(0)));
        } else {
            String regex = "[" + Pattern.quote(delimiters) + "]+";
            return input.split(regex);
        }
    }

    // Метод для проверки, является ли строка целым числом
    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Метод для проверки, является ли строка корректным временем в формате HH:MM:SS
    private static boolean isValidTime(String str) {
        String timePattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
        return Pattern.matches(timePattern, str);
    }

    // Метод для поиска самой короткой лексемы, начинающейся с цифры
    private static String findShortestStartingWithDigit(String[] lexemes) {
        String shortest = null;
        for (String lexeme : lexemes) {
            if (Character.isDigit(lexeme.charAt(0))) {
                if (shortest == null || lexeme.length() < shortest.length()) {
                    shortest = lexeme;
                }
            }
        }
        return shortest;
    }
}
