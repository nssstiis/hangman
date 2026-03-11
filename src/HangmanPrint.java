public class HangmanPrint {
    public static void drawHangman(int mistakes) {
        switch (mistakes) {
            case 0:
                System.out.println("""
                        +---+
                        |   |
                        |
                        |
                        |
                        |
                        =======""");
                break;

            case 1:
                System.out.println("""
                        +---+
                        |   |
                        |   ☺
                        |
                        |
                        |
                        =======""");
                break;

            case 2:
                System.out.println("""
                        +---+
                        |   |
                        |   ☺
                        |   |
                        |
                        |
                        =======""");
                break;

            case 3:
                System.out.println("""
                        +---+
                        |   |
                        |   ☺
                        |   |\\
                        |
                        |
                        =======""");
                break;

            case 4:
                System.out.println("""
                        +---+
                        |   |
                        |   ☺
                        |  /|\\
                        |
                        |
                        =======""");
                break;

            case 5:
                System.out.println("""
                        +---+
                        |   |
                        |   ☺
                        |  /|\\
                        |    \\
                        |
                        =======""");
                break;

            case 6:
                System.out.println("""
                        +---+
                        |   |
                        |   ☺
                        |  /|\\
                        |  / \\
                        |
                        =======""");
                break;
        }
    }
}
