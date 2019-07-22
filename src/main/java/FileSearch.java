
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearch {

    private String fileNameToSearchIfContains;
    private List<String> result = new ArrayList<String>();

    public String getFileNameToSearchIfContains() {
        return fileNameToSearchIfContains;
    }

    public void setFileNameToSearchIfContains(String fileNameToSearchIfContains) {
        this.fileNameToSearchIfContains = fileNameToSearchIfContains;
    }

    public List<String> getResult() {
        return result;
    }

    public void searchDirectory(File directory, String fileNameToSearch) {

        setFileNameToSearchIfContains(fileNameToSearch);

        if (directory.isDirectory()) {
            search(directory);
        } else {
            System.out.println(directory.getAbsoluteFile() + " is not a directory!");
        }

    }

    private void search(File file) {

        if (file.isDirectory()) {
            //System.out.println("Searching directory ... " + file.getAbsoluteFile());

            //do you have permission to read this directory?
            if (file.canRead()) {
                for (File temp : file.listFiles()) {
                    if (temp.isDirectory()) {
                        search(temp);
                    } else {
                        if (temp.getName().toLowerCase().contains(getFileNameToSearchIfContains())) {
                            result.add(temp.getAbsoluteFile().toString());
                        }

                    }
                }

            } else {
                System.out.println(file.getAbsoluteFile() + "Permission Denied");
            }
        }

    }

}