package co.uk.mrpineapple.pinesfoodmod.client.screens;

import co.uk.mrpineapple.pinesfoodmod.client.RenderUtil;
import co.uk.mrpineapple.pinesfoodmod.client.screens.components.CheckBox;
import co.uk.mrpineapple.pinesfoodmod.client.screens.handlers.PizzaBoardScreenHandler;
import co.uk.mrpineapple.pinesfoodmod.common.recipe.PizzaRecipe;
import co.uk.mrpineapple.pinesfoodmod.common.recipe.util.RecipeUtil;
import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaBoardTileEntity;
import co.uk.mrpineapple.pinesfoodmod.common.util.InventoryUtil;
import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class PizzaBoardScreen extends ContainerScreen<PizzaBoardScreenHandler> {

    private PlayerInventory playerInventory;
    private PlayerEntity playerEntity;
    private PizzaBoardTileEntity pizzaBoard;
    private List<InputItem> materials;
    private List<InputItem> filteredMaterials;
    protected static NonNullList<PizzaRecipe> recipes;
    protected static int currentIndex = 0;
    private static int previousIndex = 0;
    private static int oldRecipesSize = 0;
    private CheckBox checkBoxInputs;
    private Button btnCraft;
    private static final ResourceLocation GUI = new ResourceLocation(PinesFoodMod.ID + ":textures/gui/pizza_board.png");
    private static final ResourceLocation GUI_COMPONENTS = new ResourceLocation(PinesFoodMod.ID + ":textures/gui/pizza_board_components.png");
    private ItemTransformVec3f displayProperty;
    private ItemTransformVec3f prevDisplayProperty;
    private static boolean showRemaining = false;

    public PizzaBoardScreen(PizzaBoardScreenHandler container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.playerEntity = playerInventory.player;
        this.pizzaBoard = container.getPizzaBoardTileEntity();
        this.imageWidth = 289;
        this.imageHeight = 202;
        this.materials = new ArrayList<>();
        recipes = RecipeUtil.Pizza.getAll(playerInventory.player.level);
        if(oldRecipesSize != recipes.size()) {
            currentIndex = 0;
            previousIndex = 0;
            oldRecipesSize = recipes.size();
        }
    }

    @Override
    protected void init() {
        super.init();
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;
        this.addButton(new Button(startX + 188, startY + 168, 15, 20, new StringTextComponent("<"), button -> {
            if(currentIndex - 1 < 0) {
                this.fetchItem(recipes.size() - 1);
            } else {
                this.fetchItem(currentIndex - 1);
            }
        }));
        this.addButton(new Button(startX + 262, startY + 168, 15, 20, new StringTextComponent(">"), button -> {
            if(currentIndex + 1 >= recipes.size()) {
                this.fetchItem(0);
            } else {
                this.fetchItem(currentIndex + 1);
            }
        }));
        this.btnCraft = this.addButton(new Button(startX + 188 + 15, startY + 168, 59, 20, new TranslationTextComponent("screen.pinesfoodmod.button.make"), button -> {
            //Logic for crafting (packets)
        }));
        this.btnCraft.active = false;
        this.checkBoxInputs = this.addButton(new CheckBox(startX + 80, startY + 15, new TranslationTextComponent("screen.pinesfoodmod.button.show_remaining")));
        this.checkBoxInputs.setToggled(PizzaBoardScreen.showRemaining);
        this.fetchItem(currentIndex);
    }

    @Override
    public void tick() {
        super.tick();

        for(InputItem input : this.materials) {
            input.update();
        }

        boolean canCraft = true;
        for(InputItem input : this.materials) {
            if(!input.isEnabled()) {
                canCraft = false;
                break;
            }
        }

        this.btnCraft.active = canCraft;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean result = super.mouseClicked(mouseX, mouseY, button);
        PizzaBoardScreen.showRemaining = this.checkBoxInputs.isToggled();
        return result;
    }

    private void fetchItem(int index) {
        previousIndex = currentIndex;
        this.prevDisplayProperty = this.displayProperty;

        PizzaRecipe recipe = recipes.get(index);
        ItemStack stack = recipe.getItem();

        this.materials.clear();
        this.displayProperty = RenderUtil.getModel(stack.getItem()).getTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND);

        List<ItemStack> materials = recipe.getInputs();
        if(materials != null) {
            for(ItemStack material : materials) {
                InputItem item = new InputItem(material);
                item.update();
                this.materials.add(item);
            }
            currentIndex = index;
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);

        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;

        for(int x = 0; x < 3; x++) {
            for (int i = 0; i < this.filteredMaterials.size(); i++) {
                int itemY = startY + i * 19 + 6 + 57;
                if (RenderUtil.isMouseWithin(mouseX, mouseY, startX + 11 + (x * 90), itemY - 37, 80, 19) && i < 3) {
                    InputItem inputItem = this.filteredMaterials.get(i + (x * 3));
                    if (!inputItem.getStack().isEmpty()) {
                        this.renderTooltip(stack, inputItem.getStack(), mouseX, mouseY); //Ingredient Tooltips
                    }
                }
            }
        }
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        partialTicks = Minecraft.getInstance().getFrameTime();

        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;

        RenderSystem.enableBlend();
        this.minecraft.getTextureManager().bind(GUI);
        this.blit(stack, startX, startY + 85 + 10, 0, 158, 176, 98); //Inventory

        this.blit(stack, startX + 176 + 6, startY + 85 + 10, 176, 158, 75, 98); //Left Recipe Display
        this.blit(stack, startX + 176 + 6 + 75, startY + 85 + 10, 200, 158, 10, 98); //Connecting Recipe Display
        this.blit(stack, startX + 176 + 6 + 85, startY + 85 + 10, 241, 158, 15, 98); //Right Recipe Display

        this.blit(stack, startX, startY + 15 - 6, 0, 78, 176, 80); //Top Display Left
        this.blit(stack, startX + 176, startY + 15 - 6, 20, 78, 103, 80); //Top Display Connector
        this.blit(stack, startX + 176 + 100 + 3, startY + 15 - 6, 253, 78, 3, 80); //Top Display Right

        PizzaRecipe recipe = recipes.get(currentIndex);
        StringBuilder builder = new StringBuilder(recipe.getItem().getHoverName().getString());
        String outputName = builder.toString();

        String raw = outputName.substring(0, 3);
        String pizza = outputName.substring(outputName.length() - 5, outputName.length());
        int textXValue = startX + 232;

        if(raw.equalsIgnoreCase("raw")) {
            outputName = outputName.substring(4);
            textXValue = startX + 231;
            if(pizza.equalsIgnoreCase("pizza")) {
                outputName = outputName.substring(0, outputName.length() - 5);
                textXValue = startX + 233;
            }
        }

        drawCenteredString(stack, this.font, outputName, textXValue, startY + 112, Color.WHITE.getRGB());

        this.filteredMaterials = this.getMaterials();
        for(int x = 0; x < 3; x++) {
            for(int j = 0; j < 3; j++) {
                int moveOnXAxis = 10 - (90 * j);
                int moveOnYAxis = 37;
                this.minecraft.getTextureManager().bind(GUI_COMPONENTS);

                InputItem materialItem = this.filteredMaterials.get(x + (j*3));
                ItemStack itemStack = materialItem.stack;
                if (itemStack.isEmpty()) {
                    RenderHelper.turnOff();
                    this.blit(stack, (startX - moveOnXAxis) + 21, (startY - moveOnYAxis - 38) + x * 19 + 6 + 95, 0, 19, 80, 19);
                } else {
                    RenderHelper.turnOff();
                    if (materialItem.isEnabled()) {
                        this.blit(stack, (startX - moveOnXAxis) + 21, (startY - moveOnYAxis - 38) + x * 19 + 6 + 95, 0, 0, 80, 19); //When An Item Is Present
                    } else {
                        this.blit(stack, (startX - moveOnXAxis) + 21, (startY - moveOnYAxis - 38) + x * 19 + 6 + 95, 0, 38, 80, 19); //When an Item Isn't Present
                    }

                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                    String name = itemStack.getHoverName().getString();
                    if (this.font.width(name) > 55) {
                        name = this.font.plainSubstrByWidth(name, 50).trim() + "..."; //Cuts off item name if long
                    }
                    this.font.draw(stack, name, startX - moveOnXAxis + 43, (startY - moveOnYAxis - 38) + x * 19 + 6 + 6 + 95, Color.WHITE.getRGB()); //Renders Ingredients Name

                    Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(itemStack, startX - moveOnXAxis + 23, (startY - moveOnYAxis - 38) + x * 19 + 6 + 1 + 95); //Renders Item For Recipe

                    if (this.checkBoxInputs.isToggled()) {
                        int count = InventoryUtil.getItem(Minecraft.getInstance().player, itemStack);
                        itemStack = itemStack.copy();
                        itemStack.setCount(itemStack.getCount() - count);
                    }
                    Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(this.font, itemStack, startX - moveOnXAxis + 23, (startY - moveOnYAxis - 38) + x * 19 + 6 + 1 + 95, null);//Renders Amount Of Item Needed
                }
            }
        }
    }


    private List<InputItem> getMaterials() {
        List<InputItem> materials = NonNullList.withSize(9, new InputItem(ItemStack.EMPTY));
        List<InputItem> filteredMaterials = this.materials.stream().filter(materialItem -> this.checkBoxInputs.isToggled() ? !materialItem.isEnabled() : !materialItem.stack.isEmpty()).collect(Collectors.toList());
        for(int i = 0; i < filteredMaterials.size() && i < materials.size(); i++) {
            materials.set(i, filteredMaterials.get(i));
        }
        return materials;
    }

    @Override
    protected void renderLabels(MatrixStack stack, int mouseX, int mouseY) {
        this.font.draw(stack, this.playerInventory.getDisplayName().getString(), this.imageWidth - 280, 100, 4210752);
        this.font.draw(stack, new TranslationTextComponent("screen.pinesfoodmod.label.recipe"), this.imageWidth - 102, 100, 4210752);
        this.font.draw(stack, new TranslationTextComponent("screen.pinesfoodmod.label.ingredients"), this.imageWidth - 280, 15, 4210752);
    }

    public static ItemStack currentItemShowing() {
        PizzaRecipe recipe = PizzaBoardScreen.recipes.get(currentIndex);
        return recipe.getItem();
    }

    public static class InputItem {
        public static final InputItem EMPTY = new InputItem();

        private boolean enabled = false;
        private ItemStack stack = ItemStack.EMPTY;

        private InputItem() {}

        private InputItem(ItemStack stack) {
            this.stack = stack;
        }

        public ItemStack getStack() {
            return stack;
        }

        public void update() {
            if(!this.stack.isEmpty()) {
                this.enabled = InventoryUtil.hasItemStack(Minecraft.getInstance().player, this.stack);
            }
        }

        public boolean isEnabled() {
            return this.stack.isEmpty() || this.enabled;
        }
    }
}
