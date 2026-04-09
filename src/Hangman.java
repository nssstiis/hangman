import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    public static final int MAX_MISTAKES = 6;
    public static final String PLAY = "играть";
    public static final String EXIT = "выйти";
    public static final String YES_RESTART = "да";
    public static final String NO_RESTART = "нет";

    public static int rightLetters;
    public static int mistakes;
    public static char[] maskedWord;
    public static String word;
    public static List<Character> knownLetters = new ArrayList<>();

    public static Scanner scanner = new Scanner(System.in);

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

    public static void loopGame(char[] maskedWord) {
        rightLetters = 0;
        mistakes = 0;

        while (mistakes != MAX_MISTAKES && hasHiddenLetters(maskedWord)) {
            printWordState(maskedWord);
            HangmanPrinter.draw(mistakes);

            char attemptLetter = inputLetter();

            if (!isUsedLetter(attemptLetter)) {
                if (containsLetter(attemptLetter)) {
                    rightLetters++;
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

    public static boolean isGameStart() {
        System.out.println("Чтобы начать новую игру, введите (" + PLAY + "); Чтобы покинуть игру, введите (" + EXIT + ")");

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

    public static List<String> readFile() {
        try {
            return Files.readAllLines(Path.of("words.txt"));
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл, возникла ошибка:" + e.getMessage());
            System.out.println("Работа программы будет прекращена :(");
            System.exit(1);
            return null;
        }
    }

    public static String chooseRandomWord() {
        String[] words = readFile().toArray(new String[0]);
        return words[new Random().nextInt(words.length)];
    }

    public static char[] maskWord(String randomWord) {
        char[] wordLetters = randomWord.toCharArray();
        Arrays.fill(wordLetters, '*');
        return wordLetters;
    }

    public static void printWordState(char[] maskedWord) {
        System.out.print("Загаданное слово: ");

        for (char symbol : maskedWord) {
            System.out.print(symbol + " ");
        }
        System.out.println();
    }

    public static char inputLetter() {
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

    public static boolean isUsedLetter(char letter) {
        if (knownLetters.contains(letter)) {
            System.out.println("Буква " + "'" + letter + "'" + " была введена ранее, введите другую букву");
            return true;

        } else {
            knownLetters.add(letter);
            return false;
        }
    }
    private static boolean containsLetter(char attemptLetter) {
        return word.indexOf(attemptLetter) != -1;
    }

    public static void openLetter(char letter) {
        char[] wordLetters = word.toCharArray();

        for (int i = 0; i < wordLetters.length; i++) {
            if (wordLetters[i] == letter) {
                maskedWord[i] = letter;
            }
        }
    }

    public static void showUsedLetters() {
        System.out.println("Использованные буквы " + knownLetters);
    }

    public static boolean hasHiddenLetters(char[] maskedWord) {
        for (char symbol : maskedWord) {
            if (symbol == '*') {
                return true;
            }
        }

        return false;
    }

    public static void handleGameResults() {
        if (!hasHiddenLetters(maskedWord)) {
            HangmanPrinter.draw(mistakes);
            printWordState(maskedWord);
            System.out.println("Вы выиграли :)");
        } else {
            HangmanPrinter.draw(mistakes);
            System.out.println("Вы проиграли :( Загаданным словом было: " + word);
        }
    }

    public static boolean isGameRestart() {
        System.out.println("Хотите начать игру заново? (" + YES_RESTART + "/" + NO_RESTART + ")");

        while (true) {
            String validationRestart = scanner.nextLine();

            switch (validationRestart.toLowerCase()) {
                case YES_RESTART:
                    return true;
                case NO_RESTART:
                    return false;
                default:
                    System.out.println("Некорректный ввод! Введите " + YES_RESTART + " или " + NO_RESTART);
            }
        }
    }
}
