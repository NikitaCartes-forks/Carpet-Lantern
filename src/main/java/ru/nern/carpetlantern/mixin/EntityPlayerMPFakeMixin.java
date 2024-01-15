package ru.nern.carpetlantern.mixin;

import carpet.patches.EntityPlayerMPFake;
import org.spongepowered.asm.mixin.Mixin;
import ru.nern.carpetlantern.IPlayerAccessor;

//Support for storing name of whoever summoned a carpet bot.
@Mixin(EntityPlayerMPFake.class)
public class EntityPlayerMPFakeMixin implements IPlayerAccessor {
    private String summonerName;
    @Override
    public void setSummonerName(String name) {
        this.summonerName = name;
    }

    @Override
    public String getSummonerName() {
        return summonerName;
    }
}
