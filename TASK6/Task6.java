import java.util.*;

public class Task6 {
    public static void main(String[] args) {
        System.out.println(hiddenAnagram("Bright is the moon", "Bongo mirth"));
        System.out.println(stripUrlParams("https://edabit.com", new String[]{"b"}));
        System.out.println(nicoCipher("iloveher", "612345"));
        System.out.println(Arrays.toString(twoPair(new int[]{100, 12, 4, 1, 2}, 15)));
        System.out.println(Arrays.toString(isExact(40320)));
        System.out.println(fractions("0.1097(3)"));
        System.out.println(pilishString(""));
        System.out.println(formula("16 * 10 = 160 = 14 + 120"));
        System.out.println(isValid("abcdefghhgfedcba"));
        System.out.println(palindromeDescendant(11));
    }
    //таск 1
    public static String hiddenAnagram(String text, String phrase) {
        String cleanedText = text.replaceAll("[^a-zA-Z]", "").toLowerCase();
        String cleanedPhrase = phrase.replaceAll("[^a-zA-Z]", "").toLowerCase();
        Map<Character, Integer> phraseCount = new HashMap<>();
        for (char c : cleanedPhrase.toCharArray()) {
            phraseCount.put(c, phraseCount.getOrDefault(c, 0) + 1);
        }
        int windowSize = cleanedPhrase.length();
        Map<Character, Integer> windowCount = new HashMap<>();
        for (int i = 0; i < cleanedText.length(); i++) {
            char currentChar = cleanedText.charAt(i);
            windowCount.put(currentChar, windowCount.getOrDefault(currentChar, 0) + 1);
            if (i >= windowSize) {
                char leftChar = cleanedText.charAt(i - windowSize);
                windowCount.put(leftChar, windowCount.get(leftChar) - 1);
                if (windowCount.get(leftChar) == 0) {
                    windowCount.remove(leftChar);
                }
            }
            if (windowCount.equals(phraseCount)) {
                return cleanedText.substring(i - windowSize + 1, i + 1);
            }
        }
        return "noutfond";
    }
    //таск 2
    public static String stripUrlParams(String url, String[] params) {
        String[] urlParts = url.split("\\?", 2);
        String baseUrl = urlParts[0];
        if (urlParts.length == 1) {
            return baseUrl;
        }
        String[] paramPairs = urlParts[1].split("&");
        Map<String, String> paramsMap = new LinkedHashMap<>();
        for (String pair : paramPairs) {
            String[] keyValue = pair.split("=", 2);
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1] : "";
            paramsMap.put(key, value);
        }
        if (params != null) {
            for (String param : params) {
                paramsMap.remove(param);
            }
        }
        List<String> sortedKeys = new ArrayList<>(paramsMap.keySet());
        Collections.sort(sortedKeys);
        StringBuilder result = new StringBuilder(baseUrl);
        if (!sortedKeys.isEmpty()) {
            result.append("?");
            appendParams(sortedKeys, paramsMap, result, 0);
        }
        return result.toString();
    }
    private static void appendParams(List<String> keys, Map<String, String> paramsMap, StringBuilder result, int index) {
        if (index >= keys.size()) {
            return;
        }
        String key = keys.get(index);
        result.append(key).append("=").append(paramsMap.get(key));
        if (index < keys.size() - 1) {
            result.append("&");
        }
        appendParams(keys, paramsMap, result, index + 1);
    }
    //таск 3
    public static String nicoCipher(String message, String key) {
        int keyLength = key.length();
        Integer[] keyOrder = getKeyOrder(key);
        int padding = keyLength - (message.length() % keyLength);
        if (padding != keyLength) {
            message += " ".repeat(padding);
        }
        int rows = message.length() / keyLength;
        char[][] grid = new char[rows][keyLength];
        for (int i = 0; i < message.length(); i++) {
            grid[i / keyLength][i % keyLength] = message.charAt(i);
        }
        char[][] sortedGrid = new char[rows][keyLength];
        for (int i = 0; i < keyLength; i++) {
            int targetColumn = keyOrder[i];
            for (int j = 0; j < rows; j++) {
                sortedGrid[j][i] = grid[j][targetColumn];
            }
        }
        StringBuilder result = new StringBuilder();
        for (char[] row : sortedGrid) {
            for (char c : row) {
                result.append(c);
            }
        }
        return result.toString();
    }
    private static Integer[] getKeyOrder(String key) {
        Character[] keyChars = key.chars()
                .mapToObj(c -> (char) c)
                .toArray(Character[]::new);
        Character[] sortedKeyChars = keyChars.clone();
        Arrays.sort(sortedKeyChars);
        Map<Character, Queue<Integer>> charIndexMap = new HashMap<>();
        for (int i = 0; i < keyChars.length; i++) {
            charIndexMap.putIfAbsent(keyChars[i], new LinkedList<>());
            charIndexMap.get(keyChars[i]).add(i);
        }
        Integer[] keyOrder = new Integer[key.length()];
        for (int i = 0; i < sortedKeyChars.length; i++) {
            keyOrder[i] = charIndexMap.get(sortedKeyChars[i]).poll();
        }
        return keyOrder;
    }
    //таск 4
    public static int[] twoPair(int[] arr, int n) {
        Map<Integer, Integer> seenNumbers = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            int current = arr[i];
            if (n % current == 0) {
                int pair = n / current;
                if (seenNumbers.containsKey(pair)) {
                    return new int[]{arr[seenNumbers.get(pair)], current};
                }
            }
            seenNumbers.put(current, i);
        }
        return new int[]{};
    }
    //таск 5
    private static int[] isExactFactorial(int n, int currentFactorial, int currentNumber) {
        if (currentFactorial == n) {
            return new int[]{n, currentNumber};
        }
        if (currentFactorial > n) {
            return new int[]{};
        }
        return isExactFactorial(n, currentFactorial * (currentNumber + 1), currentNumber + 1);
    }
    public static int[] isExact(int n) {
        return isExactFactorial(n, 1, 1);
    }
    //таск 6
    public static String fractions(String s) {
        if (!s.contains("(")) {
            return convertToFraction(s);
        } else {
            int index = s.indexOf('(');
            String nonRepeating = s.substring(0, index);
            String repeating = s.substring(index + 1, s.length() - 1);
            return handleWithBothParts(nonRepeating, repeating);
        }
    }
    private static String convertToFraction(String s) {
        double num = Double.parseDouble(s);
        int denominator = (int) Math.pow(10, s.length() - s.indexOf('.') - 1);
        int numerator = (int) (num * denominator);
        return simplify(numerator, denominator);
    }
    private static String handleWithBothParts(String nonRepeating, String repeating) {
        String[] parts = nonRepeating.split("\\.");
        String integerPart = parts[0];
        String fractionalPart = parts.length > 1 ? parts[1] : "";
        String fullDecimal = integerPart + fractionalPart + repeating;
        int numerator = Integer.parseInt(fullDecimal) - Integer.parseInt(integerPart + fractionalPart);
        int denominator = (int) (Math.pow(10, fullDecimal.length()) - Math.pow(10, integerPart.length() + fractionalPart.length()));
        return simplify(numerator, denominator);
    }
    private static String simplify(int numerator, int denominator) {
        int gcd = gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        return numerator + "/" + denominator;
    }
    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    //таск 7
    public static String pilishString(String txt) {
        int[] piDigits = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9};
        if (txt == null || txt.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (int length : piDigits) {
            if (i >= txt.length()) {
                break;
            }
            if (i + length > txt.length()) {
                String word = txt.substring(i);
                word += repeatLastChar(txt.charAt(txt.length() - 1), length - word.length());
                result.append(word);
                break;
            } else {
                result.append(txt, i, i + length);
                i += length;
            }
            if (i < txt.length() && result.length() < txt.length() + piDigits.length) {
                result.append(" ");
            }
        }
        return result.toString();
    }
    private static String repeatLastChar(char lastChar, int times) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(lastChar).repeat(Math.max(0, times)));
        return sb.toString();
    }
    //таск 8
    public static boolean formula(String input) {
        String[] parts = input.split("=");
        if (parts.length < 2) {
            return false;
        }
        try {
            double leftResult = evaluate(parts[0].trim());
            for (int i = 1; i < parts.length; i++) {
                double rightResult = evaluate(parts[i].trim());
                if (leftResult != rightResult) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private static double evaluate(String expression) {
        expression = expression.replaceAll("\\s+", "");
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        StringBuilder currentNumber = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                currentNumber.append(c);
            } else {
                if (!currentNumber.isEmpty()) {
                    numbers.push(Double.parseDouble(currentNumber.toString()));
                    currentNumber.setLength(0);
                }
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    processTopOperator(numbers, operators);
                }
                operators.push(c);
            }
        }
        if (currentNumber.length() > 0) {
            numbers.push(Double.parseDouble(currentNumber.toString()));
        }
        while (!operators.isEmpty()) {
            processTopOperator(numbers, operators);
        }
        return numbers.pop();
    }
    private static void processTopOperator(Stack<Double> numbers, Stack<Character> operators) {
        double b = numbers.pop();
        double a = numbers.pop();
        char operator = operators.pop();
        switch (operator) {
            case '+':
                numbers.push(a + b);
                break;
            case '-':
                numbers.push(a - b);
                break;
            case '*':
                numbers.push(a * b);
                break;
            case '/':
                numbers.push(a / b);
                break;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
    private static int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }
        return 0;
    }
    //таск 9
    public static String isValid(String s) {
        Map<Character, Integer> charFrequency = new HashMap<>();
        for (char c : s.toCharArray()) {
            charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
        }
        Map<Integer, Integer> frequencyCount = new HashMap<>();
        for (int freq : charFrequency.values()) {
            frequencyCount.put(freq, frequencyCount.getOrDefault(freq, 0) + 1);
        }
        if (frequencyCount.size() == 1) {
            return "YES";
        }
        if (frequencyCount.size() > 2) {
            return "NO";
        }
        int freq1 = -1;
        int freq2 = -1;
        int count1 = 0;
        int count2 = 0;
        for (var entry : frequencyCount.entrySet()) {
            if (freq1 == -1) {
                freq1 = entry.getKey();
                count1 = entry.getValue();
            } else {
                freq2 = entry.getKey();
                count2 = entry.getValue();
            }
        }
        if ((freq1 == 1 && count1 == 1) || (freq2 == 1 && count2 == 1)) {
            return "YES";
        }
        if ((Math.abs(freq1 - freq2) == 1) && ((freq1 > freq2 && count1 == 1) || (freq2 > freq1 && count2 == 1))) {
            return "YES";
        }
        return "NO";
    }
    //таск 10
    public static boolean palindromeDescendant(int num) {
        String numStr = String.valueOf(num);
        while (numStr.length() >= 2) {
            if (isPalindrome(numStr)) {
                return true;
            }

            numStr = generateDescendant(numStr);
        }

        return false;
    }
    private static boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
    private static String generateDescendant(String numStr) {
        StringBuilder descendant = new StringBuilder();

        for (int i = 0; i < numStr.length(); i += 2) {
            int digit1 = Character.getNumericValue(numStr.charAt(i));
            int digit2 = Character.getNumericValue(numStr.charAt(i + 1));
            descendant.append(digit1 + digit2);
        }
        return descendant.toString();
    }
}