package com.elmsuf.school.tasks;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class WhatcherService implements Runnable {

    @Override
    public void run() {
        try (WatchService service = FileSystems.getDefault().newWatchService()) {


            Map<WatchKey, Path> keyMap = new HashMap<>();
            Path path = Paths.get("mailfxserver/persistence");
            keyMap.put(path.register(service,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY),
                    path);

            WatchKey watchKey;

            do {
                watchKey = service.take();
                Path eventDir = keyMap.get(watchKey);

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path context = (Path) event.context();
                    System.out.printf("eventDir { %s : kind[%s] : context[%s] }%n", eventDir, kind, context);

                }
            } while (watchKey.reset());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new WhatcherService().run();
    }
}
