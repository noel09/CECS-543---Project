import java.util.Scanner;

/**
 * RepoTest.java
 * Created by Imanuel Kurniawan
 */
public class RepoTest {

    public void runCreateRepo()
    {
        //Declarations
        MyRepository myRepo = new MyRepository();
        Scanner keyboardReader = new Scanner(System.in);
        String command = "", source, target;

        //Get User Input
        System.out.print("Enter command: ");  //create
        command = keyboardReader.nextLine();

        if(command.toUpperCase().equals("CREATE"))
        {
            System.out.print("Enter Source Path: ");  //Test Folders/Source 3
            source = keyboardReader.nextLine();
            System.out.print("Enter Target Path: ");  //Test Folders/Target 3
            target = keyboardReader.nextLine();

            if(myRepo.createRepo(source, target))
                System.out.println("Success!");
            else
                System.out.println("Fails!");
        }
        else
            System.out.println("Unrecognized Command!");
    }
}
