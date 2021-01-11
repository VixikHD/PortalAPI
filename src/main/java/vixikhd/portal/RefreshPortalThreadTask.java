package vixikhd.portal;

import cn.nukkit.scheduler.Task;
import lombok.Getter;

public class RefreshPortalThreadTask extends Task {

    @Getter
    private final Portal plugin;

    public RefreshPortalThreadTask(Portal plugin) {
        this.plugin = plugin;
    }

    public void onRun(int currentTick) {
        this.getPlugin().tick();
    }
}
