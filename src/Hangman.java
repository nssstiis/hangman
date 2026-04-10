import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    private static final int MAX_MISTAKES = 6;
    private static final String PLAY = "y";
    private static final String EXIT = "n";

    private static int mistakes;
    private static char[] maskedWord;
    private static String word;
    private static final List<Character> knownLetters = new ArrayList<>();

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (isGameStart()) {
            while (true) {
                word = chooseRandomWord();
                maskedWord = maskWord(word);

                loopGame(maskedWord);
                handleGameResults();

                if (isGameRestart()) {
                    knownLetters.clear();
                } else {
                    break;
                }
            }
        }
    }

    private static void loopGame(char[] maskedWord) {
        mistakes = 0;

        while (mistakes != MAX_MISTAKES && hasHiddenLetters(maskedWord)) {
            printWordState(maskedWord);
            HangmanPrinter.draw(mistakes);

            char attemptLetter = inputLetter();

            if (!isUsedLetter(attemptLetter)) {
                if (containsLetter(attemptLetter)) {
                    System.out.println("Такая буква есть!");
                    openLetter(attemptLetter);
                } else {
                    mistakes++;
                    System.out.println("Такой буквы в слове нет! Количество ошибок: " + mistakes);
                }
            }

            System.out.println();
            showUsedLetters();
        }
    }

    private static boolean isGameStart() {
        System.out.printf("Чтобы начать новую игру введите (%s); Чтобы покинуть игру, введите (%s) %n", PLAY, EXIT);
        while (true) {
            String validationStart = scanner.nextLine();

            switch (validationStart.toLowerCase()) {
                case PLAY:
                    return true;
                case EXIT:
                    return false;
                default:
                    System.out.println("Ошибка! Введите (" + PLAY + ") или (" + EXIT + ")");
            }
        }
    }

    private static List<String> readFile() {
        try {
            return Files.readAllLines(Path.of("words.txt"));
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл, возникла ошибка:" + e.getMessage());
            System.out.println("Работа программы будет прекращена :(");
            System.exit(1);
            return null;
        }
    }

    private static String chooseRandomWord() {
        List<String> words = readFile();
        int randomIndex = new Random().nextInt(words.size());
        return words.get(randomIndex);
    }

    private static char[] maskWord(String randomWord) {
        char[] wordLetters = randomWord.toCharArray();
        Arrays.fill(wordLetters, '*');
        return wordLetters;
    }

    private static void printWordState(char[] maskedWord) {
        System.out.print("Загаданное слово: ");

        for (char symbol : maskedWord) {
            System.out.print(symbol + " ");
        }
        System.out.println();
    }

    private static char inputLetter() {
        System.out.print("Введите букву: ");

        while (true) {
            String input = Hangman.scanner.nextLine();

            if (input.length() != 1) {
                System.out.println("Ошибка! Введите одну букву");
                continue;
            }

            char letter = Character.toLowerCase(input.charAt(0));

            if (!Character.isLetter(letter) || Character.UnicodeBlock.of(letter) != Character.UnicodeBlock.CYRILLIC) {
                System.out.println("Ошибка! Введите букву от А до Я");
                continue;
            }

            return letter;
        }
    }

    private static boolean isUsedLetter(char letter) {
        if (knownLetters.contains(letter)) {
            System.out.printf("Буква %c была введена ранее, введите другую букву %n", letter);
            return true;
        }
        knownLetters.add(letter);
        return false;
    }

    private static boolean containsLetter(char attemptLetter) {
        return word.indexOf(attemptLetter) != -1;
    }

    private static void openLetter(char letter) {
        char[] wordLetters = word.toCharArray();

        for (int i = 0; i < wordLetters.length; i++) {
            if (wordLetters[i] == letter) {
                maskedWord[i] = letter;
            }
        }
    }

    private static void showUsedLetters() {
        System.out.println("Использованные буквы " + knownLetters);
    }

    private static boolean hasHiddenLetters(char[] maskedWord) {
        for (char symbol : maskedWord) {
            if (symbol == '*') {
                return true;
            }
        }

        return false;
    }

    private static void handleGameResults() {
        if (!hasHiddenLetters(maskedWord)) {
            HangmanPrinter.draw(mistakes);
            printWordState(maskedWord);
            System.out.println("Вы выиграли :)");
        } else {
            HangmanPrinter.draw(mistakes);
            System.out.println("Вы проиграли :( Загаданным словом было: " + word);
        }
    }

    private static boolean isGameRestart() {
        System.out.printf("Хотите начать игру заново? Да - %s; Нет - %s %n", PLAY, EXIT);
        while (true) {
            String validationRestart = scanner.nextLine();

            switch (validationRestart.toLowerCase()) {
                case PLAY:
                    return true;
                case EXIT:
                    return false;
                default:
                    System.out.printf("Некорректный ввод! Введите %s или %s %n", PLAY, EXIT);
            }
        }
    }
}
