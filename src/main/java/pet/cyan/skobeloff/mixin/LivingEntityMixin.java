package pet.cyan.skobeloff.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import pet.cyan.skobeloff.Skobeloff;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    public void cloakedDamagerCheck(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof LivingEntity attacker) {
            Optional<TrinketComponent> tc = TrinketsApi.getTrinketComponent(attacker);
            if (tc.isPresent() && tc.get().isEquipped(Skobeloff.SALT_PENDANT) && (LivingEntity)(Object)this instanceof MobEntity mob) {
                EntityType<?> entityType = mob.getType();
                TagKey<EntityType<?>> bosses = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("c","bosses"));
                if (!entityType.isIn(bosses) && ((MobEntityInvoker)mob).invokeIsDisallowedInPeaceful()) { // if entity isn't a boss (and is hostile), don't detect player
                    cir.setReturnValue(false); // if you can't detect the player, the player can't attack you
                }
            }
        }
    }
}
