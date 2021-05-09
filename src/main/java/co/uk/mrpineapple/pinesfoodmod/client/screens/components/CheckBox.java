package co.uk.mrpineapple.pinesfoodmod.client.screens.components;

import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.types.templates.Check;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CheckBox extends Widget {
    static final ResourceLocation GUI = new ResourceLocation(PinesFoodMod.ID + ":textures/gui/components.png");
    boolean toggled = false;

    public CheckBox(int left, int top, ITextComponent text) {
        super(left, top, 8, 8, text);
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public boolean isToggled() {
        return toggled;
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(GUI);
        this.blit(stack, this.x, this.y - 1, 0, 0, 8, 8);
        if(this.toggled) {
            this.blit(stack, this.x, this.y - 1, 8, 0, 9, 8);
        }
        minecraft.font.draw(stack, I18n.get(this.getMessage().getString()), this.x + 12, this.y, 4210752);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.toggled = !this.toggled;
    }
}
