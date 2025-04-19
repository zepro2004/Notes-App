import Layout.Layout;
import Database.DBInitializer;

public class App {
    public static void main(String[] args) throws Exception {
        DBInitializer.initializeDatabase();
        new Layout();
    }
}
