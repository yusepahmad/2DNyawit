package com.uph_lpjk.sawit2d.achievement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.uph_lpjk.sawit2d.controller.UserInterface;

public class AchievementManager {

    public static final String FIRST_BUY = "FIRST_BUY";
    public static final String LOUDSPEAKER_OWNER = "LOUDSPEAKER_OWNER";
    public static final String LOUDSPEAKER_USED = "LOUDSPEAKER_USED";
    public static final String MARKET_VISITOR = "MARKET_VISITOR";
    public static final String SURVIVOR = "SURVIVOR";
    public static final String RICH_PLAYER = "RICH_PLAYER";
    public static final String FIRST_HARVEST = "FIRST_HARVEST";
    public static final String LUMBERJACK = "LUMBERJACK";
    public static final String FIREBREAKER = "FIREBREAKER";

    private final Map<String, Achievement> achievements = new LinkedHashMap<>();
    private UserInterface ui;

    private int marketVisits = 0;
    private long survivalStartMillis = 0;
    private boolean survivalTimerRunning = false;

    private static final String SAVE_FILE =
            System.getProperty("user.home") + "/.sawit2d_achievements.dat";

    public AchievementManager() {
        register(
                FIRST_BUY,
                "Pedagang Pemula",
                "Membeli item pertama dari pasar",
                "Ada tempat di mana kamu bisa membeli sesuatu...");
        register(
                LOUDSPEAKER_OWNER,
                "Provokator Handal",
                "Membeli Loudspeaker di pasar",
                "Cek rak paling mahal di pasar.");
        register(
                LOUDSPEAKER_USED,
                "Hidup Jokowi!",
                "Menggunakan Loudspeaker (F atau G)",
                "Kalau punya barang aneh, coba tekan tombol aksi.");
        register(
                MARKET_VISITOR,
                "Pelanggan Setia",
                "Mengunjungi pasar sebanyak 10 kali",
                "Pasar tak pernah menolak kedatanganmu.");
        register(
                SURVIVOR,
                "Tahan Banting",
                "Bertahan selama 5 menit dalam satu sesi",
                "Jangan mati terlalu cepat.");
        register(
                RICH_PLAYER,
                "Juragan",
                "Memiliki 10000 gold sekaligus",
                "Kumpulkan, jangan langsung dihabiskan.");
        register(
                FIRST_HARVEST,
                "Petani Perdana",
                "Memanen sawit untuk pertama kali",
                "Sawit yang sudah matang menunggu tanganmu.");
        register(
                LUMBERJACK,
                "Tukang Tebang",
                "Menebang 5 pohon dalam satu sesi",
                "Ada banyak pohon di area kebun, coba tebang...");
        register(
                FIREBREAKER,
                "Pemadam Handal",
                "Menangani kebakaran pertama",
                "Api bisa muncul kapan saja, siapkan dirimu.");

        load();
    }

    public void setUI(UserInterface ui) {
        this.ui = ui;
    }

    private void register(String id, String name, String desc, String clue) {
        achievements.put(id, new Achievement(id, name, desc, clue));
    }

    public Collection<Achievement> getAll() {
        return achievements.values();
    }

    public Achievement get(String id) {
        return achievements.get(id);
    }

    public void unlock(String id) {
        Achievement a = achievements.get(id);
        if (a == null || a.isUnlocked()) return;
        a.unlock();
        save();
        if (ui != null) {
            ui.pushBanner(
                    UserInterface.BannerTone.SUCCESS,
                    "Achievement Unlocked!",
                    a.name + " — " + a.description);
        }
    }

    

    public void onMarketVisit() {
        marketVisits++;
        if (marketVisits >= 10) unlock(MARKET_VISITOR);
    }

    public void onItemBought() {
        unlock(FIRST_BUY);
    }

    public void onLoudspeakerBought() {
        unlock(LOUDSPEAKER_OWNER);
        onItemBought();
    }

    public void onLoudspeakerUsed() {
        unlock(LOUDSPEAKER_USED);
    }

    public void onHarvest() {
        unlock(FIRST_HARVEST);
    }

    public void onTreeChopped() {
        
    }

    public void onTreeChoppedCount(int total) {
        if (total >= 5) unlock(LUMBERJACK);
    }

    public void onFireHandled() {
        unlock(FIREBREAKER);
    }

    public void onGoldCheck(int gold) {
        if (gold >= 10000) unlock(RICH_PLAYER);
    }

    public void startSurvivalTimer() {
        if (!survivalTimerRunning) {
            survivalStartMillis = System.currentTimeMillis();
            survivalTimerRunning = true;
        }
    }

    public void checkSurvivalTimer() {
        if (!survivalTimerRunning) return;
        long elapsed = System.currentTimeMillis() - survivalStartMillis;
        if (elapsed >= 5 * 60 * 1000L) {
            unlock(SURVIVOR);
        }
    }

    public void resetSession() {
        marketVisits = 0;
        survivalTimerRunning = false;
        survivalStartMillis = 0;
    }

    /**
     * Hapus semua progress achievement: reset unlock + hapus file save. Dipanggil saat New Game.
     */
    public void resetAll() {
        for (Achievement a : achievements.values()) {
            a.setUnlocked(false);
        }
        resetSession();
        new java.io.File(SAVE_FILE).delete();
    }

    

    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SAVE_FILE))) {
            for (Achievement a : achievements.values()) {
                if (a.isUnlocked()) {
                    pw.println(a.id);
                }
            }
        } catch (IOException e) {
            
        }
    }

    private void load() {
        try {
            File f = new File(SAVE_FILE);
            if (!f.exists()) return;
            List<String> lines = Files.readAllLines(f.toPath());
            for (String line : lines) {
                String id = line.trim();
                if (achievements.containsKey(id)) {
                    achievements.get(id).setUnlocked(true);
                }
            }
        } catch (IOException e) {
            
        }
    }
}
