package com.placeholder.factory_game.ui.view;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.placeholder.factory_game.ui.model.InventoryViewModel;

public class InventoryView extends View<InventoryViewModel> {
    private final Image selectionImg;
    private Group selectedItem;

    public InventoryView(Stage stage, Skin skin, InventoryViewModel viewModel) {
        super(stage, skin, viewModel);
        this.selectionImg = new Image(skin, "selection");
//        this.selectionImg.setTouchable(Touchable.disabled);
//        this.selectedItem = findActor(MenuOption.START_GAME.name());
        selectInventoryItem(this.selectedItem);
    }

    /**
     * Selects a menu item and animates the selection indicator.
     */
    private void selectInventoryItem(Group inventoryItem) {
        if (selectionImg.getParent() != null) {
            selectionImg.getParent().removeActor(selectionImg);
        }
        this.selectedItem = inventoryItem;

        float extraSize = 7f;
        float halfExtraSize = extraSize * 0.5f;
        float resizeTime = 0.2f;

        inventoryItem.addActor(selectionImg);
        selectionImg.setPosition(-halfExtraSize, -halfExtraSize);
        selectionImg.setSize(inventoryItem.getWidth() + extraSize, inventoryItem.getHeight() + extraSize);
        selectionImg.clearActions();
        selectionImg.addAction(Actions.forever(Actions.sequence(
            Actions.parallel(
                Actions.sizeBy(extraSize, extraSize, resizeTime, Interpolation.linear),
                Actions.moveBy(-halfExtraSize, -halfExtraSize, resizeTime, Interpolation.linear)
            ),
            Actions.parallel(
                Actions.sizeBy(-extraSize, -extraSize, resizeTime, Interpolation.linear),
                Actions.moveBy(halfExtraSize, halfExtraSize, resizeTime, Interpolation.linear)
            )
        )));
    }

    /**
     * Sets up the main UI layout with banner and menu content.
     */
    @Override
    protected void setupUI() {
        setFillParent(true);

        setupInventoryContent();

    }

    /**
     * Creates the menu content with buttons and volume sliders.
     */
    private void setupInventoryContent() {
            // Creamos la tabla que contendrá todo el grid
            Table gridTable = new Table();
            gridTable.center();
            gridTable.pad(20f);

            int rows = 3; // Número de filas del grid
            int cols = 3; // Número de columnas del grid

            // Creamos un array 2D para los botones (opcional, útil si quieres acceder a ellos por fila/col)
            Group[][] inventoryItems = new Group[rows][cols];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    // Cada "item" del inventario es un Group que puede contener imagen + label
                    Group itemGroup = new Group();
                    itemGroup.setName("ITEM_" + r + "_" + c);

                    // Imagen del item (puede ser reemplazada por cualquier drawable)
                    Image itemImage = new Image(skin, "item_placeholder");
                    itemImage.setSize(80f, 80f);
                    itemGroup.addActor(itemImage);

                    // Label opcional para el nombre o cantidad
                    Label itemLabel = new Label("Item " + (r * cols + c + 1), skin);
                    itemLabel.setColor(skin.getColor("green"));
                    itemLabel.setPosition(0, -20f); // Posición debajo de la imagen
                    itemGroup.addActor(itemLabel);

                    // Definimos el tamaño del group para que la selección funcione correctamente
                    itemGroup.setSize(80f, 100f);

                    // Evento al entrar (para actualizar selección)
                    onEnter(itemGroup, this::selectInventoryItem);

                    // Evento de click (puedes vincular a viewModel o acción)
                    itemGroup.addListener(event -> {
                        System.out.println("Clicked: " + itemGroup.getName());
                        return false;
                    });

                    inventoryItems[r][c] = itemGroup;
                    gridTable.add(itemGroup).size(80f, 100f).pad(10f);
                }
                gridTable.row();
            }

            // Agregamos el grid a la vista
            add(gridTable).center().expand();

            // Seleccionamos el primer item por defecto
            selectInventoryItem(inventoryItems[0][0]);
    }


    // Hay que enviar el InventoryOption inventoryOption aquí
    private Slider setupVolumeSlider(Table contentTable, String title) {
        Table table = new Table();
//        table.setName(inventoryOption.name());
        Label label = new Label(title, skin);
        label.setColor(skin.getColor("green"));
        table.add(label).row();

        Slider slider = new Slider(0.0f, 1f, 0.05f, false, skin);
        table.add(slider);
        contentTable.add(table).padTop(10.0f).row();

        onEnter(table, this::selectInventoryItem);
        return slider;
    }

    /**
     * Moves selection to the next menu item.
     */
    @Override
    public void onDown() {
        Group menuContentTable = this.selectedItem.getParent();
        int currentIdx = menuContentTable.getChildren().indexOf(this.selectedItem, true);
        if (currentIdx == -1) {
            throw new GdxRuntimeException("'selectedItem' is not a child of 'menuContentTable'");
        }

        int numOptions = menuContentTable.getChildren().size;
        currentIdx = (currentIdx + 1) % numOptions;
        selectInventoryItem((Group) menuContentTable.getChild(currentIdx));
    }

    /**
     * Moves selection to the previous menu item.
     */
    @Override
    public void onUp() {
        Group menuContentTable = this.selectedItem.getParent();
        int currentIdx = menuContentTable.getChildren().indexOf(this.selectedItem, true);
        if (currentIdx == -1) {
            throw new GdxRuntimeException("'selectedItem' is not a child of 'menuContentTable'");
        }

        int numOptions = menuContentTable.getChildren().size;
        currentIdx = currentIdx == 0 ? numOptions - 1 : currentIdx - 1;
        selectInventoryItem((Group) menuContentTable.getChild(currentIdx));
    }

//    @Override
//    public void onRight() {
//        MenuOption menuOption = MenuOption.valueOf(this.selectedItem.getName());
//        switch (menuOption) {
//            case MUSIC_VOLUME, SOUND_VOLUME -> {
//                Slider slider = (Slider) this.selectedItem.getChild(1);
//                slider.setValue(slider.getValue() + slider.getStepSize());
//            }
//        }
//    }
//
//    @Override
//    public void onLeft() {
//        MenuOption menuOption = MenuOption.valueOf(this.selectedItem.getName());
//        switch (menuOption) {
//            case MUSIC_VOLUME, SOUND_VOLUME -> {
//                Slider slider = (Slider) this.selectedItem.getChild(1);
//                slider.setValue(slider.getValue() - slider.getStepSize());
//            }
//        }
//    }
//
//    @Override
//    public void onSelect() {
//        MenuOption menuOption = MenuOption.valueOf(this.selectedItem.getName());
//        switch (menuOption) {
//            case START_GAME -> viewModel.startGame();
//            case QUIT_GAME -> viewModel.quitGame();
//        }
//    }
//
//    private enum MenuOption {
//        START_GAME,
//        MUSIC_VOLUME,
//        SOUND_VOLUME,
//        QUIT_GAME
//    }
}
