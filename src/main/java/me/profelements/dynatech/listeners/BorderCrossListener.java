package me.profelements.dynatech.listeners;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.User;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.event.BorderClaimEvent;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.profelements.dynatech.DynaTechItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class BorderCrossListener {

    public BorderCrossListener() {
        GriefDefender.getEventManager().getBus().subscribe(BorderClaimEvent.class, e -> {
            //Bukkit.getLogger().info("BorderClaimEvent");
            Player player = Bukkit.getPlayer(e.getEntityUniqueId());
            if (player == null) return;
            //Bukkit.getLogger().info("BorderClaimEvent for " + player.getName());
            if (player.hasPermission("essentials.fly")) return;
            //Bukkit.getLogger().info("No essentials.fly");

            User user = e.getUser();
            if (user == null) return;
            //Bukkit.getLogger().info("IS a valid user");

            boolean hasAngelGem = false;
            PlayerInventory inventory = player.getInventory();
            for (int i=0; i<=40; i++) {
                if (SlimefunUtils.isItemSimilar(inventory.getItem(i), DynaTechItems.ANGEL_GEM, false)) {
                    hasAngelGem = true;
                    //Bukkit.getLogger().info("hasAngelGem = true");
                }
            }
            if (!hasAngelGem) return;

            Claim enterClaim = e.getEnterClaim();
            if (hasClaimAccess(enterClaim, user.getUniqueId())) {
                //Bukkit.getLogger().info("Enabling fly for " + player.getName());
                player.setFlying(true); // Might act weird
                player.setAllowFlight(true);
                return;
            }

            Claim exitClaim = e.getExitClaim();
            if (hasClaimAccess(exitClaim, user.getUniqueId())) {
                //Bukkit.getLogger().info("Disabling fly for " + player.getName());
                player.setFlying(false);
                player.setAllowFlight(false);
                player.setFallDistance(0f);
            }
        });
    }

    private boolean hasClaimAccess(Claim claim, UUID uuid) {
        return claim.getUserTrusts().contains(uuid) || claim.getOwnerUniqueId().equals(uuid);
    }
}
