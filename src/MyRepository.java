import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * MyRepository.java
 * Created by Imanuel Kurniawan
 */

public class MyRepository {
    private final int[] CHECKSUM = {1, 3, 11, 17};
    private Date timeStamp;

    //This function will create a new repository in the target directory
    public boolean createRepo(String sourcePath, String targetPath)
    {
        timeStamp = new Date();
        String fileName = generateManifestName();
        createManifestFile("CREATE", fileName, sourcePath, targetPath);
        return copyContent(new RepoFolder(sourcePath), targetPath, new File(targetPath + "/Activity/" + fileName));
    }

    //This function will copy the content from source directory to the target directory
    //and update the manifest file
    public boolean copyContent(RepoFolder rootPar, String targetPath, File manifestPar)
    {
        boolean generateRepo = true;    //Error flag
        //Copy All Sub Directories
        if(rootPar.hasSubDirectories())
        {
            //Get each sub directory
            for (File temp : rootPar.getSubDirectories())
            {
                //Create a new directory in the target folder
                if(!new File(targetPath + "/" + temp.getName()).mkdir())
                    generateRepo = false;
                //Copy the content of the sub directory
                if(!copyContent(new RepoFolder(temp), targetPath + "/" + temp.getName(), manifestPar))
                    generateRepo = false;
            }//End for loop
        }//End if

        //Copy All Files
        if(rootPar.hasFiles())
        {
            String fileName = "", text = "", fileExt = "";
            FileInputStream fileReader;
            PrintWriter outputFile;
            int artifactId, counter, buf;

            try {
                //Get each file
                for (File temp : rootPar.getFiles())
                {
                    if(!temp.isHidden()) {
                        artifactId = 0;
                        counter = 0;
                        text = "";
                        fileName = temp.getName();
                        fileExt = fileName.substring(fileName.indexOf('.'));
                        fileReader = new FileInputStream(temp);


                        //Copy the content of the file and calculate the artifact ID
                        while (fileReader.available() > 0) {
                            buf = fileReader.read();
                            artifactId += CHECKSUM[counter++ % 4] * buf;
                            text += (char) buf;
                        }//End while loop

                        //Create new leaf folder
                        if (!new File(targetPath + "/" + fileName).mkdir())
                            generateRepo = false;

                        //Copy the file into new leaf folder
                        outputFile = new PrintWriter(targetPath + "/" + fileName +
                                     "/" + artifactId + "." + counter +
                                     fileExt);
                        outputFile.print(text);

                        //Update manifest file
                        text = "Copy \"" + fileName + "\" to \"" + targetPath + "/" + fileName + "\" (" +
                                "Artifact ID = " + artifactId + ")";
                        updateManifestFile(text, manifestPar);

                        //Close all I/O Streamers
                        fileReader.close();
                        outputFile.close();
                    }
                }//End for loop
            } catch (IOException e)
            {
                System.out.println("An error occurred while copying " + fileName + ".");
            }//End catch
        }//End if
        return generateRepo;
    }//End createRepo()

    //This function will create a new manifest file for a specific command
    public void createManifestFile(String command, String fileName, String sourcePath, String targetPath)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        //Create a new activity folder
        if(!new File(targetPath + "/Activity").exists())
            new File(targetPath + "/Activity").mkdir();

        //Create a new manifest file
        try {
            PrintWriter outputFile = new PrintWriter(targetPath + "/Activity/" + fileName);
            outputFile.println(fileName + "\n");
            outputFile.println("CKC - " + dateFormat.format(timeStamp));
            outputFile.println("Command: " + command + " \"" + sourcePath + "\" \"" + targetPath + "\"");
            outputFile.println("Source Directory: \"" + sourcePath + "\"");
            outputFile.println("Target Directory: \"" + targetPath + "\"\n");
            outputFile.close();
        }catch (IOException e)
        {
            System.out.println("An error occurred while generating manifest file.");
        }//End try catch
    }//End createManifestFile()

    //This function is used to update the existing manifest file
    public void updateManifestFile(String newActivity, File manifestFile)
    {
        String existingText = "";
        try {
            //Read the manifest file
            Scanner fileReader = new Scanner(manifestFile);
            while (fileReader.hasNextLine())
            {
                existingText += fileReader.nextLine() + "\n";
            }

            //Update manifest file
            PrintWriter fileWriter = new PrintWriter(manifestFile);
            fileWriter.println(existingText + "" + newActivity);

            //Close I/O Streamers
            fileReader.close();
            fileWriter.close();
        } catch (FileNotFoundException e)
        {
            System.out.println("Manifest file does not exist.");
        }//End try catch
    }//End updateManifestFile

    //This function will generate a name for the new manifest file
    public String generateManifestName()
    {
        int year = timeStamp.getYear() + 1900;
        int month = timeStamp.getMonth() + 1;
        int date = timeStamp.getDate();
        int hour = timeStamp.getHours();
        int minute = timeStamp.getMinutes();

        String fileName = "MANIFEST_" +
                year + String.format("%02d", month) + String.format("%02d", date) +
                "_" + String.format("%02d", hour) + String.format("%02d", minute) + ".txt";

        return fileName;
    }//End generateManifestName
}//End class
