package ru.epavlov.spring.rest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.epavlov.avrdude.Sketch;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Eugene on 21.03.2017.
 */
@org.springframework.web.bind.annotation.RestController
public class SketchRest {
    private static final Logger log = Logger.getLogger(SketchRest.class);
    private Sketch sketch = new Sketch();

    @PostMapping(value = "/sketch")
    public Response sketch(@RequestParam(value = "name", defaultValue = "") String name,
                           @RequestParam(value = "action", defaultValue = "") String action,
                           @RequestParam(value = "port", defaultValue = "") String port,
                           @RequestParam(value = "plata", defaultValue = "mega") String plata) {

        if (action.equals("list")) {
            return Response.ok().entity(sketch.getFiles()).build();
        }
        if (action.equals("inProgress")) //возвращаем грузим мы или нет
            return Response.ok().entity(sketch.isUploading()).build();
        if (action.equals("logs")) {
            return Response.ok().entity(sketch.getLogs()).build();
        }
        if (action.equals("upload") && sketch.getFiles().contains(name) && !port.equals("")) {
            sketch.upload(name, port, plata);
            return Response.ok().build();
        }
        if (action.equals("stop")) {
            sketch.stop();
            return Response.ok().build();
        }
        if (action.equals("clear")) {
            sketch.getLogs().clear();
            return Response.ok().build();
        }

        return Response.noContent().build();
    }

    @PostMapping(value = "/sketch/load")
    public Response uploadSketch(@RequestParam(value = "file") MultipartFile file) {
        log.warn("[/sketch/load]:: "+file.getOriginalFilename()+": "+file.getSize());
        File f = new File(Sketch.sketch + file.getOriginalFilename());
        try {
            Files.write(f.toPath(), file.getBytes());
        } catch (IOException e) {
            log.error(e.toString());
            return Response.notAcceptable(null).build();
        }
        return Response.ok().build();
    }
    @PostMapping(value = "/sketch/delete")
    public Response deleteSketch(@RequestParam(value = "filename") String filename){
        log.warn("[/sketch/delete]::"+filename);
        if (sketch.getFiles().contains(filename)) sketch.delete(filename);
        return Response.ok().build();
    }

}
