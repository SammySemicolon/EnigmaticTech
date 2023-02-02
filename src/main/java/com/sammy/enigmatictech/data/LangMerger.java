package com.sammy.enigmatictech.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.sammy.enigmatictech.EnigmaticTechMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.data.LanguageProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class LangMerger extends LanguageProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private final DataGenerator gen;

    public LangMerger(DataGenerator gen) {
        super(gen, EnigmaticTechMod.MODID, "en_us");
        this.gen = gen;
    }

    @Override
    protected void addTranslations() {
        Path path = gen.getOutputFolder().resolve("assets/" + EnigmaticTechMod.MODID + "/lang/" + "en_us.json");

        try {
            collectExistingEntries(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addAll(fromResource("assets/" + EnigmaticTechMod.MODID + "/lang/extra_lang.json").getAsJsonObject());
    }

    private void collectExistingEntries(Path path) throws IOException {
        if (!Files.exists(path)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            addAll(GsonHelper.fromJson(GSON, reader, JsonObject.class));
            reader.close();
        }
    }

    private void addAll(JsonObject jsonObject) {
        jsonObject.entrySet().forEach(e -> {
            String key = e.getKey();
            String value = e.getValue().getAsString();
            add(key, value);
        });
    }

    private JsonElement fromResource(String filepath) {
        JsonElement element = loadJsonResource(filepath);
        if (element == null)
            throw new IllegalStateException(String.format("Could not find default lang file: %s", filepath));
        return element;
    }

    public static JsonElement loadJsonResource(String filepath) {
        return loadJson(ClassLoader.getSystemResourceAsStream(filepath));
    }

    private static JsonElement loadJson(InputStream inputStream) {
        try {
            JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
            reader.setLenient(true);
            JsonElement element = Streams.parse(reader);
            reader.close();
            inputStream.close();
            return element;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}