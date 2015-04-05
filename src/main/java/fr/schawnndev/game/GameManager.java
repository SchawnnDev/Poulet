package fr.schawnndev.game;

import fr.schawnndev.Main;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by SchawnnDev on 04/04/2015.
 */

public class GameManager {

    @Getter (value = AccessLevel.PUBLIC)
    @Setter (value = AccessLevel.PUBLIC)
    private static Game currentGame = Main.defaultGame;

}
