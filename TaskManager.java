import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

// Task class
class Task {
    String title;
    boolean completed;

    Task(String title) {
        this.title = title;
        this.completed = false;
    }

    // Display-friendly string for console
    String display() {
        return title + " | " + (completed ? "Completed" : "Not Completed");
    }

    // Save-friendly string for file (human-readable)
    String saveString() {
        return display();
    }

    // Create Task from saved string safely
    static Task deserialize(String data) {
        if(data == null || !data.contains(" | ")) {
            // skip invalid lines
            return null;
        }
        String[] parts = data.split(" \\| "); // split by " | "
        Task t = new Task(parts[0]);
        t.completed = parts[1].equalsIgnoreCase("Completed");
        return t;
    }
}

// Main class
public class TaskManager {
    static final String FILE_NAME = "tasks.txt";

    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();
        loadTasks(tasks);

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while(running) {
            System.out.println("\n1. Add Task  2. View Tasks  3. Toggle Task Done/Undone  4. Remove Task  5. Exit");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch(choice) {
                case 1: // Add task
                    System.out.print("Task Title: ");
                    String title = sc.nextLine().trim(); // trim extra spaces
                    if(title.isEmpty()) { // prevent empty titles
                        System.out.println("Task title cannot be empty!");
                        break;
                    }
                    tasks.add(new Task(title));
                    saveTasks(tasks);
                    break;

                case 2: // View tasks
                    if(tasks.isEmpty()) {
                        System.out.println("No tasks found.");
                    } else {
                        for(int i = 0; i < tasks.size(); i++) {
                            System.out.println((i+1) + ". " + tasks.get(i).display());
                        }
                    }
                    break;

                case 3: // Toggle Completed
                    if(tasks.isEmpty()) {
                        System.out.println("No tasks to toggle.");
                        break;
                    }
                    System.out.print("Task Number to Toggle Done/Undone: ");
                    int toggleIndex = sc.nextInt() - 1;
                    if(toggleIndex >= 0 && toggleIndex < tasks.size()) {
                        Task t = tasks.get(toggleIndex);
                        t.completed = !t.completed; // toggle
                        System.out.println("Task updated: " + t.display());
                        saveTasks(tasks);
                    } else {
                        System.out.println("Invalid task number!");
                    }
                    break;

                case 4: // Remove task
                    if(tasks.isEmpty()) {
                        System.out.println("No tasks to remove.");
                        break;
                    }
                    System.out.print("Task Number to Remove: ");
                    int removeIndex = sc.nextInt() - 1;
                    if(removeIndex >= 0 && removeIndex < tasks.size()) {
                        tasks.remove(removeIndex);
                        saveTasks(tasks);
                        System.out.println("Task removed.");
                    } else {
                        System.out.println("Invalid task number!");
                    }
                    break;

                case 5: // Exit
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        }

        sc.close();
    }

    // Save tasks to a human-readable file
    static void saveTasks(ArrayList<Task> tasks) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for(Task t : tasks) {
                pw.println(t.saveString());
            }
        } catch(IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    // Load tasks from human-readable file safely
    static void loadTasks(ArrayList<Task> tasks) {
        File f = new File(FILE_NAME);
        if(!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while((line = br.readLine()) != null) {
                Task t = Task.deserialize(line);
                if(t != null) tasks.add(t);
            }
        } catch(IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }
}