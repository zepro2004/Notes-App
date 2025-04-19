package ToDo;
public class ToDo {
    private String taskDescription;
    private String endDate;
    private boolean isCompleted;

    public ToDo(String taskDescription, String endDate) {
        this.taskDescription = taskDescription;
        this.endDate = endDate;
        this.isCompleted = false;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markAsCompleted() {
        this.isCompleted = true;
    }
}
