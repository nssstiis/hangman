import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Hangman {
    public static final int MAX_MISTAKES = 6;
    public static final String PLAY = "играть";
    public static final String EXIT = "выйти";
    public static final String YES_RESTART = "да";
    public static final String NO_RESTART = "нет";

    public static int rightLetters;
    public static int mistakes;
    public static String word;
    public static Scanner scanner = new Scanner(System.in);
    public static char[] maskedWord;
    public static List<Character> knownLetters = new ArrayList<>();

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
            printStateOfWord(maskedWord);
            HangmanPrint.drawHangman(mistakes);
            char attemptLetter = enterLetter();
            if (!isLetterUsedEarlier(attemptLetter)) {
                if (isLetterInWord(attemptLetter)) {
                    rightLetters++;
                    System.out.println("Такая буква есть!");
                    openLetter(attemptLetter);
                } else {
                    mistakes++;
                    System.out.println("Такой буквы в слове нет! Количество ошибок: " + mistakes);
                }
            }
            System.out.println();
            showUsedLetters(attemptLetter);
        }
    }

    public static boolean isGameStart() {
        System.out.println("Чтобы начать новую игру, введите (" + PLAY + "); Чтобы покинуть игру, введите (" + EXIT + ")");
        while (true) {
            String validationStart = scanner.nextLine();
            switch (validationStart) {
                case PLAY:
                    return true;
                case EXIT:
                    return false;
                default:
                    System.out.println("Ошибка! Введите (" + PLAY + ") или (" + EXIT + ")");
            }
        }
    }

    private static boolean isLetterInWord(char attemptLetter) {
        return word.indexOf(attemptLetter) != -1;
    }

    public static List<String> readFile() {
        try {
            return Files.readAllLines(Path.of("words.txt"));
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл");
            return new ArrayList<>();
        }
    }

    public static String chooseRandomWord() {
        String [] words = readFile().toArray(new String[0]);
        return words[new Random().nextInt(words.length)];
    }

    public static char[] maskWord(String randomWord) {
        char[] charArrayWord = randomWord.toCharArray();
        Arrays.fill(charArrayWord, '*');
        return charArrayWord;
    }

    public static boolean hasHiddenLetters(char[] maskedWord) {
        for (char symbol : maskedWord) {
            if (symbol == '*') {
                return true;
            }
        }
        return false;
    }

    public static char enterLetter() {
        System.out.print("Введите букву: ");
        while (true) {
            String input = Hangman.scanner.nextLine();
            if (input.length() != 1) {
                System.out.println("Ошибка! Введите одну букву");
                continue;
            }
            char letter = Character.toLowerCase(input.charAt(0));
            if (!Character.isLetter(letter)) {
                System.out.println("Ошибка! Введите букву");
                continue;
            }
            return letter;
        }
    }

    public static void showUsedLetters(char attemptLetter) {
        System.out.println("Использованные буквы " + knownLetters);
    }

    public static void openLetter(char letter) {
        char[] charArrayWord = word.toCharArray();
        for (int i = 0; i < charArrayWord.length; i++) {
            if (charArrayWord[i] == letter) {
                maskedWord[i] = letter;
            }
        }
    }

    public static void printStateOfWord(char[] maskedWord) {
        System.out.print("Загаданное слово: ");
        for (char symbol : maskedWord) {
            System.out.print(symbol + " ");
        }
        System.out.println();
    }

    public static boolean isLetterUsedEarlier(char letter) {
        if (knownLetters.contains(letter)) {
            System.out.println("Буква " + "'" + letter + "'" + " была введена ранее, введите другую букву");
            return true;
        } else {
            knownLetters.add(letter);
            return false;
        }
    }

    public static void handleGameResults() {
        if (!hasHiddenLetters(maskedWord)) {
            HangmanPrint.drawHangman(mistakes);
            printStateOfWord(maskedWord);
            System.out.println("Вы выиграли :)");
        } else {
            HangmanPrint.drawHangman(mistakes);
            System.out.println("Вы проиграли :( Загаданным словом было: " + word);
        }
    }

    public static boolean isGameRestart() {
        System.out.println("Хотите начать игру заново? (" + YES_RESTART + "/" + NO_RESTART + ")");
        while (true) {
            String validationRestart = scanner.nextLine();
            switch (validationRestart) {
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
