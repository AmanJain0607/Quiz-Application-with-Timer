import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizGame {

    static String[][] quizData = {
        {"What is the capital of France?", "A. Paris", "B. London", "C. Rome", "D. Madrid", "A"},
        {"What is the largest planet in the solar system?", "A. Earth", "B. Jupiter", "C. Saturn", "D. Venus", "B"},
        {"Who wrote 'Hamlet'?", "A. Charles Dickens", "B. J.K. Rowling", "C. William Shakespeare", "D. Mark Twain", "C"}
    };

    static int score = 0;
    static boolean answerSubmitted = false;
    static int timeLeft;

    static class Result {
        String question;
        String userAnswer;
        String correctAnswer;
        boolean isCorrect;

        Result(String question, String userAnswer, String correctAnswer, boolean isCorrect) {
            this.question = question;
            this.userAnswer = userAnswer;
            this.correctAnswer = correctAnswer;
            this.isCorrect = isCorrect;
        }
    }

    public static void main(String[] args) throws java.io.IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String userAnswer;
        List<Result> results = new ArrayList<>();

        for (int i = 0; i < quizData.length; i++) {
            answerSubmitted = false;
            timeLeft = 10;

            System.out.println("Question " + (i + 1) + ": " + quizData[i][0]);
            for (int j = 1; j <= 4; j++) {
                System.out.println(quizData[i][j]);
            }

            Thread.sleep(2000);

            Thread timerThread = new Thread(() -> {
                try {
                    for (int t = timeLeft; t >= 0; t--) {
                        if (answerSubmitted) {
                            break;
                        }
                        System.out.println(t + " seconds left");
                        Thread.sleep(1000);
                    }
                    if (!answerSubmitted) {
                        System.out.println("Time's up! Moving to the next question.");
                        answerSubmitted = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            timerThread.start();

            long startTime = System.currentTimeMillis();
            while (!answerSubmitted) {
                if (System.in.available() > 0) {
                    userAnswer = scanner.nextLine().toUpperCase().trim();
                    long elapsedTime = System.currentTimeMillis() - startTime;

                    if (elapsedTime < timeLeft * 1000 && !answerSubmitted) {
                        answerSubmitted = true;
                        if (userAnswer.equals(quizData[i][5])) {
                            System.out.println("Correct!");
                            score++;
                            results.add(new Result(quizData[i][0], userAnswer, quizData[i][5], true));
                        } else {
                            System.out.println("Incorrect!");
                            results.add(new Result(quizData[i][0], userAnswer, quizData[i][5], false));
                        }
                    }
                }
            }

            if (!answerSubmitted) {
                results.add(new Result(quizData[i][0], "No Answer", quizData[i][5], false));
            }

            timerThread.join();
            System.out.println();
        }

        System.out.println("--- Quiz Over ---");
        System.out.println("Your final score is: " + score + "/" + quizData.length);

        System.out.println("--- Summary of Answers ---");
        for (int i = 0; i < quizData.length; i++) {
            boolean found = false;
            for (Result result : results) {
                if (result.question.equals(quizData[i][0])) {
                    System.out.println("Question: " + result.question);
                    System.out.println("Your answer: " + result.userAnswer);
                    System.out.println("Correct answer: " + result.correctAnswer);
                    System.out.println(result.isCorrect ? "You got this correct!\n" : "You got this wrong.\n");
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Question: " + quizData[i][0]);
                System.out.println("Your answer: No Answer");
                System.out.println("Correct answer: " + quizData[i][5]);
                System.out.println("You got this wrong.\n");
            }
        }
    }
}
