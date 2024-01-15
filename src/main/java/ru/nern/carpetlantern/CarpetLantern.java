package ru.nern.carpetlantern;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class CarpetLantern implements CarpetExtension, ModInitializer {
	@Override
	public void onInitialize() {
		CarpetServer.manageExtension(new CarpetLantern());
	}

	@Override
	public String version() {
		return "carpet-lantern";
	}

	@Override
	public Map<String, String> canHasTranslations(String lang) {
		InputStream langFile = CarpetLantern.class.getClassLoader().getResourceAsStream("assets/carpetlantern/lang/%s.json".formatted(lang));
		if (langFile == null) {
			// we don't have that language
			return Collections.emptyMap();
		}
		String jsonData;
		try {
			jsonData = IOUtils.toString(langFile, StandardCharsets.UTF_8);
		} catch (IOException e) {
			return Collections.emptyMap();
		}
		Gson gson = new GsonBuilder().setLenient().create(); // lenient allows for comments
		return gson.fromJson(jsonData, new TypeToken<Map<String, String>>() {}.getType());
	}

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(CarpetLanternSettings.class);
	}
}
