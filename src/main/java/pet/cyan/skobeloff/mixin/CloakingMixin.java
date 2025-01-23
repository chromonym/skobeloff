package pet.cyan.skobeloff.mixin;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import pet.cyan.skobeloff.Skobeloff;

@Mixin(TargetPredicate.class)
public abstract class CloakingMixin {
    @Inject(at = @At("HEAD"), method = "test", cancellable = true)
    public void cloakingOverride(@Nullable LivingEntity baseEntity, LivingEntity targetEntity, CallbackInfoReturnable<Boolean> info) {
        Optional<TrinketComponent> tc = TrinketsApi.getTrinketComponent(targetEntity); // if a player is
        if (tc.isPresent() && tc.get().isEquipped(Skobeloff.SALT_PENDANT)) {
            if (baseEntity != null && baseEntity instanceof MobEntity mob) {
                EntityType<?> entityType = baseEntity.getType();
                TagKey<EntityType<?>> bosses = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("c","bosses"));
                if (!entityType.isIn(bosses) && ((MobEntityInvoker)mob).invokeIsDisallowedInPeaceful()) { // if entity isn't a boss (and is hostile), don't detect player
                    info.setReturnValue(false);
                }
            } else { // if no entity, don't detect player (?)
                info.setReturnValue(false);
            }
        }
    }
}
