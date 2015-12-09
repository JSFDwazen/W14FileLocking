package w14filelocking;


import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jsf3
 */
public class WatchDir {

    public static void main(String[] args) {
        final WatchService watchService;
        Path dir = Paths.get("/media/Fractal/");
        WatchKey key;
        
        try{
            watchService = FileSystems.getDefault().newWatchService();
            dir.register(watchService,ENTRY_MODIFY);
            
            while(true){
                key=watchService.take();
                for(WatchEvent<?> watchEvent : key.pollEvents()){
                    WatchEvent<Path> ev = (WatchEvent<Path>) watchEvent;
                    Path fileName = ev.context();
                    Path child = dir.resolve(fileName);
                    WatchEvent.Kind kind = ev.kind();
                    if(kind == ENTRY_MODIFY){
                        System.out.println(child + " modified");   
                    }
                }
            }       
        }
        catch(Exception e){
            
        }
    }

}
