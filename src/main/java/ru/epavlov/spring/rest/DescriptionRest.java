package ru.epavlov.spring.rest;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.epavlov.desc.Description;
import ru.epavlov.entity.Desc_Entity;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Eugene on 21.03.2017.
 */
@org.springframework.web.bind.annotation.RestController
public class DescriptionRest {
    private Description description = new Description();

    @PostMapping("description")
    public Response getDesc(@RequestParam(value = "action", defaultValue = "getList") String action,
                            @RequestParam(value = "name", defaultValue = "") String name,
                            @RequestParam(value = "text", defaultValue = "") String text){
        if (action.equals("getList")){
            return Response.ok().entity(description.getFiles()).build(); // возвращаем список описаний
        }
        if (action.equals("get")&& !name.equals("")){ //получаем список значений из файла
            ArrayList<Desc_Entity> list = description.getDesc(name);
            return Response.ok().entity(description.getDesc(name)).build();
        }
        if (action.equals("raw") && description.getFiles().contains(name)){
            return Response.ok().entity(description.getRaw(name)).build();
        }
        if (action.equals("delete") && description.getFiles().contains(name)){ //удаляем файл
            return Response.ok().entity(description.delete(name)).build();
        }
        if (action.equals("create") && !description.getFiles().contains(name)){ //создаем пустой файл
            return Response.ok().entity(description.createEmptyFile(name)).build();
        }
        // System.out.println(action+" "+name+" "+text);
        if (action.equals("edit") && description.getFiles().contains(name) && !text.equals("")){
            return Response.ok().entity(description.edit(name,text)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @PostMapping("/addDesc")
    public Response addDesc(@RequestParam("file") MultipartFile multipartFile,
                            @RequestParam(value = "name", defaultValue = "") String name ) throws IOException {
        if (!description.getFiles().contains(name) && !name.equals("")){
            description.add(multipartFile.getBytes(),name);
            return  Response.ok().entity(description.getFiles()).build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }
}
