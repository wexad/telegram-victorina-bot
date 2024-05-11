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

    public FileManager(String filePath) {
        this.FILE_PATH = filePath;
        this.GSON = new GsonBuilder().setDateFormat("dd/MM/yyyy").setPrettyPrinting().create();

    }

    public synchronized void write(List<M> list, Class<M> c) {
        try {
            Type type = TypeToken.getParameterized(List.class, c).getType();
            String json = GSON.toJson(list, type);
            Files.writeString(Path.of(this.FILE_PATH), json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized List<M> load(Class<M> c) {
        Type type = TypeToken.getParameterized(List.class, c).getType();
        List<M> result = new ArrayList<>();
        try {
            String json = Files.readString(Path.of(this.FILE_PATH));
            ArrayList<M> list = GSON.fromJson(json, type);
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
