import java.io.File;
import java.util.ArrayList;

/**
 * RepoFolder.java
 * Created by Imanuel Kurniawan
 */

public class RepoFolder {
    private File rootDirectory;
    private ArrayList<File> subDirectories = new ArrayList<File>();
    private ArrayList<File> files = new ArrayList<File>();

    //Public constructor that takes the name of the folder
    public RepoFolder(String rootFolderName)
    {
        rootDirectory = new File(rootFolderName);
        initializeContent();
    }//End constructor

    //Public constructor that takes an object of File type
    public RepoFolder(File rootPar)
    {
        rootDirectory = rootPar;
        initializeContent();
    }//End constructor

    //Get all files and folders inside the root folder
    private void initializeContent()
    {
        //Store all content in the file into an array
        File[] content = rootDirectory.listFiles();
        //Determine if the content is a file or a folder
        for (int counter = 0; counter < content.length; counter++) {
            if (content[counter].isDirectory())
                subDirectories.add(content[counter]);
            else
                files.add(content[counter]);
        }//End for loop
    }//End initializeContent()

    public boolean hasSubDirectories()
    {
        if(!subDirectories.isEmpty())
            return true;
        else
            return false;
    }//End subDirectories()

    public boolean hasFiles()
    {
        if(!files.isEmpty())
            return true;
        else
            return false;
    }//End hasFiles()

    public File getRootDirectory()
    {
        return rootDirectory;
    }//End getRootDirectory()

    public ArrayList<File> getSubDirectories()
    {
        return subDirectories;
    }//End getSubDirectories()

    public ArrayList<File> getFiles()
    {
        return files;
    }//End getFiles()
}//End class
