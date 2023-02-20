package duke;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * duke.TaskList class encapsulates the information of the tasks keyed in the chatbot by the user.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList(Storage storage) {
        this.tasks = new ArrayList<Task>();
    }

    /**
     * Returns true if the task argument is in the task list and false otherwise.
     *
     * @param task a duke.Task object encapsulate the task we are checking.
     * @return Returns true if the task argument is in the task list and false otherwise.
     */
    public boolean contains(Task task) {
        return this.tasks.contains(task);
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Add the task argument into the task list.
     *
     * @param newTask a duke.Task object encapsulating the new task being added.
     */
    public String addTask(Task newTask) {
        int size = tasks.size();
        tasks.add(newTask);
        Storage.writeFile(tasks);
        return Ui.getAddTaskMessage(newTask, size + 1);
    }

    /**
     * Mark the task specified in the input of user to task list.
     *
     * @param command User input specifying which task to mark.
     */
    public String mark(String command) {
        int index = Integer.valueOf(command.substring(5)) - 1;
        Task target = tasks.get(index);
        target.mark();
        Storage.writeFile(tasks);
        return Ui.getMarkTaskMessage(target);
    }

    /**
     * Unmark the task specified in the input of user to task list.
     *
     * @param command User input specifying which task to unmark.
     */
    public String unmark(String command) {
        int index = Integer.valueOf(command.substring(7)) - 1;
        Task target = tasks.get(index);
        target.unmark();
        Storage.writeFile(tasks);
        return Ui.getUnmarkTaskMessage(target);
    }

    /**
     * Delete the task specified in the input of user from the task list.
     *
     * @param command User input specifying which task to delete.
     */
    public String delete(String command) {
        int index = Integer.valueOf(command.substring(7)) - 1;
        int size = tasks.size();
        Task target = tasks.get(index);
        tasks.remove(index);
        Storage.writeFile(tasks);
        return Ui.getDeleteTaskMessage(target, size - 1);
    }

    /**
     * Find the task specified in the input of the user from the task list.
     *
     * @param command User input specifying which task keyword to find.
     */
    public String find(String command) {
        String keyWord = command.substring(5);
        Predicate<Task> keyWordFilter = task -> (task.getDescription().contains(keyWord));
        Stream<Task> resultedTasks = tasks.stream().filter(keyWordFilter);
        return Ui.getFindTaskMessage(resultedTasks);
    }
}
