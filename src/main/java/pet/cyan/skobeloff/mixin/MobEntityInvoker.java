package pet.cyan.skobeloff.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.mob.MobEntity;

@Mixin(MobEntity.class)
public interface MobEntityInvoker {
    @Invoker("isDisallowedInPeaceful")
    public boolean invokeIsDisallowedInPeaceful();
}
