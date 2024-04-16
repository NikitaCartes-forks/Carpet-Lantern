package ru.nern.carpetlantern.mixin;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.carpetlantern.CarpetLanternSettings;

import static carpettisaddition.utils.Messenger.sendToConsole;
import static carpettisaddition.utils.Messenger.tell;

//Support for locational update suppression message.
@Mixin(value = UpdateSuppressionContext.class, remap = false)
public abstract class UpdateSuppressionContextMixin {

    @Shadow @Final private static Translator tr;

    @Shadow public abstract MutableText getMessageText();

    private BlockPos pos;
    private String dimensionId;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void carpetlantern$saveData(Throwable cause, World world, BlockPos pos, CallbackInfo ci) {
        this.pos = pos;
        this.dimensionId = world.getDimensionKey().getValue().toString();
    }

    @Inject(method = "report", at = @At(value = "HEAD"), cancellable = true)
    private void carpetlantern$updateSuppressionLocalBroadcast(CallbackInfo ci) {
        if(CarpetLanternSettings.updateSuppressionMessageRange > -1) {
            ci.cancel();

            MutableText message = Messenger.formatting(tr.tr("report_message", this.getMessageText()), Formatting.RED, Formatting.ITALIC);
            Logger logger = LoggerRegistry.getLogger("updateSuppressedCrashes");
            if (logger != null) {
                logger.log(() -> new MutableText[]{message});
                Messenger.sendToConsole(message);
            } else {
                broadcastLocal(message, pos, dimensionId);
            }
        }
    }

    //Custom
    @Unique
    private static void broadcastLocal(MutableText text, BlockPos pos, String dimensionId)
    {
        sendToConsole(text);
        if (CarpetTISAdditionServer.minecraft_server != null) {
            CarpetTISAdditionServer.minecraft_server.getPlayerManager().getPlayerList().forEach(player -> {
                if(player.getEntityWorld().getDimensionKey().getValue().toString().toString().equals(dimensionId) && pos.isWithinDistance(player.getPos(), CarpetLanternSettings.updateSuppressionMessageRange)) {
                    if(CarpetLanternSettings.clUseCarpetMessageFormat) {
                        tell(player, text);
                    }else {
                        player.sendMessage(text);
                    }
                }

            });
        }
    }
}
