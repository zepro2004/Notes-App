import Database.DBInitializer;
import Layout.MainLayout;

public class App {
    public static void main(String[] args) throws Exception {
        DBInitializer.initializeDatabase();
        new MainLayout();
    }
}
