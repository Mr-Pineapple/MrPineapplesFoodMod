package co.uk.mrpineapple.pinesfoodmod.client.screens;

import co.uk.mrpineapple.pinesfoodmod.client.screens.handlers.PizzaBoardScreenHandler;
import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PizzaBoardScreen extends ContainerScreen<PizzaBoardScreenHandler> {
    private static final ResourceLocation GUI = new ResourceLocation(PinesFoodMod.ID + ":textures/gui/pizza_board.png");
    private static final ResourceLocation GUI_COMPONENTS = new ResourceLocation(PinesFoodMod.ID + ":textures/gui/pizza_board_components.png");
    private PlayerInventory playerInventory;
    private Button craftButton;

    public PizzaBoardScreen(PizzaBoardScreenHandler container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.imageWidth = 289;
        this.imageHeight = 202;
        this.playerInventory = playerInventory;
    }

    @Override
    protected void init() {
        super.init();
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;
        this.addButton(new Button(startX + 188, startY + 168, 15, 20, new StringTextComponent("<"), button -> {

        }));
        this.addButton(new Button(startX + 262, startY + 168, 15, 20, new StringTextComponent(">"), button -> {

        }));
        this.craftButton = this.addButton(new Button(startX + 188 + 15, startY + 168, 59, 20, new TranslationTextComponent("screen.pinesfoodmod.pizza_board.make"), button -> {

        }));
        this.craftButton.active = false;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;

        this.minecraft.getTextureManager().bind(GUI);
        this.blit(matrixStack, startX, startY + 85 + 10, 0, 158, 176, 98); //Inventory
        this.blit(matrixStack, startX + 176 + 6, startY + 85 + 10, 176, 158, 75, 98); //Left Recipe Display
        this.blit(matrixStack, startX + 176 + 6 + 75, startY + 85 + 10, 200, 158, 10, 98); //Connecting Recipe Display
        this.blit(matrixStack, startX + 176 + 6 + 85, startY + 85 + 10, 241, 158, 15, 98); //Right Recipe Display
        this.blit(matrixStack, startX, startY + 15 - 6, 0, 78, 176, 80); //Top Display Left
        this.blit(matrixStack, startX + 176, startY + 15 - 6, 20, 78, 103, 80); //Top Display Connector
        this.blit(matrixStack, startX + 176 + 100 + 3, startY + 15 - 6, 253, 78, 3, 80); //Top Display Right
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.playerInventory.getDisplayName().getString(), this.imageWidth - 280, 100, 4210752);
        this.font.draw(matrixStack, "Ingredients", this.imageWidth - 280, 15, 4210752);
    }
}
