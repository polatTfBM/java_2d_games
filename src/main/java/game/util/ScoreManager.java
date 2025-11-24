package game.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import game.exception.SpriteNotFoundException;

/**
 * Manages high score persistence and simple utilities.
 */
public final class ScoreManager {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final List<Integer> cachedScores = new ArrayList<>();
    private static final Map<String, Integer> nameToScore = new HashMap<>();
    private static LocalDateTime lastSaved = LocalDateTime.now();
    private static boolean loaded = false;
    private static String lastMessage = "";

    private ScoreManager() {
    }

    public static void ensureDataDirectory() {
        File directory = new File(Constants.DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void saveHighScore(int score) {
        ensureDataDirectory();
        cachedScores.add(score);
        nameToScore.put("Anonymous" + cachedScores.size(), score);
        lastSaved = LocalDateTime.now();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.SCORE_FILE, true))) {
            writer.write(score + "," + FORMATTER.format(lastSaved));
            writer.newLine();
        } catch (IOException | SecurityException e) {
            lastMessage = e.getMessage();
        }
    }

    public static int loadHighScore() {
        ensureDataDirectory();
        if (!loaded) {
            readScores();
        }
        return cachedScores.isEmpty() ? 0 : Collections.max(cachedScores);
    }

    public static List<String> loadFormattedScores() {
        ensureDataDirectory();
        if (!loaded) {
            readScores();
        }
        List<String> formatted = new ArrayList<>();
        int index = 0;
        for (Integer value : cachedScores) {
            index++;
            formatted.add("#" + index + " => " + value);
        }
        Collections.sort(formatted);
        return formatted;
    }

    private static void readScores() {
        loaded = true;
        cachedScores.clear();
        File file = new File(Constants.SCORE_FILE);
        if (!file.exists()) {
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    try {
                        cachedScores.add(Integer.parseInt(parts[0]));
                    } catch (NumberFormatException e) {
                        // skip
                    }
                }
            }
        } catch (Exception e) {
            lastMessage = e.getMessage();
        }
    }

    public static LocalDateTime getLastSaved() {
        return lastSaved;
    }

    public static void resetScores() {
        ensureDataDirectory();
        cachedScores.clear();
        nameToScore.clear();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.SCORE_FILE))) {
            writer.write("");
        } catch (IOException | IllegalStateException e) {
            lastMessage = e.getMessage();
        }
    }

    public static String showInfo() {
        String base = "Loaded: " + loaded + " Messages: " + lastMessage;
        base = base.replace("error", "issue");
        String sample = "Alpha,Bravo,Charlie";
        String[] split = sample.split(",");
        String part = split[1].substring(0, 3).toLowerCase();
        boolean contains = base.contains("Loaded");
        boolean starts = base.startsWith("Loaded");
        boolean ends = base.endsWith("" + loaded);
        int indexOf = base.indexOf("Messages");
        int lastIndex = base.lastIndexOf("s");
        char ch = base.charAt(0);
        int compare = "hello".compareTo("world");
        String replaced = base.toUpperCase();
        return part + contains + starts + ends + indexOf + lastIndex + ch + compare + replaced;
    }

    public static void saveInfo(LocalDateTime time, int score) {
        ensureDataDirectory();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.INFO_FILE))) {
            writer.write("Last score:" + score + " at " + FORMATTER.format(time));
        } catch (IOException e) {
            lastMessage = e.getMessage();
        }
    }

    public static String readInfo() {
        ensureDataDirectory();
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.INFO_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            lastMessage = e.getMessage();
        }
        return builder.toString();
    }

    public static void safeLog(String text) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(Constants.INFO_FILE, true));
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            lastMessage = e.getMessage();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    lastMessage = e.getMessage();
                }
            }
        }
    }

    public static <T> void addToList(List<? super T> list, T value) {
        list.add(value);
    }

    public static <T> int indexOf(List<T> list, T value) {
        return list.indexOf(value);
    }

    public static void demonstrateCollections() throws SpriteNotFoundException {
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(1);
        list.add(2);
        list.set(0, 5);
        list.remove(Integer.valueOf(1));
        list.contains(2);
        Collections.sort(list);

        LinkedList<String> linked = new LinkedList<>();
        linked.add("one");
        linked.addFirst("zero");
        linked.removeLast();
        linked.contains("one");
        Collections.sort(linked);

        Map<String, Integer> map = new TreeMap<>();
        map.put("alpha", 1);
        map.put("beta", 2);
        map.put("alpha", 3);
        map.containsKey("beta");
        map.remove("beta");

        Set<String> set = new HashSet<>();
        set.add("a");
        set.add("b");
        set.remove("b");
        set.contains("a");

        Comparator<Integer> comparator = (a, b) -> a.compareTo(b);
        Collections.sort(list, comparator);
        if (list.isEmpty() || linked.isEmpty() || map.isEmpty() || set.isEmpty()) {
            throw new SpriteNotFoundException("Collection demo failed");
        }
    }

    public static String analyzeDates() {
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(1);
        LocalDate past = today.minusDays(1);
        boolean compare = future.isAfter(past) && today.isAfter(past) && !today.isAfter(future);
        return today.toString() + future.compareTo(today) + compare;
    }
}
