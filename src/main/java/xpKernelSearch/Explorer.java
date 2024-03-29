package xpKernelSearch;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Explorer {
    private File root;
    public Explorer(String path) throws Exception {
        this.root= new File(path);
        if(!this.root.isDirectory())
            throw new Exception();
    }
    public Explorer(URI uri) throws Exception {
        this.root= new File(uri);
        if(!this.root.isDirectory())
            throw new Exception();
    }

    public List<File> retrieveFiles() throws Exception {
        List<File> lis= new ArrayList<>();
        for (File f: root.listFiles()){
            if (!f.canWrite()|| f.isHidden())
                continue;
            if(f.isFile()){
                lis.add(f);
            }
            if (f.isDirectory()){
                for (File fl: new Explorer(f.getAbsolutePath()).retrieveFiles()){
                    lis.add(fl);
                }
            }
        }
        return lis;
    }
    public List<File> retrieveFiles(String[] nameOfFiles) throws Exception {
        List<File> lis= new ArrayList<>();
        for (File f: root.listFiles()){
            if (!f.canWrite()|| f.isHidden())
                continue;
            if(f.isFile() && Arrays.stream(nameOfFiles).anyMatch(name -> name.equals(f.getName()))) {
                lis.add(f);
            }
            if (f.isDirectory()){
                for (File fl: new Explorer(f.getAbsolutePath()).retrieveFiles(nameOfFiles)){
                    lis.add(fl);
                }
            }
        }
        return lis;
    }

    public void printAll () throws Exception {
        for (File f: this.retrieveFiles())
            System.out.println(f.getName());
    }
}
