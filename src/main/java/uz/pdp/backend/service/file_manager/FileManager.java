package uz.pdp.backend.service.file_manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager<M> {

    private final String FIlE_PATH;
    private final Gson GSON;

    public FileManager(String filePath) {
        this.FIlE_PATH = filePath;
        this.GSON = (new GsonBuilder()).setDateFormat("dd/MM/yyyy").setPrettyPrinting().create();
    }

    public void write(List<M> list) {
        String json = this.GSON.toJson(list);

        try {
            Files.writeString(Path.of(this.FIlE_PATH), json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<M> load() {
        List<M> result = new ArrayList<>();

        try {
            String jsonModel = Files.readString(Path.of(this.FIlE_PATH));
            Type type = (new TypeToken<List<M>>() {
            }).getType();
            List<M> list = (List<M>) this.GSON.fromJson(jsonModel, type);
            if (list == null) {
                list = new ArrayList<>();
            }

            return (List<M>) list;
        } catch (IOException e) {
            System.out.println("Something in loading data wrong! ");
        }
        return result;
    }
}
