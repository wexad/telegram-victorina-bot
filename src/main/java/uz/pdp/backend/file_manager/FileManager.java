package uz.pdp.backend.file_manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import uz.pdp.backend.model.bot_user.BotUser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager<M> {

    private final String FILE_PATH;
    private final Gson GSON;
    private final Type TYPE;

    public FileManager(String filePath) {
        this.FILE_PATH = filePath;
        this.GSON = new GsonBuilder().setDateFormat("dd/MM/yyyy").setPrettyPrinting().create();
        this.TYPE = new TypeToken<List<M>>() {
        }.getType();
    }

    public void write(List<M> list) {
        try {
            String json = GSON.toJson(list, TYPE);
            Files.writeString(Path.of(this.FILE_PATH), json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<M> load() {
        List<M> result = new ArrayList<>();
        try {
            String json = Files.readString(Path.of(this.FILE_PATH));
            ArrayList<M> list = GSON.fromJson(json, TYPE);
            if (list != null) {
                for (M m : list) {
                    if (m != null) {
                        result.add(m);
                    }
                }
            }
            return result;
        } catch (IOException e) {
            System.out.println("Something in loading data wrong! ");
        }
        return result;
    }
}
