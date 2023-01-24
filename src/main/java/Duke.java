import java.util.ArrayList;
import java.util.Scanner;

public class Duke {
    private static final String DIVIDER_LINE = "____________________________________________________\n";
    private static ArrayList<Task> TASKS = new ArrayList<Task>();

    private static void displayIntro() {
        System.out.println(reply("Hello! I'm Duke\n What can I do for you?" + "\n"));
    }

    private static void displayOutro() {
        System.out.println(reply("Bye. Hope to see you again soon!" + "\n"));
    }

    private static void addTask(Task newTask) {
        TASKS.add(newTask);
        System.out.println(reply("Got it. I've added this task:\n  "
                + newTask
                + "\nNow you have " + TASKS.size() + " tasks in the list\n"));
    }

    private static void mark(String command) {
        int index = Integer.valueOf(command.substring(5)) - 1;
        Task target = TASKS.get(index);
        target.mark();
        System.out.println(reply("Nice! I've marked this task as above:\n  " + target + "\n"));
    }

    private static void unmark(String command) {
        int index = Integer.valueOf(command.substring(7)) - 1;
        Task target = TASKS.get(index);
        target.unmark();
        System.out.println(reply("OK, I've marked this task as not done yet:\n  " + target + "\n"));
    }

    private static void displayTasks() {
        String list_of_tasks = "";
        Task currentTask;
        for (int i = 0; i < TASKS.size(); i++) {
            currentTask = TASKS.get(i);
            list_of_tasks += i + 1 + "." + currentTask.toString() + "\n";
        }
        System.out.println(reply(list_of_tasks));
    }

    private static void handleTaskInput(Scanner input) {
        String command = input.nextLine();
        while (true) {
            if (command.equals("bye")) {
                break;
            } else if (command.equals("list")) {
                displayTasks();
                command = input.nextLine();
                continue;
            }

            if (command.startsWith("mark ")) {
                mark(command);
                command = input.nextLine();
                continue;
            } else if (command.startsWith("unmark ")) {
                unmark(command);
                command = input.nextLine();
                continue;
            }

            String description = command;
            Task newTask = new Task(command);
            try {
                if (command.startsWith("todo")) {
                    if (description.length() <= 5) {
                        throw new DukeException(reply("The description of a todo cannot be empty.\n"));
                    }
                    newTask = new ToDo(description.substring(5));
                } else if (command.startsWith("deadline")) {
                    if (description.length() <= 9) {
                        throw new DukeException(reply("The description of a deadline cannot be empty.\n"));
                    }
                    String by = description.substring(description.indexOf(" /by ") + 5);
                    description = description.substring(9, description.indexOf(" /by "));
                    newTask = new Deadline(description, by);
                } else if (command.startsWith("event")) {
                    if (description.length() <= 6) {
                        throw new DukeException(reply("The description of a event cannot be empty.\n"));
                    }
                    String start = description.substring(description.indexOf(" /from ") + 7,
                            description.indexOf(" /to "));
                    String end = description.substring(description.indexOf(" /to ") + 5);
                    description = description.substring(6, description.indexOf(" /from "));
                    newTask = new Event(description, start, end);
                } else {
                    throw new DukeException(reply("I'm sorry, but I don't know what that means :-(\n"));
                }
                if (!TASKS.contains(newTask)) {
                    addTask(newTask);
                }
            } catch (DukeException e) {
                System.out.println(e);
            } finally {
                command = input.nextLine();
            }
        }
    }

    private static String reply(String command) {
        return DIVIDER_LINE + command + DIVIDER_LINE;
    }

    public static void main(String[] args) {
        displayIntro();
        Scanner input = new Scanner(System.in);
        handleTaskInput(input);
        displayOutro();
    }
}
