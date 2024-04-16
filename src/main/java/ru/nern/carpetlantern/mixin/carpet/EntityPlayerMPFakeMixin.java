package ru.nern.carpetlantern.mixin.carpet;

import carpet.patches.EntityPlayerMPFake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import ru.nern.carpetlantern.IPlayerAccessor;

//Support for storing name of whoever summoned a carpet bot.
@Mixin(EntityPlayerMPFake.class)
public class EntityPlayerMPFakeMixin implements IPlayerAccessor {
    @Unique
    private String summonerName;
    @Override
    public void carpetlantern$setSummonerName(String name) {
        this.summonerName = name;
    }

    @Override
    public String carpetlantern$getSummonerName() {
        return summonerName;
    }
}
